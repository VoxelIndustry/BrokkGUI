package org.yggard.brokkgui.style.tree;

import org.yggard.brokkgui.style.StyleHolder;

import java.util.*;

public class StyleSelector
{
    private EnumMap<StyleSelectorType, List<String>> selectors;
    private int                                      computedSpecificity  = -1;
    private int                                      inheritedSpecificity = 0;

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
            this.computedSpecificity = this.inheritedSpecificity;
            this.selectors.forEach((type, list) -> this.computedSpecificity += list.size() * type.getSpecificity());
        }
        return this.computedSpecificity;
    }

    public Map<StyleSelectorType, List<String>> getSelectors()
    {
        return selectors;
    }

    public void setInheritedSpecificity(int inheritedSpecificity)
    {
        this.inheritedSpecificity = inheritedSpecificity;
    }

    public boolean match(StyleHolder styleHolder)
    {
        for (Map.Entry<StyleSelectorType, List<String>> selector : this.selectors.entrySet())
        {
            switch (selector.getKey())
            {
                case WILDCARD:
                    return true;
                case TYPE:
                    if (!selector.getValue().stream().allMatch(styleHolder.getOwner().getType()::equals))
                        return false;
                    break;
                case CLASS:
                    if (!styleHolder.getOwner().getStyleClass().getValue().containsAll(selector.getValue()))
                        return false;
                    break;
                case ID:
                    if (!selector.getValue().stream().allMatch(styleHolder.getOwner().getID()::equals))
                        return false;
                    break;
                case PSEUDOCLASS:
                    if (!styleHolder.getOwner().getActivePseudoClass().getValue().containsAll(selector.getValue()))
                        return false;
                    break;
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "{selectors=" + selectors + ", specificity=" + this.getSpecificity() + '}';
    }

    public enum StyleSelectorType
    {
        WILDCARD(1000), TYPE(1), CLASS(10), ID(100), PSEUDOCLASS(10);

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
