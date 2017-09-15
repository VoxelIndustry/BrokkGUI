package org.yggard.brokkgui.style.tree;

public enum StyleSelectorType
{
    WILDCARD(0), TYPE(1), CLASS(10), ID(100), PSEUDOCLASS(10);

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
