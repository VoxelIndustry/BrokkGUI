package net.voxelindustry.brokkgui.layout;

public interface ILayoutBox {
    float minWidth();

    float minHeight();

    float prefWidth();

    float prefHeight();

    float maxWidth();

    float maxHeight();

    boolean isLayoutDirty();
}
