package org.yggard.brokkgui.style.parser;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.yggard.brokkgui.style.StylesheetManager;
import org.yggard.brokkgui.style.tree.IStyleSelector;
import org.yggard.brokkgui.style.tree.StyleList;
import org.yggard.brokkgui.style.tree.StyleRule;
import org.yggard.brokkgui.util.NumberedLineIterator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class StylesheetParser
{
    private StyleSelectorParser selectorParser;
    private Logger              logger;

    public StylesheetParser(Logger logger)
    {
        this.selectorParser = new StyleSelectorParser();

        this.logger = logger;
    }

    public StyleList loadStylesheet(String styleSheet) throws IOException
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
