package net.voxelindustry.brokkgui.style.tree;

import net.voxelindustry.brokkgui.exp.component.GuiElement;
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
        if (styleHolder.getParent() == null)
            return false;

        if (this.isDirectChild())
        {
            if (!styleHolder.getParent().has(StyleHolder.class) || !this.parentSelector.match(styleHolder.getParent().get(StyleHolder.class)))
                return false;
        }
        else
        {
            GuiElement current = styleHolder.getParent();

            boolean matched = false;
            while (current != null)
            {
                if (styleHolder.getParent().has(StyleHolder.class) && this.parentSelector.match(current.get(StyleHolder.class)))
                {
                    matched = true;
                    break;
                }
                current = current.transform().parent() != null ? current.transform().parent().element() : null;
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
