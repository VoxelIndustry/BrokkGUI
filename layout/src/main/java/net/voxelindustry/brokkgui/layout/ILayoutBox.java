package net.voxelindustry.brokkgui.layout;

public interface ILayoutBox {
    float minWidth();

    float minHeight();

    float prefWidth();

    float prefHeight();

    float maxWidth();

    float maxHeight();

    void layoutWidth(float width);

    void layoutHeight(float height);

    float layoutWidth();

    float layoutHeight();

    float effectiveWidth();

    float effectiveHeight();
}
