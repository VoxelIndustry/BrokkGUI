package net.voxelindustry.brokkgui.layout;

import net.voxelindustry.brokkgui.data.RectAlignment;

public class DummyLayoutComponentBuilder
{
    private float minWidth;
    private float preferredWidth;
    private float maxWidth;
    private float minHeight;
    private float preferredHeight;
    private float maxHeight;
    private float xPos;
    private float yPos;
    private float width;
    private float height;

    private RectAlignment alignment;

    public DummyLayoutComponentBuilder setUnshrinkableTo(float width, float height)
    {
        this.setMinSize(width, height);
        this.setPrefSize(width, height);
        this.setMaxSize(width, height);
        return this;
    }

    public DummyLayoutComponentBuilder setMinSize(float minWidth, float minHeight)
    {
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        return this;
    }

    public DummyLayoutComponentBuilder setPrefSize(float preferredWidth, float preferredHeight)
    {
        this.preferredWidth = preferredWidth;
        this.preferredHeight = preferredHeight;
        return this;
    }

    public DummyLayoutComponentBuilder setMaxSize(float maxWidth, float maxHeight)
    {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        return this;
    }

    public DummyLayoutComponentBuilder setPos(float x, float y)
    {
        this.xPos = x;
        this.yPos = y;
        return this;
    }

    public DummyLayoutComponentBuilder setSize(float width, float height)
    {
        this.width = width;
        this.height = height;
        return this;
    }

    public DummyLayoutComponentBuilder setMinWidth(float minWidth)
    {
        this.minWidth = minWidth;
        return this;
    }

    public DummyLayoutComponentBuilder setPreferredWidth(float preferredWidth)
    {
        this.preferredWidth = preferredWidth;
        return this;
    }

    public DummyLayoutComponentBuilder setMaxWidth(float maxWidth)
    {
        this.maxWidth = maxWidth;
        return this;
    }

    public DummyLayoutComponentBuilder setMinHeight(float minHeight)
    {
        this.minHeight = minHeight;
        return this;
    }

    public DummyLayoutComponentBuilder setPreferredHeight(float preferredHeight)
    {
        this.preferredHeight = preferredHeight;
        return this;
    }

    public DummyLayoutComponentBuilder setMaxHeight(float maxHeight)
    {
        this.maxHeight = maxHeight;
        return this;
    }

    public DummyLayoutComponentBuilder setxPos(float xPos)
    {
        this.xPos = xPos;
        return this;
    }

    public DummyLayoutComponentBuilder setyPos(float yPos)
    {
        this.yPos = yPos;
        return this;
    }

    public DummyLayoutComponentBuilder setWidth(float width)
    {
        this.width = width;
        return this;
    }

    public DummyLayoutComponentBuilder setHeight(float height)
    {
        this.height = height;
        return this;
    }

    public DummyLayoutComponentBuilder setAlignment(RectAlignment alignment)
    {
        this.alignment = alignment;
        return this;
    }

    public DummyLayoutComponent createDummyLayoutComponent()
    {
        return new DummyLayoutComponent(minWidth, preferredWidth, maxWidth, minHeight, preferredHeight, maxHeight, xPos, yPos, width, height, alignment);
    }
}