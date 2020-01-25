package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.border.ColorBorderDrawer;
import net.voxelindustry.brokkgui.border.ImageBorderDrawer;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.sprite.SpriteAnimation;
import net.voxelindustry.brokkgui.sprite.SpriteBackgroundDrawer;
import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.style.HeldPropertyState;
import net.voxelindustry.brokkgui.style.optional.BorderImageProperties;
import net.voxelindustry.brokkgui.style.optional.BorderProperties;

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
        this.getStyle().registerProperty("background-repeat", SpriteRepeat.NONE, SpriteRepeat.class);
        this.getStyle().registerProperty("background-animation", SpriteAnimation.EMPTY, SpriteAnimation.class);
        this.getStyle().registerProperty("background-position", RectBox.EMPTY, RectBox.class);

        this.getStyle().registerProperty("foreground-texture", Texture.EMPTY, Texture.class);
        this.getStyle().registerProperty("foreground-repeat", SpriteRepeat.NONE, SpriteRepeat.class);
        this.getStyle().registerProperty("foreground-animation", SpriteAnimation.EMPTY, SpriteAnimation.class);
        this.getStyle().registerProperty("foreground-position", RectBox.EMPTY, RectBox.class);

        this.getStyle().registerConditionalProperties("border*", BorderProperties.getInstance());
        this.getStyle().registerConditionalProperties("border-image*", BorderImageProperties.getInstance());
    }

    @Override
    protected void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (pass == RenderPass.BACKGROUND)
        {
            if (this.getBackgroundTexture() != Texture.EMPTY)
            {
                SpriteBackgroundDrawer.drawBackground(this, renderer);
            }
            if (this.getBackgroundColor().getAlpha() != 0)
            {
                Color background = this.getBackgroundColor();

                this.shape.drawColored(this, renderer,
                        getxPos() + getxTranslate(), getyPos() + getyTranslate(),
                        background, getzLevel());
            }

            if (this.hasBorder())
            {
                if (this.hasBorderImage())
                    ImageBorderDrawer.drawBorder(this, renderer);
                else
                    ColorBorderDrawer.drawBorder(this, renderer);
            }
        }
        if (pass == RenderPass.FOREGROUND)
        {
            if (this.getForegroundTexture() != Texture.EMPTY)
            {
                SpriteBackgroundDrawer.drawForeground(this, renderer);
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
        return this.getStyle().getStyleValue("background-texture", Texture.class, Texture.EMPTY);
    }

    public void setBackgroundTexture(Texture texture)
    {
        this.getStyle().setPropertyDirect("background-texture", texture, Texture.class);
    }

    public Color getBackgroundColor()
    {
        return this.getStyle().getStyleValue("background-color", Color.class, Color.ALPHA);
    }

    public void setBackgroundColor(Color color)
    {
        this.getStyle().setPropertyDirect("background-color", color, Color.class);
    }

    public SpriteRepeat getBackgroundRepeat()
    {
        return this.getStyle().getStyleValue("background-repeat", SpriteRepeat.class, SpriteRepeat.NONE);
    }

    public void setBackgroundRepeat(SpriteRepeat spriteRepeat)
    {
        this.getStyle().setPropertyDirect("background-repeat", spriteRepeat, SpriteRepeat.class);
    }

    public SpriteAnimation getBackgroundAnimation()
    {
        return this.getStyle().getStyleValue("background-animation", SpriteAnimation.class, SpriteAnimation.EMPTY);
    }

    public void setBackgroundAnimation(SpriteAnimation spriteAnimation)
    {
        this.getStyle().setPropertyDirect("background-animation", spriteAnimation, SpriteAnimation.class);
    }

    public RectBox getBackgroundPosition()
    {
        return this.getStyle().getStyleValue("background-position", RectBox.class, RectBox.EMPTY);
    }

    public void setBackgroundPosition(RectBox position)
    {
        this.getStyle().setPropertyDirect("background-position", position, RectBox.class);
    }

    public Texture getForegroundTexture()
    {
        return this.getStyle().getStyleValue("foreground-texture", Texture.class, Texture.EMPTY);
    }

    public void setForegroundTexture(Texture texture)
    {
        this.getStyle().setPropertyDirect("foreground-texture", texture, Texture.class);
    }

    public Color getForegroundColor()
    {
        return this.getStyle().getStyleValue("foreground-color", Color.class, Color.ALPHA);
    }

    public void setForegroundColor(Color color)
    {
        this.getStyle().setPropertyDirect("foreground-color", color, Color.class);
    }

    public SpriteRepeat getForegroundRepeat()
    {
        return this.getStyle().getStyleValue("foreground-repeat", SpriteRepeat.class, SpriteRepeat.NONE);
    }

    public void setForegroundRepeat(SpriteRepeat spriteRepeat)
    {
        this.getStyle().setPropertyDirect("foreground-repeat", spriteRepeat, SpriteRepeat.class);
    }

    public SpriteAnimation getForegroundAnimation()
    {
        return this.getStyle().getStyleValue("foreground-animation", SpriteAnimation.class, SpriteAnimation.EMPTY);
    }

    public void setForegroundAnimation(SpriteAnimation spriteAnimation)
    {
        this.getStyle().setPropertyDirect("foreground-animation", spriteAnimation, SpriteAnimation.class);
    }

    public RectBox getForegroundPosition()
    {
        return this.getStyle().getStyleValue("foreground-position", RectBox.class, RectBox.EMPTY);
    }

    public void setForegroundPosition(RectBox position)
    {
        this.getStyle().setPropertyDirect("foreground-position", position, RectBox.class);
    }

    public boolean hasBorder()
    {
        return this.getStyle().doesHoldProperty("border-width") == HeldPropertyState.PRESENT;
    }

    public boolean hasBorderImage()
    {
        return this.getStyle().doesHoldProperty("border-image-source") == HeldPropertyState.PRESENT;
    }

    public float getBorderWidth()
    {
        return this.getStyle().getStyleValue("border-width", Float.class, 0f);
    }

    public void setBorderWidth(float borderWidth)
    {
        this.getStyle().setPropertyDirect("border-width", borderWidth, Float.class);
    }

    public float getBorderWidth(RectSide side)
    {
        return this.getStyle().getStyleValue("border-" + side.getCssString() + "-width", Float.class, 0f);
    }

    public void setBorderWidth(float borderWidth, RectSide side)
    {
        this.getStyle().setPropertyDirect("border-" + side.getCssString() + "-width", borderWidth, Float.class);
    }

    public Integer getBorderRadius()
    {
        return this.getStyle().getStyleValue("border-radius", Integer.class, 0);
    }

    public void setBorderRadius(int radius)
    {
        this.getStyle().setPropertyDirect("border-radius", radius, Integer.class);
    }

    public Integer getBorderRadius(RectCorner corner)
    {
        return this.getStyle().getStyleValue("border-" + corner.getCssString() + "-radius", Integer.class, 0);
    }

    public void setBorderRadius(int radius, RectCorner corner)
    {
        this.getStyle().setPropertyDirect("border-" + corner.getCssString() + "-radius", radius, Integer.class);
    }

    public Color getBorderColor()
    {
        return this.getStyle().getStyleValue("border-color", Color.class, Color.ALPHA);
    }

    public void setBorderColor(Color color)
    {
        this.getStyle().setPropertyDirect("border-color", color, Color.class);
    }

    public Texture getBorderImage()
    {
        return this.getStyle().getStyleValue("border-image-source", Texture.class, Texture.EMPTY);
    }

    public void setBorderImage(Texture texture)
    {
        this.getStyle().setPropertyDirect("border-image-source", texture, Texture.class);
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
