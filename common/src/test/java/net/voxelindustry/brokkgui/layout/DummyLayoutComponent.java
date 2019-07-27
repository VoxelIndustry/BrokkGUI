package net.voxelindustry.brokkgui.layout;

import net.voxelindustry.brokkgui.data.RectAlignment;

class DummyLayoutComponent implements LayoutComponent
{
    private float minWidth, preferredWidth, maxWidth;
    private float minHeight, preferredHeight, maxHeight;
    private float xPos, yPos;
    private float width, height;

    private RectAlignment alignment;

    public DummyLayoutComponent(float minWidth, float preferredWidth, float maxWidth, float minHeight, float preferredHeight, float maxHeight, float xPos, float yPos, float width, float height, RectAlignment alignment)
    {
        this.minWidth = minWidth;
        this.preferredWidth = preferredWidth;
        this.maxWidth = maxWidth;
        this.minHeight = minHeight;
        this.preferredHeight = preferredHeight;
        this.maxHeight = maxHeight;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;

        this.alignment = alignment;
    }

    @Override
    public float minWidth()
    {
        return minWidth;
    }

    @Override
    public void minWidth(float minWidth)
    {
        this.minWidth = minWidth;
    }

    @Override
    public float preferredWidth()
    {
        return preferredWidth;
    }

    @Override
    public void preferredWidth(float preferredWidth)
    {
        this.preferredWidth = preferredWidth;
    }

    @Override
    public float maxWidth()
    {
        return maxWidth;
    }

    @Override
    public void maxWidth(float maxWidth)
    {
        this.maxWidth = maxWidth;
    }

    @Override
    public float minHeight()
    {
        return minHeight;
    }

    @Override
    public void minHeight(float minHeight)
    {
        this.minHeight = minHeight;
    }

    @Override
    public float preferredHeight()
    {
        return preferredHeight;
    }

    @Override
    public void preferredHeight(float preferredHeight)
    {
        this.preferredHeight = preferredHeight;
    }

    @Override
    public float maxHeight()
    {
        return maxHeight;
    }

    @Override
    public void maxHeight(float maxHeight)
    {
        this.maxHeight = maxHeight;
    }

    @Override
    public float xPos()
    {
        return xPos;
    }

    @Override
    public void xPos(float xPos)
    {
        this.xPos = xPos;
    }

    @Override
    public float yPos()
    {
        return yPos;
    }

    @Override
    public void yPos(float yPos)
    {
        this.yPos = yPos;
    }

    public float width()
    {
        return width;
    }

    @Override
    public void width(float width)
    {
        this.width = width;
    }

    public float height()
    {
        return height;
    }

    @Override
    public void height(float height)
    {
        this.height = height;
    }

    @Override
    public RectAlignment alignment()
    {
        return this.alignment;
    }

    public void alignment(RectAlignment alignment)
    {
        this.alignment = alignment;
    }
}
