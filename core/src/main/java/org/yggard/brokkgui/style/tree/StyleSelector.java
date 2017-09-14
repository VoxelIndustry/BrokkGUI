package org.yggard.brokkgui.style.tree;

import org.apache.commons.lang3.tuple.Pair;
import org.yggard.brokkgui.style.StyleHolder;

import java.util.ArrayList;
import java.util.List;

public class StyleSelector implements IStyleSelector
{
    private final List<Pair<StyleSelectorType, String>> selectors;
    private       int                                   computedSpecificity;

    public StyleSelector()
    {
        this.selectors = new ArrayList<>();
        this.computedSpecificity = -1;
    }

    public StyleSelector addWildcard()
    {
        this.add(StyleSelectorType.WILDCARD, "*");
        this.computedSpecificity = 1000;
        return this;
    }

    public StyleSelector add(StyleSelectorType type, String selector)
    {
        this.selectors.add(Pair.of(type, selector));
        this.computedSpecificity = -1;
        return this;
    }

    public int getSpecificity()
    {
        if (this.computedSpecificity == -1)
        {
            this.computedSpecificity = 0;
            for (Pair<StyleSelectorType, String> selector : this.selectors)
                this.computedSpecificity += selector.getKey().getSpecificity();
        }
        return this.computedSpecificity;
    }

    public List<Pair<StyleSelectorType, String>> getSelectors()
    {
        return selectors;
    }

    public boolean match(StyleHolder styleHolder)
    {
        for (Pair<StyleSelectorType, String> selector : this.selectors)
        {
            switch (selector.getKey())
            {
                case WILDCARD:
                    return true;
                case TYPE:
                    if (!selector.equals(styleHolder.getOwner().getType()))
                        return false;
                    break;
                case CLASS:
                    if (!styleHolder.getOwner().getStyleClass().getValue().contains(selector))
                        return false;
                    break;
                case ID:
                    if (!selector.equals(styleHolder.getOwner().getID()))
                        return false;
                    break;
                case PSEUDOCLASS:
                    if (!styleHolder.getOwner().getActivePseudoClass().getValue().contains(selector))
                        return false;
                    break;
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "{" + selectors.toString() + ", specificity=" + this.getSpecificity() + '}';
    }

    public boolean containsAll(List<Pair<StyleSelectorType,String>> selectors)
    {
        // TODO: Content check
        return false;
    }
}
