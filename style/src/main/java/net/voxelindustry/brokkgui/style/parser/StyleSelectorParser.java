package net.voxelindustry.brokkgui.style.parser;

import net.voxelindustry.brokkgui.style.selector.IStyleSelector;
import net.voxelindustry.brokkgui.style.selector.StyleSelector;
import net.voxelindustry.brokkgui.style.selector.StyleSelectorHierarchic;
import net.voxelindustry.brokkgui.style.selector.StyleSelectorType;
import net.voxelindustry.brokkgui.style.selector.structural.StructuralSelectors;
import org.apache.commons.lang3.StringUtils;

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
            return parseHierarchic(selector);
        else
            return parseSimple(selector);
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

    IStyleSelector parseHierarchic(String selector)
    {
        boolean direct = false;

        if (selector.charAt(selector.lastIndexOf('>') - 1) != '>')
            direct = true;

        String[] split = selector.split("[>]+(?=[^>]*$)");

        if (!split[0].contains(">"))
            return new StyleSelectorHierarchic(parseSimple(split[0]), parseSimple(split[1]), direct);
        else
            return new StyleSelectorHierarchic(parseHierarchic(split[0]), parseSimple(split[1]), direct);
    }

    IStyleSelector parseSimple(String selector)
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
            {
                if (StructuralSelectors.isStructural(pseudoClass))
                    rtn.add(StyleSelectorType.STRUCTURAL_PSEUDOCLASS, pseudoClass);
                else
                    rtn.add(StyleSelectorType.PSEUDOCLASS, pseudoClass);
            }
        }
        return rtn;
    }

    boolean isToken(char c)
    {
        return StringUtils.contains(">", c);
    }
}
