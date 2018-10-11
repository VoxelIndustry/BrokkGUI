package net.voxelindustry.brokkgui.style.tree;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.style.ICascadeStyleable;
import net.voxelindustry.brokkgui.style.StyleHolder;

public class StyleSelectorHierarchic implements IStyleSelector
{
    private final IStyleSelector parentSelector;
    private final IStyleSelector childSelector;
    private final boolean        directChild;

    public StyleSelectorHierarchic(IStyleSelector parentSelector, IStyleSelector childSelector, boolean directChild)
    {
        this.parentSelector = parentSelector;
        this.childSelector = childSelector;
        this.directChild = directChild;
    }

    public int getSpecificity()
    {
        return childSelector.getSpecificity() + parentSelector.getSpecificity();
    }

    @Override
    public IStyleSelector add(StyleSelectorType type, String selector)
    {
        return this.addChild(type, selector);
    }

    public IStyleSelector addChild(StyleSelectorType type, String selector)
    {
        this.childSelector.add(type, selector);
        return this;
    }

    public IStyleSelector addParent(StyleSelectorType type, String selector)
    {
        this.parentSelector.add(type, selector);
        return this;
    }

    public boolean match(StyleHolder styleHolder)
    {
        if (!styleHolder.getParent().isPresent())
            return false;

        if (this.isDirectChild())
        {
            if (!this.parentSelector.match(styleHolder.getParent().getValue().getStyle()))
                return false;
        }
        else
        {
            BaseProperty<ICascadeStyleable> current = styleHolder.getParent();

            boolean matched = false;
            while (current.isPresent())
            {
                if (this.parentSelector.match(current.getValue().getStyle()))
                {
                    matched = true;
                    break;
                }
                current = current.getValue().getStyle().getParent();
            }
            if (!matched)
                return false;
        }
        return this.childSelector.match(styleHolder);
    }

    @Override
    public boolean match(IStyleSelector selector)
    {
        if (selector == this)
            return true;
        if (!(selector instanceof StyleSelectorHierarchic))
            return false;
        StyleSelectorHierarchic other = (StyleSelectorHierarchic) selector;
        return this.isDirectChild() == other.isDirectChild() && this.childSelector.match(other.childSelector) &&
                this.parentSelector.match(other.parentSelector);
    }

    public boolean isDirectChild()
    {
        return directChild;
    }

    public IStyleSelector getParentSelector()
    {
        return parentSelector;
    }

    public IStyleSelector getChildSelector()
    {
        return childSelector;
    }

    @Override
    public String toString()
    {
        return "StyleSelectorHierarchic{" + "parentSelector=" + parentSelector + ", childSelector=" + childSelector +
                ", directChild=" + directChild + "}";
    }
}
