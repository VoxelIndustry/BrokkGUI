package org.yggard.brokkgui.style;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.yggard.brokkgui.gui.BrokkGuiScreen;
import org.yggard.brokkgui.util.NumberedLineIterator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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

    private StylesheetManager()
    {
        logger = Logger.getLogger("BrokkGui CSS Loader");
    }

    public void refreshStylesheets(BrokkGuiScreen screen)
    {
        screen.getStylesheetsProperty().getValue().forEach(styleSheet ->
        {
            try
            {
                this.loadStylesheet(styleSheet);
            } catch (IOException e)
            {
                logger.throwing("StylesheetManager", "refreshStyleSheets", e);
            }
        });
    }

    StyleTree loadStylesheet(String styleSheet) throws IOException
    {
        InputStream input = StylesheetManager.class.getResourceAsStream(styleSheet);
        NumberedLineIterator iterator = new NumberedLineIterator(new InputStreamReader(input, Charsets.toCharset
                (StandardCharsets.UTF_8)));
        StyleTree tree = new StyleTree();

        while (iterator.hasNext())
        {
            String line = iterator.nextLine();
            if (StringUtils.isEmpty(line))
                continue;

            if (line.contains("{"))
                readBlock(readSelector(iterator, line), tree, iterator);
            else
                logger.severe("Expected { at line " + (iterator.getLineNumber()));
        }
        input.close();
        return tree;
    }

    private StyleSelector readSelector(NumberedLineIterator content, String currentLine)
    {
        StyleSelector rtn = new StyleSelector();
        String selector = currentLine;

        selector = selector.replace('{', ' ').trim();

        String[] splitted = selector.split(" ");
        for (String part : splitted)
        {
            if (part.contains(":"))
            {
                rtn.addSelector(StyleSelector.StyleSelectorType.PSEUDOCLASS, part.split(":")[1]);
                part = part.split(":")[0];
            }
            if (part.startsWith("#"))
                rtn.addSelector(StyleSelector.StyleSelectorType.ID, part.substring(1));
            else if (part.startsWith("."))
                rtn.addSelector(StyleSelector.StyleSelectorType.CLASS, part.substring(1));
            else
                rtn.addSelector(StyleSelector.StyleSelectorType.TYPE, part);
        }
        return rtn;
    }

    private void readBlock(StyleSelector selector, StyleTree tree, NumberedLineIterator content)
    {
        if (!content.hasNext())
            return;
        String currentLine = content.nextLine();
        List<String> elements = new ArrayList<>();
        while (!StringUtils.contains(currentLine, "}"))
        {
            if (StringUtils.contains(currentLine, "{"))
            {
                logger.severe("Found opening bracket at line " + content.getLineNumber() + " while inside a block");
                return;
            }
            elements.add(currentLine.replace(';', ' ').trim());
            if (!content.hasNext())
                return;
            currentLine = content.nextLine();
        }
        tree.addEntry(selector, elements);
    }
}
