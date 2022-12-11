package net.voxelindustry.brokkgui.style.specificity;

public record StylePriority(StyleSource source, int specificity) implements Comparable<StylePriority>
{
    public static final StylePriority CODE = new StylePriority(StyleSource.CODE, 10_000);

    @Override
    public int compareTo(StylePriority priority)
    {
        return this.compareTo(priority.source, priority.specificity);
    }

    public int compareTo(StyleSource source, int specificity)
    {
        if (source == this.source && specificity == this.specificity)
            return 0;

        var sourceComparison = this.source.compareTo(source);
        if (sourceComparison < 0)
            return -1;
        if (sourceComparison > 0)
            return 1;

        return Integer.compare(this.specificity, specificity);
    }
}
