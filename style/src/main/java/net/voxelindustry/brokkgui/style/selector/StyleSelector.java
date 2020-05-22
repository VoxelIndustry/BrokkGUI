package net.voxelindustry.brokkgui.style.selector;

import net.voxelindustry.brokkgui.style.StyleComponent;
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
        selectors = new ArrayList<>();
        structuralSelectors = new ArrayList<>(1);
        computedSpecificity = -1;
    }

    public StyleSelector addWildcard()
    {
        add(StyleSelectorType.WILDCARD, "*");
        computedSpecificity = 0;
        return this;
    }

    @Override
    public StyleSelector add(StyleSelectorType type, String selector)
    {
        if (type == STRUCTURAL_PSEUDOCLASS)
            structuralSelectors.add(StructuralSelectors.fromString(selector));
        else
            selectors.add(Pair.of(type, selector));

        computedSpecificity = -1;
        return this;
    }

    @Override
    public int getSpecificity()
    {
        if (computedSpecificity == -1)
        {
            computedSpecificity = 0;
            for (Pair<StyleSelectorType, String> selector : selectors)
                computedSpecificity += selector.getKey().getSpecificity();

            computedSpecificity += structuralSelectors.size() * STRUCTURAL_PSEUDOCLASS.getSpecificity();
        }
        return computedSpecificity;
    }

    public List<Pair<StyleSelectorType, String>> getSelectors()
    {
        return selectors;
    }

    public boolean isSupersetOf(StyleSelector selector)
    {
        // This check does not use the structuralSelectors since they cannot be compared effectively
        return selectors.containsAll(selector.selectors);
    }

    @Override
    public boolean match(StyleComponent styleComponent)
    {
        for (Pair<StyleSelectorType, String> selector : selectors)
        {
            if (selector.getKey() == StyleSelectorType.WILDCARD)
                return true;
            if (!checkSelector(selector, styleComponent))
                return false;
        }
        for (StructuralSelector structuralSelector : structuralSelectors)
        {
            if (!structuralSelector.test(styleComponent))
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
        return selectors.size() == other.selectors.size()
                && selectors.containsAll(other.selectors)
                && structuralSelectors.size() == other.structuralSelectors.size()
                && structuralSelectors.containsAll(other.structuralSelectors);
    }

    protected boolean checkSelector(Pair<StyleSelectorType, String> selector, StyleComponent styleComponent)
    {
        switch (selector.getKey())
        {
            case WILDCARD:
                return true;
            case TYPE:
                if (!selector.getValue().equals(styleComponent.element().type()))
                    return false;
                break;
            case CLASS:
                if (!styleComponent.styleClass().getValue().contains(selector.getValue()))
                    return false;
                break;
            case ID:
                if (!selector.getValue().equals(styleComponent.element().id()))
                    return false;
                break;
            case PSEUDOCLASS:
                if (!styleComponent.activePseudoClass().getValue().contains(selector.getValue()))
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
