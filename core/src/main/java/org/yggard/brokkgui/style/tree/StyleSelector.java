package org.yggard.brokkgui.style.tree;

import org.yggard.brokkgui.style.StyleHolder;

public class StyleSelector
{
    private StyleSelectorType type;
    private String            selector;
    private int computedSpecificity  = -1;
    private int inheritedSpecificity = 0;

    public StyleSelector addWildcard()
    {
        this.type = StyleSelectorType.WILDCARD;
        this.selector = "*";
        this.computedSpecificity = 1000;
        return this;
    }

    public StyleSelector setSelector(StyleSelectorType type, String selector)
    {
        this.type = type;
        this.selector = selector;
        this.computedSpecificity = -1;
        return this;
    }

    public int getSpecificity()
    {
        if (this.computedSpecificity == -1)
        {
            this.computedSpecificity = this.inheritedSpecificity;
            this.computedSpecificity += this.type.getSpecificity();
        }
        return this.computedSpecificity;
    }

    public String getSelector()
    {
        return selector;
    }

    public StyleSelectorType getType()
    {
        return type;
    }

    public void setInheritedSpecificity(int inheritedSpecificity)
    {
        this.inheritedSpecificity = inheritedSpecificity;
    }

    public boolean match(StyleHolder styleHolder)
    {
        switch (type)
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
        return true;
    }

    @Override
    public String toString()
    {
        return "{" + type + "=" + selector + ", specificity=" + this.getSpecificity() + '}';
    }
}
