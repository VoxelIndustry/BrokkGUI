package fr.ourten.brokkgui.paint;

import fr.ourten.brokkgui.internal.IGuiRenderer;

public class Background
{
    private Color   color;
    private Texture texture;

    public Background(final Color color, final Texture texture)
    {
        super();
        this.color = color;
        this.texture = texture;
    }

    public Background(final Color color)
    {
        this(color, new Texture());
    }

    public Background()
    {
        this(Color.ALPHA);
    }

    public void renderBackground(final IGuiRenderer renderer, final float x, final float y, final float width,
            final float height, final float zLevel)
    {
        if (!this.texture.isEmpty())
        {
            renderer.getHelper().bindTexture(this.texture);
            renderer.getHelper().drawTexturedModalRect(renderer, x, y, this.texture.getU(), this.texture.getV(),
                    this.texture.getS(), this.texture.getT(), width, height, zLevel);
        }
        if (this.color.getAlpha() != 0)
            renderer.getHelper().drawColoredRect(renderer, x, y, width, height, zLevel, this.color);
    }

    public Color getColor()
    {
        return this.color;
    }

    public void setColor(final Color color)
    {
        this.color = color;
    }

    public Texture getTexture()
    {
        return this.texture;
    }

    public void setTexture(final Texture texture)
    {
        this.texture = texture;
    }
}