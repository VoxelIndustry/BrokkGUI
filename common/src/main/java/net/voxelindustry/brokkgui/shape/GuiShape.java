package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.style.StyleSource;

public abstract class GuiShape extends GuiNode
{
    private ShapeDefinition shape;

    public GuiShape(String type, ShapeDefinition shape)
    {
        super(type);

        this.shape = shape;

        this.getStyle().registerProperty("background-color", Color.ALPHA, Color.class);
        this.getStyle().registerProperty("foreground-color", Color.ALPHA, Color.class);

        this.getStyle().registerProperty("background-texture", Texture.EMPTY, Texture.class);
        this.getStyle().registerProperty("foreground-texture", Texture.EMPTY, Texture.class);

        this.getStyle().registerProperty("border-width", 0f, Float.class);
        this.getStyle().registerProperty("border-color", Color.BLACK, Color.class);
    }

    @Override
    protected void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (pass == RenderPass.BACKGROUND)
        {
            if (this.getBackgroundTexture() != Texture.EMPTY)
            {
                Texture background = this.getBackgroundTexture();

                renderer.getHelper().bindTexture(background);
                this.shape.drawTextured(this, renderer,
                        getxPos() + getxTranslate(), getyPos() + getyTranslate(),
                        background, getzLevel());
            }
            if (this.getBackgroundColor().getAlpha() != 0)
            {
                Color background = this.getBackgroundColor();

                this.shape.drawColored(this, renderer,
                        getxPos() + getxTranslate(), getyPos() + getyTranslate(),
                        background, getzLevel());
            }
        }
        if (pass == RenderPass.FOREGROUND)
        {
            // Draw border
            if (this.getBorderThin() > 0 && this.getBorderColor() != Color.ALPHA)
                this.shape.drawColoredEmpty(this, renderer,
                        getxPos() + getxTranslate(), getyPos() + getyTranslate(),
                        getBorderThin(), getBorderColor(), getzLevel());

            if (this.getForegroundTexture() != Texture.EMPTY)
            {
                Texture foreground = this.getForegroundTexture();

                renderer.getHelper().bindTexture(foreground);
                this.shape.drawTextured(this, renderer,
                        getxPos() + getxTranslate(), getyPos() + getyTranslate(),
                        foreground, getzLevel());
            }
            if (this.getForegroundColor().getAlpha() != 0)
            {
                Color foreground = this.getForegroundColor();

                this.shape.drawColored(this, renderer,
                        getxPos() + getxTranslate(), getyPos() + getyTranslate(),
                        foreground, getzLevel());
            }
        }
    }

    @Override
    public boolean isPointInside(int mouseX, int mouseY)
    {
        return shape.isMouseInside(this, mouseX, mouseY);
    }

    public Texture getBackgroundTexture()
    {
        return this.getStyle().getStyleProperty("background-texture", Texture.class).getValue();
    }

    public void setBackgroundTexture(Texture texture)
    {
        this.getStyle().getStyleProperty("background-texture", Texture.class)
                .setStyle(StyleSource.CODE, 10_000, texture);
    }

    public Color getBackgroundColor()
    {
        return this.getStyle().getStyleProperty("background-color", Color.class).getValue();
    }

    public void setBackgroundColor(Color color)
    {
        this.getStyle().getStyleProperty("background-color", Color.class)
                .setStyle(StyleSource.CODE, 10_000, color);
    }

    public Texture getForegroundTexture()
    {
        return this.getStyle().getStyleProperty("foreground-texture", Texture.class).getValue();
    }

    public void setForegroundTexture(Texture texture)
    {
        this.getStyle().getStyleProperty("foreground-texture", Texture.class)
                .setStyle(StyleSource.CODE, 10_000, texture);
    }

    public Color getForegroundColor()
    {
        return this.getStyle().getStyleProperty("foreground-color", Color.class).getValue();
    }

    public void setForegroundColor(Color color)
    {
        this.getStyle().getStyleProperty("foreground-color", Color.class)
                .setStyle(StyleSource.CODE, 10_000, color);
    }

    public float getBorderThin()
    {
        return this.getStyle().getStyleProperty("border-width", Float.class).getValue();
    }

    public void setBorderThin(float borderThin)
    {
        this.getStyle().getStyleProperty("border-width", Float.class).setStyle(StyleSource.CODE, 10_000, borderThin);
    }

    public Color getBorderColor()
    {
        return this.getStyle().getStyleProperty("border-color", Color.class).getValue();
    }

    public void setBorderColor(Color color)
    {
        this.getStyle().getStyleProperty("border-color", Color.class).setStyle(StyleSource.CODE, 10_000, color);
    }

    public void setShape(ShapeDefinition shape)
    {
        this.shape = shape;
    }

    public ShapeDefinition getShape()
    {
        return this.shape;
    }
}