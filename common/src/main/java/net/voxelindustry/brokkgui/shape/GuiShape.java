package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.style.StyleProperty;
import net.voxelindustry.brokkgui.style.StyleSource;
import net.voxelindustry.brokkgui.style.shorthand.ShorthandArgMappers;
import net.voxelindustry.brokkgui.style.shorthand.ShorthandProperty;

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

        StyleProperty<Color> borderColorProperty = this.getStyle().registerProperty("border-color", Color.BLACK,
                Color.class);

        ShorthandProperty<Float> borderWidthProperty = this.getStyle().registerShorthand("border-width", 0f,
                Float.class, ShorthandArgMappers.BOX_MAPPER,
                "border-top-width", "border-right-width", "border-bottom-width", "border-left-width");


        this.getStyle().registerShorthand("border-radius", 0,
                Integer.class, ShorthandArgMappers.BOX_MAPPER,
                "border-top-left-radius", "border-top-right-radius",
                "border-bottom-right-radius", "border-bottom-left-radius");

        this.getStyle().registerGenericShorthand("border", "", borderColorProperty, borderWidthProperty);
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

            // Draw border
            if (this.getBorderColor() != Color.ALPHA)
                this.shape.drawBorder(this, renderer);
        }
        if (pass == RenderPass.FOREGROUND)
        {
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

    public float getBorderWidth()
    {
        return this.getStyle().getStyleProperty("border-width", Float.class).getValue();
    }

    public void setBorderWidth(float borderWidth)
    {
        this.getStyle().getStyleProperty("border-width", Float.class)
                .setStyle(StyleSource.CODE, 10_000, borderWidth);
    }

    public float getBorderWidth(RectSide side)
    {
        return this.getStyle().getStyleProperty("border-" + side.getCssString() + "-width", Float.class).getValue();
    }

    public void setBorderWidth(float borderWidth, RectSide side)
    {
        this.getStyle().getStyleProperty("border-" + side.getCssString() + "-width", Float.class)
                .setStyle(StyleSource.CODE, 10_000, borderWidth);
    }

    public Integer getBorderRadius()
    {
        return this.getStyle().getStyleProperty("border-radius", Integer.class).getValue();
    }

    public void setBorderRadius(int radius)
    {
        this.getStyle().getStyleProperty("border-radius", Integer.class)
                .setStyle(StyleSource.CODE, 10_000, radius);
    }

    public Integer getBorderRadius(RectCorner corner)
    {
        return this.getStyle().getStyleProperty("border-" + corner.getCssString() + "-radius", Integer.class).getValue();
    }

    public void setBorderRadius(int radius, RectCorner corner)
    {
        this.getStyle().getStyleProperty("border-" + corner.getCssString() + "-radius", Integer.class)
                .setStyle(StyleSource.CODE, 10_000, radius);
    }

    public Color getBorderColor()
    {
        return this.getStyle().getStyleProperty("border-color", Color.class).getValue();
    }

    public void setBorderColor(Color color)
    {
        this.getStyle().getStyleProperty("border-color", Color.class)
                .setStyle(StyleSource.CODE, 10_000, color);
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
