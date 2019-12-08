package net.voxelindustry.brokkgui.layout;

public interface ILayoutBox {
    // Constraints
    float minWidth();

    float minHeight();

    float prefWidth();

    float prefHeight();

    float maxWidth();

    float maxHeight();

    // Layout lifecycle
    boolean isLayoutDirty();

    // Propagating produced layout
    void layoutWidth(float layoutWidth);

    void layoutHeight(float layoutHeight);

    void layoutPosX(float layoutPosX);

    void layoutPosY(float layoutPosY);
}
