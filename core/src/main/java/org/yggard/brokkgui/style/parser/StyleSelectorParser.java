package org.yggard.brokkgui.style.parser;

import org.apache.commons.lang3.StringUtils;
import org.yggard.brokkgui.style.tree.IStyleSelector;
import org.yggard.brokkgui.style.tree.StyleSelector;
import org.yggard.brokkgui.style.tree.StyleSelectorHierarchic;
import org.yggard.brokkgui.style.tree.StyleSelectorType;

public class StyleSelectorParser
{

    IStyleSelector[] readSelectors(String selector)
    {
        selector = selector.replace('{', ' ').trim();

        if (selector.contains(","))
        {
            IStyleSelector[] selectors = new IStyleSelector[StringUtils.countMatches(selector, ",") + 1];

            int i = 0;
            for (String subSelector : selector.split(","))
            {
                selectors[i] = this.readSingleSelector(cleanSelector(subSelector.trim()));
                i++;
            }
            return selectors;
        }
        else
            return new IStyleSelector[]{this.readSingleSelector(cleanSelector(selector))};
    }

    IStyleSelector readSingleSelector(String selector)
    {
        if (selector.contains(">"))
            return parseHierarchicSelector(selector);
        else
            return parseSimpleSelector(selector);
    }

    String cleanSelector(String selector)
    {
        StringBuilder cleanSelector = new StringBuilder(selector);

        int i = 0;
        while (i < cleanSelector.length())
        {
            if (i != 0 && cleanSelector.charAt(i) == ' ')
            {
                int count = 1;
                while (cleanSelector.charAt(i + count) == ' ')
                    count++;
                if (!this.isToken(cleanSelector.charAt(i + count)) && !this.isToken(cleanSelector.charAt(i - 1)))
                    cleanSelector.replace(i, i + count, ">>");
                else
                    cleanSelector.delete(i, i + count);
            }
            i++;
        }
        return cleanSelector.toString();
    }

    IStyleSelector parseHierarchicSelector(String selector)
    {
        boolean direct = false;

        if (selector.charAt(selector.indexOf('>') + 1) != '>')
            direct = true;
        String[] split = selector.split(direct ? ">" : ">>", 2);
        return new StyleSelectorHierarchic(parseSimpleSelector(split[0]), split[1].contains(">") ?
                parseHierarchicSelector(split[1]) : parseSimpleSelector(split[1]), direct);
    }

    IStyleSelector parseSimpleSelector(String selector)
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
            else if (pseudoClass == null)
                rtn.add(StyleSelectorType.TYPE, part);
            if (pseudoClass != null)
                rtn.add(StyleSelectorType.PSEUDOCLASS, pseudoClass);
        }
        return rtn;
    }

    boolean isToken(char c)
    {
        return StringUtils.contains(">", c);
    }
}
