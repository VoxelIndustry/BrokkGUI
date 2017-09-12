package org.yggard.brokkgui.style.tree;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class StyleSelectorList
{
    private List<Pair<StyleSelectorType, String>> selectors;

    public StyleSelectorList()
    {
        this.selectors = new ArrayList<>();
    }

    public StyleSelectorList add(StyleSelectorType type, String selector)
    {
        this.selectors.add(Pair.of(type, selector));
        return this;
    }

    public List<Pair<StyleSelectorType, String>> getSelectors()
    {
        return selectors;
    }

    @Override
    public String toString()
    {
        return '{' + selectors.toString() + '}';
    }
}