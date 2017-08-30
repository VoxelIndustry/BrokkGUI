package org.yggard.brokkgui.style;

import java.util.*;

public class StyleSelector
{
    private EnumMap<StyleSelectorType, List<String>> selectors;
    private int computedSpecificity = -1;

    public StyleSelector()
    {
        this.selectors = new EnumMap<>(StyleSelectorType.class);
    }

    public StyleSelector addWildcard()
    {
        this.selectors.put(StyleSelectorType.WILDCARD, Collections.singletonList("*"));
        this.computedSpecificity = 1000;
        return this;
    }

    public StyleSelector addSelector(StyleSelectorType type, String selector)
    {
        if (!this.selectors.containsKey(type))
            this.selectors.put(type, new ArrayList<>());
        this.selectors.get(type).add(selector);
        this.computedSpecificity = -1;
        return this;
    }

    public int getSpecificity()
    {
        if (this.computedSpecificity == -1)
        {
            this.computedSpecificity = 0;
            this.selectors.forEach((type, list) -> this.computedSpecificity += list.size() * type.getSpecificity());
        }
        return this.computedSpecificity;
    }

    public Map<StyleSelectorType, List<String>> getSelectors()
    {
        return selectors;
    }

    public enum StyleSelectorType
    {
        TYPE(1), CLASS(10), PSEUDOCLASS(10), ID(100), WILDCARD(1000);

        int specificity;

        StyleSelectorType(int specificity)
        {
            this.specificity = specificity;
        }

        public int getSpecificity()
        {
            return this.specificity;
        }
    }
}
