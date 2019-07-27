package net.voxelindustry.brokkgui.layout;

public interface LayoutDelegate
{
    float desiredWidth();

    float desiredHeight();

    void width(float width);

    void height(float height);
}
