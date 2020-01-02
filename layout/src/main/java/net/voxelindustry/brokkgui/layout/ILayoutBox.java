package net.voxelindustry.brokkgui.layout;

public interface ILayoutBox
{
    // Constraints
    float minWidth();

    float minHeight();

    float prefWidth();

    float prefHeight();

    float maxWidth();

    float maxHeight();

    default SizeUnit minWidthUnit()
    {
        return this.widthUnit();
    }

    default SizeUnit minHeightUnit()
    {
        return this.heightUnit();
    }

    default SizeUnit prefWidthUnit()
    {
        return this.widthUnit();
    }

    default SizeUnit prefHeightUnit()
    {
        return this.heightUnit();
    }

    default SizeUnit maxWidthUnit()
    {
        return this.widthUnit();
    }

    default SizeUnit maxHeightUnit()
    {
        return this.heightUnit();
    }

    /**
     * @return the unit used for width values.
     * Default to {@link SizeUnit#PIXEL}
     * <p>
     * Can be used to make the width of an element a fixed part of his parent height.
     * Also used to make element width fit their text with font-relative sizes.
     */
    // TODO : Implement in layout calculation in LayoutGroup
    default SizeUnit widthUnit()
    {
        return SizeUnit.PIXEL;
    }

    /**
     * @return the unit used for height values.
     * Default to {@link SizeUnit#PIXEL}
     * <p>
     * Can be used to make the height of an element a fixed part of his parent height.
     * Also used to make element height fit their text with font-relative sizes.
     */
    // TODO : Implement in layout calculation in LayoutGroup
    default SizeUnit heightUnit()
    {
        return SizeUnit.PIXEL;
    }

    // Layout lifecycle
    boolean isLayoutDirty();

    // Propagating produced layout
    void layoutWidth(float layoutWidth);

    void layoutHeight(float layoutHeight);

    void layoutPosX(float layoutPosX);

    void layoutPosY(float layoutPosY);
}
