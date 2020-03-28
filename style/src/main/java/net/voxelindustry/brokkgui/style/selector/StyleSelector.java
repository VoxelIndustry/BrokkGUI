package net.voxelindustry.brokkgui.style.selector;

import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.selector.structural.StructuralSelector;
import net.voxelindustry.brokkgui.style.selector.structural.StructuralSelectors;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.voxelindustry.brokkgui.style.selector.StyleSelectorType.STRUCTURAL_PSEUDOCLASS;

public class StyleSelector implements IStyleSelector
{
    private final List<Pair<StyleSelectorType, String>> selectors;
    private final List<StructuralSelector>              structuralSelectors;
    protected     int                                   computedSpecificity;

    public StyleSelector()
    {
        this.selectors = new ArrayList<>();
        this.structuralSelectors = new ArrayList<>(1);
        this.computedSpecificity = -1;
    }

    public StyleSelector addWildcard()
    {
        this.add(StyleSelectorType.WILDCARD, "*");
        this.computedSpecificity = 0;
        return this;
    }

    @Override
    public StyleSelector add(StyleSelectorType type, String selector)
    {
        if (type == STRUCTURAL_PSEUDOCLASS)
            structuralSelectors.add(StructuralSelectors.fromString(selector));
        else
            this.selectors.add(Pair.of(type, selector));

        this.computedSpecificity = -1;
        return this;
    }

    @Override
    public int getSpecificity()
    {
        if (this.computedSpecificity == -1)
        {
            this.computedSpecificity = 0;
            for (Pair<StyleSelectorType, String> selector : this.selectors)
                this.computedSpecificity += selector.getKey().getSpecificity();

            this.computedSpecificity += structuralSelectors.size() * STRUCTURAL_PSEUDOCLASS.getSpecificity();
        }
        return this.computedSpecificity;
    }

    public List<Pair<StyleSelectorType, String>> getSelectors()
    {
        return selectors;
    }

    @Override
    public boolean match(StyleHolder styleHolder)
    {
        for (Pair<StyleSelectorType, String> selector : this.selectors)
        {
            if (selector.getKey() == StyleSelectorType.WILDCARD)
                return true;
            if (!this.checkSelector(selector, styleHolder))
                return false;
        }
        for (StructuralSelector structuralSelector : structuralSelectors)
        {
            if (!structuralSelector.test(styleHolder))
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
        return this.selectors.size() == other.selectors.size()
                && this.selectors.containsAll(other.selectors)
                && this.structuralSelectors.size() == other.structuralSelectors.size()
                && this.structuralSelectors.containsAll(other.structuralSelectors);
    }

    protected boolean checkSelector(Pair<StyleSelectorType, String> selector, StyleHolder styleHolder)
    {
        switch (selector.getKey())
        {
            case WILDCARD:
                return true;
            case TYPE:
                if (!selector.getValue().equals(styleHolder.getOwner().getType()))
                    return false;
                break;
            case CLASS:
                if (!styleHolder.getOwner().getStyleClass().getValue().contains(selector.getValue()))
                    return false;
                break;
            case ID:
                if (!selector.getValue().equals(styleHolder.getOwner().getID()))
                    return false;
                break;
            case PSEUDOCLASS:
                if (!styleHolder.getOwner().getActivePseudoClass().getValue().contains(selector.getValue()))
                    return false;
                break;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "StyleSelector{" +
                "selectors=" + selectors +
                ", structuralSelectors=" + structuralSelectors +
                ", computedSpecificity=" + computedSpecificity +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StyleSelector that = (StyleSelector) o;
        return Objects.equals(selectors, that.selectors) &&
                Objects.equals(structuralSelectors, that.structuralSelectors);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(selectors, structuralSelectors);
    }
}
