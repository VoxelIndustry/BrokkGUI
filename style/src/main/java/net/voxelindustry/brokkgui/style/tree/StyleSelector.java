package net.voxelindustry.brokkgui.style.tree;

import net.voxelindustry.brokkgui.style.StyleHolder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StyleSelector implements IStyleSelector
{
    private final List<Pair<StyleSelectorType, String>> selectors;

    private int computedSpecificity;

    public StyleSelector()
    {
        this.selectors = new ArrayList<>();
        this.computedSpecificity = -1;
    }

    public StyleSelector addWildcard()
    {
        this.add(StyleSelectorType.WILDCARD, "*");
        this.computedSpecificity = 0;
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
            if (selector.getKey() == StyleSelectorType.WILDCARD)
                return true;
            if (!this.checkSelector(selector, styleHolder))
                return false;
        }
        return true;
    }

    @Override
    public boolean match(IStyleSelector selector)
    {
        if (selector == this)
            return true;
        if (!(selector instanceof StyleSelector))
            return false;
        StyleSelector other = (StyleSelector) selector;
        return this.selectors.size() == other.selectors.size() && this.selectors.containsAll(other.selectors);
    }

    protected boolean checkSelector(Pair<StyleSelectorType, String> selector, StyleHolder styleHolder)
    {
        switch (selector.getKey())
        {
            case WILDCARD:
                return true;
            case TYPE:
                if (!selector.getValue().equals(styleHolder.type()))
                    return false;
                break;
            case CLASS:
                if (!styleHolder.styleClass().getValue().contains(selector.getValue()))
                    return false;
                break;
            case ID:
                if (!selector.getValue().equals(styleHolder.id()))
                    return false;
                break;
            case PSEUDOCLASS:
                if (!styleHolder.activePseudoClass().getValue().contains(selector.getValue()))
                    return false;
                break;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "{" + selectors.toString() + ", specificity=" + this.getSpecificity() + '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StyleSelector that = (StyleSelector) o;
        return computedSpecificity == that.computedSpecificity &&
                Objects.equals(getSelectors(), that.getSelectors());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getSelectors(), computedSpecificity);
    }
}
