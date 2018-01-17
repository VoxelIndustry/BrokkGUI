package org.yggard.brokkgui.style;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.yggard.brokkgui.gui.BrokkGuiScreen;
import org.yggard.brokkgui.style.tree.*;
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
        StyleList tree = screen.getUserAgentStyleTree();

        try
        {
            tree = this.loadStylesheet(ArrayUtils.addAll(new String[]{screen.getUserAgentStylesheetProperty()
                    .getValue()}, screen.getStylesheetsProperty().getValue().toArray(new String[screen
                    .getStylesheetsProperty().size()])));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        screen.setStyleTree(tree);
    }

    StyleList loadStylesheet(String... styleSheets) throws IOException
    {
        StyleList tree = new StyleList();

        for (String styleSheet : styleSheets)
        {
            InputStream input = StylesheetManager.class.getResourceAsStream(styleSheet);
            NumberedLineIterator iterator = new NumberedLineIterator(
                    new InputStreamReader(input, Charsets.toCharset(StandardCharsets.UTF_8)));
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
        }
        return tree;
    }

    private IStyleSelector readSelector(String currentLine)
    {
        String selector = currentLine;

        selector = selector.replace('{', ' ').trim().replace(" ", "");

        if (selector.contains(">"))
            return parseHierarchicSelector(selector);
        else
            return parseSimpleSelector(selector);
    }

    private IStyleSelector parseHierarchicSelector(String selector)
    {
        boolean direct = false;

        if (selector.charAt(selector.indexOf('>') + 1) != '>')
            direct = true;
        String[] splitted = selector.split(direct ? ">" : ">>", 2);
        return new StyleSelectorHierarchic(parseSimpleSelector(splitted[0]), splitted[1].contains(">") ?
                parseHierarchicSelector(splitted[1]) : parseSimpleSelector(splitted[1]), direct);
    }

    private IStyleSelector parseSimpleSelector(String selector)
    {
        StyleSelector rtn = new StyleSelector();
        for (String part : selector.split("(?=[.#:])"))
        {
            String pseudoClass = null;
            if (part.contains(":"))
            {
                pseudoClass = part.split(":")[1];
                part = part.split(":")[0];
            }
            if (part.startsWith("#"))
                rtn.add(StyleSelectorType.ID, part.substring(1));
            else if (part.startsWith("."))
                rtn.add(StyleSelectorType.CLASS, part.substring(1));
            else if(pseudoClass == null)
                rtn.add(StyleSelectorType.TYPE, part);
            if (pseudoClass != null)
                rtn.add(StyleSelectorType.PSEUDOCLASS, pseudoClass);
        }
        return rtn;
    }

    private void readBlock(IStyleSelector selectors, StyleList tree, NumberedLineIterator content)
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
            String[] rule = currentLine.replace(';', ' ').trim().split(":",2);
            elements.add(new StyleRule(rule[0].trim(), rule[1].trim()));
            if (!content.hasNext())
                return;
            currentLine = content.nextLine();
        }
        tree.addEntry(selectors, elements);
    }
}
