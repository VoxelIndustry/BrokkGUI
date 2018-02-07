package org.yggard.brokkgui.style;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.yggard.brokkgui.gui.BrokkGuiScreen;
import org.yggard.brokkgui.style.tree.IStyleSelector;
import org.yggard.brokkgui.style.tree.StyleList;
import org.yggard.brokkgui.style.tree.StyleRule;
import org.yggard.brokkgui.util.NumberedLineIterator;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class StylesheetManager
{
    private static StylesheetManager INSTANCE;

    public static StylesheetManager getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new StylesheetManager();
        return INSTANCE;
    }

    private Logger logger;

    private LoadingCache<String, StyleList> styleCache;
    private StyleSelectorParser             selectorParser;

    private StylesheetManager()
    {
        logger = Logger.getLogger("BrokkGui CSS Loader");

        this.styleCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, StyleList>()
                {
                    @Override
                    public StyleList load(@Nonnull String stylesheet) throws IOException
                    {
                        return loadStylesheet(stylesheet);
                    }
                });

        this.selectorParser = new StyleSelectorParser();
    }

    public void refreshStylesheets(BrokkGuiScreen screen)
    {
        StyleList list = screen.getUserAgentStyleTree();

        try
        {
            list.merge(this.loadStylesheets(screen.getStylesheetsProperty().getValue().toArray(
                    new String[screen.getStylesheetsProperty().size()])));
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        screen.setStyleTree(list);
    }

    StyleList loadStylesheets(String... styleSheets) throws ExecutionException
    {
        StyleList list = new StyleList();

        for (String styleSheet : styleSheets)
            list.merge(this.getStyleList(styleSheet));
        return list;
    }

    public StyleList getStyleList(String styleSheet) throws ExecutionException
    {
        return this.styleCache.get(styleSheet);
    }

    StyleList loadStylesheet(String styleSheet) throws IOException
    {
        StyleList list = new StyleList();

        InputStream input = StylesheetManager.class.getResourceAsStream(styleSheet);
        if (input == null)
            throw new FileNotFoundException("Cannot load stylesheet " + styleSheet);
        NumberedLineIterator iterator = new NumberedLineIterator(
                new InputStreamReader(input, Charsets.toCharset(StandardCharsets.UTF_8)));
        while (iterator.hasNext())
        {
            String line = iterator.nextLine();
            if (StringUtils.isEmpty(line))
                continue;

            if (line.contains("{"))
            {
                IStyleSelector[] selectors = selectorParser.readSelectors(line);
                List<StyleRule> rules = readBlock(iterator);

                for (IStyleSelector selector : selectors)
                    list.addEntry(selector, rules);
            }
            else
                logger.severe("Expected { at line " + iterator.getLineNumber());
        }
        input.close();
        return list;
    }

    private List<StyleRule> readBlock(NumberedLineIterator content)
    {
        if (!content.hasNext())
            return Collections.emptyList();
        String currentLine = content.nextLine();
        List<StyleRule> elements = new ArrayList<>();
        while (!StringUtils.contains(currentLine, "}"))
        {
            if (StringUtils.contains(currentLine, "{"))
            {
                logger.severe("Found opening bracket at line " + content.getLineNumber() + " while inside a block");
                return Collections.emptyList();
            }
            String[] rule = currentLine.replace(';', ' ').trim().split(":", 2);
            elements.add(new StyleRule(rule[0].trim(), rule[1].trim()));
            if (!content.hasNext())
                return Collections.emptyList();
            currentLine = content.nextLine();
        }
        return elements;
    }
}
