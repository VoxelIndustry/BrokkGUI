package net.voxelindustry.brokkgui.layout;

import net.voxelindustry.brokkgui.data.RectAlignment;

public interface LayoutComponent
{
    RectAlignment alignment();

    float minWidth();

    float preferredWidth();

    float maxWidth();

    float minHeight();

    float preferredHeight();

    float maxHeight();

    void minWidth(float width);

    void preferredWidth(float width);

    void maxWidth(float width);

    void minHeight(float height);

    void preferredHeight(float height);

    void maxHeight(float height);

    float xPos();

    float yPos();

    // Transform delegates

    void width(float width);

    void height(float height);

    void xPos(float xPos);

    void yPos(float yPos);
}
