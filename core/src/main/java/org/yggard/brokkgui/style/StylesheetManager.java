package org.yggard.brokkgui.style;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.yggard.brokkgui.gui.BrokkGuiScreen;
import org.yggard.brokkgui.style.tree.StyleRule;
import org.yggard.brokkgui.style.tree.StyleSelector;
import org.yggard.brokkgui.style.tree.StyleTree;
import org.yggard.brokkgui.util.NumberedLineIterator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
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
        StyleTree tree = screen.getUserAgentStyleTree();
        screen.getStylesheetsProperty().getValue().forEach(styleSheet ->
        {
            try
            {
                tree.merge(this.loadStylesheet(styleSheet));
            } catch (IOException e)
            {
                logger.throwing("StylesheetManager", "refreshStyleSheets", e);
            }
        });
        screen.setStyleTree(tree);
    }

    public void refreshUserAgent(BrokkGuiScreen screen)
    {
        StyleTree tree = new StyleTree();
        try
        {
            tree.merge(this.loadStylesheet("assets/brokkgui/css/user_agent.css"));
        } catch (IOException e)
        {
            logger.throwing("StylesheetManager", "refreshUserAgentStyleSheets", e);
        }
        screen.setUserAgentStyleTree(tree);
    }

    StyleTree loadStylesheet(String styleSheet) throws IOException
    {
        InputStream input = StylesheetManager.class.getResourceAsStream(styleSheet);
        NumberedLineIterator iterator = new NumberedLineIterator(
                new InputStreamReader(input, Charsets.toCharset(StandardCharsets.UTF_8)));
        StyleTree tree = new StyleTree();

        while (iterator.hasNext())
        {
            String line = iterator.nextLine();
            if (StringUtils.isEmpty(line))
                continue;

            if (line.contains("{"))
                readBlock(readSelector(line), tree, iterator);
            else
                logger.severe("Expected { at line " + (iterator.getLineNumber()));
        }
        input.close();
        return tree;
    }

    private StyleSelector readSelector(String currentLine)
    {
        StyleSelector rtn = new StyleSelector();
        String selector = currentLine;

        selector = selector.replace('{', ' ').trim();

        String[] splitted = selector.split(" ");
        for (String part : splitted)
        {
            if (part.contains(":"))
            {
                rtn.setSelector(StyleSelector.StyleSelectorType.PSEUDOCLASS, part.split(":")[1]);
                part = part.split(":")[0];
            }
            if (part.startsWith("#"))
                rtn.setSelector(StyleSelector.StyleSelectorType.ID, part.substring(1));
            else if (part.startsWith("."))
                rtn.setSelector(StyleSelector.StyleSelectorType.CLASS, part.substring(1));
            else
                rtn.setSelector(StyleSelector.StyleSelectorType.TYPE, part);
        }
        return rtn;
    }

    private void readBlock(StyleSelector selector, StyleTree tree, NumberedLineIterator content)
    {
        if (!content.hasNext())
            return;
        String currentLine = content.nextLine();
        Set<StyleRule> elements = new HashSet<>();
        while (!StringUtils.contains(currentLine, "}"))
        {
            if (StringUtils.contains(currentLine, "{"))
            {
                logger.severe("Found opening bracket at line " + content.getLineNumber() + " while inside a block");
                return;
            }
            String[] rule = currentLine.replace(';', ' ').trim().split(":");
            elements.add(new StyleRule(rule[0].trim(), rule[1].trim()));
            if (!content.hasNext())
                return;
            currentLine = content.nextLine();
        }
        tree.addEntry(selector, elements);
    }
}
