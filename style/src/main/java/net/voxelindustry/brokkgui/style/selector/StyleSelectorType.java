package net.voxelindustry.brokkgui.style.selector;

public enum StyleSelectorType
{
    WILDCARD(0), TYPE(1), CLASS(1_000), ID(1_000_000), PSEUDOCLASS(1_000), STRUCTURAL_PSEUDOCLASS(1_000);

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
