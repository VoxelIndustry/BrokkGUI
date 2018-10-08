package org.yggard.brokkgui.shape;

import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.Texture;

public abstract class GuiShape extends GuiNode
{
    public GuiShape(String type)
    {
        super(type);

        this.getStyle().registerProperty("line-weight", 0, Integer.class);
        this.getStyle().registerProperty("line-color", Color.WHITE, Color.class);
        this.getStyle().registerProperty("color", Color.ALPHA, Color.class);
        this.getStyle().registerProperty("texture", Texture.EMPTY, Texture.class);
    }

    public int getLineWeight()
    {
        return this.getStyle().getStyleProperty("line-weight", Integer.class).getValue();
    }

    public Color getLineColor()
    {
        return this.getStyle().getStyleProperty("line-color", Color.class).getValue();
    }

    public Color getColor()
    {
        return this.getStyle().getStyleProperty("color", Color.class).getValue();
    }

    public Texture getTexture()
    {
        return this.getStyle().getStyleProperty("texture", Texture.class).getValue();
    }
}