package org.yggard.brokkgui.style.tree;

import fr.ourten.teabeans.value.BaseProperty;
import org.apache.commons.lang3.tuple.Pair;
import org.yggard.brokkgui.style.ICascadeStyleable;
import org.yggard.brokkgui.style.StyleHolder;

import java.util.ArrayList;
import java.util.List;

public class StyleSelectorHierarchic extends StyleSelector
{
    private final List<Pair<StyleSelectorType, String>> parentSelectors;
    private final boolean                               directChild;

    public StyleSelectorHierarchic(boolean directChild)
    {
        this.directChild = directChild;
        this.parentSelectors = new ArrayList<>();
    }

    public StyleSelector addParent(StyleSelectorType type, String selector)
    {
        this.parentSelectors.add(Pair.of(type, selector));
        this.computedSpecificity = -1;
        return this;
    }

    public int getSpecificity()
    {
        if (this.computedSpecificity == -1)
        {
            this.computedSpecificity = super.getSpecificity();
            for (Pair<StyleSelectorType, String> selector : this.parentSelectors)
                this.computedSpecificity += selector.getKey().getSpecificity();
        }
        return this.computedSpecificity;
    }

    public boolean match(StyleHolder styleHolder)
    {
        if (!styleHolder.getParent().isPresent())
            return false;

        if (this.isDirectChild())
        {
            for (Pair<StyleSelectorType, String> selector : this.parentSelectors)
            {
                if (selector.getKey() == StyleSelectorType.WILDCARD)
                    return true;
                if (!this.checkSelector(selector, styleHolder.getParent().getValue().getStyle()))
                    return false;
            }
        }
        else
        {
            BaseProperty<ICascadeStyleable> current = styleHolder.getParent();

            boolean matched = false;
            while (current.isPresent())
            {
                boolean valid = true;
                for (Pair<StyleSelectorType, String> selector : this.parentSelectors)
                {
                    if (selector.getKey() == StyleSelectorType.WILDCARD)
                    {
                        valid = true;
                        break;
                    }
                    if (!this.checkSelector(selector, current.getValue().getStyle()))
                        valid = false;
                }
                if (valid)
                {
                    matched = true;
                    break;
                }
                current = current.getValue().getStyle().getParent();
            }
            if (!matched)
                return false;
        }
        return super.match(styleHolder);
    }

    @Override
    public boolean match(IStyleSelector selector)
    {
        if (selector == this)
            return true;
        if (!(selector instanceof StyleSelectorHierarchic))
            return false;
        StyleSelectorHierarchic other = (StyleSelectorHierarchic) selector;
        return this.isDirectChild() == other.isDirectChild() &&
                this.getSelectors().size() == other.getSelectors().size() &&
                this.getSelectors().containsAll(other.getSelectors()) &&
                this.getParentSelectors().size() == other.getParentSelectors().size() &&
                this.getParentSelectors().containsAll(other.getParentSelectors());
    }

    public boolean isDirectChild()
    {
        return directChild;
    }

    public List<Pair<StyleSelectorType, String>> getParentSelectors()
    {
        return parentSelectors;
    }

    @Override
    public String toString()
    {
        return "StyleSelectorHierarchic{" +
                "parentSelectors=" + parentSelectors +
                ", directChild=" + directChild +
                "} " + super.toString();
    }
}
