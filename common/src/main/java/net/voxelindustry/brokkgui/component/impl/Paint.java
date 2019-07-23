package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.border.ColorBorderDrawer;
import net.voxelindustry.brokkgui.border.ImageBorderDrawer;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.RenderComponent;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.shape.ShapeDefinition;

public class Paint extends GuiComponent implements RenderComponent
{
    private ShapeDefinition shape;

    protected BaseProperty<Texture> backgroundTextureProperty;
    protected BaseProperty<Color>   backgroundColorProperty;

    protected BaseProperty<Texture> foregroundTextureProperty;
    protected BaseProperty<Color>   foregroundColorProperty;

    protected BaseProperty<Color> borderColorProperty;

    protected BaseProperty<Float> borderWidthLeftProperty;
    protected BaseProperty<Float> borderWidthRightProperty;
    protected BaseProperty<Float> borderWidthTopProperty;
    protected BaseProperty<Float> borderWidthBottomProperty;

    protected BaseProperty<Integer> borderRadiusTopLeftProperty;
    protected BaseProperty<Integer> borderRadiusTopRightProperty;
    protected BaseProperty<Integer> borderRadiusBottomLeftProperty;
    protected BaseProperty<Integer> borderRadiusBottomRightProperty;

    protected BaseProperty<Texture> borderImageProperty;
    protected BaseProperty<RectBox> borderImageSliceProperty;
    protected BaseProperty<RectBox> borderImageWidthProperty;
    protected BaseProperty<RectBox> borderImageOutsetProperty;

    protected BaseProperty<Boolean> borderImageFillProperty;

    public Paint()
    {
    }

    public ShapeDefinition shape()
    {
        return shape;
    }

    public void shape(ShapeDefinition shape)
    {
        this.shape = shape;

        if (this.hasElement())
            this.element().transform().mouseInBoundsChecker(shape::isMouseInside);
    }

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        element.transform().mouseInBoundsChecker(shape::isMouseInside);
    }

    @Override
    public void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        float x = element().transform().leftPos();
        float y = element().transform().topPos();
        float z = element().transform().zLevel();

        if (pass == RenderPass.BACKGROUND)
        {
            if (this.hasTextureBackground() && this.backgroundTexture() != Texture.EMPTY)
            {
                Texture background = this.backgroundTexture();

                renderer.getHelper().bindTexture(background);
                this.shape.drawTextured(this.element().transform(), renderer, x, y, background, z);
            }
            if (this.hasColorBackground() && this.backgroundColor().getAlpha() != 0)
            {
                Color background = this.backgroundColor();

                this.shape.drawColored(this.element().transform(), renderer, x, y, background, z);
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
            if (this.hasTextureForeground() && this.foregroundTexture() != Texture.EMPTY)
            {
                Texture foreground = this.foregroundTexture();

                renderer.getHelper().bindTexture(foreground);
                this.shape.drawTextured(this.element().transform(), renderer, x, y, foreground, z);
            }
            if (this.hasColorForeground() && this.foregroundColor().getAlpha() != 0)
            {
                Color foreground = this.foregroundColor();

                this.shape.drawColored(this.element().transform(), renderer, x, y, foreground, z);
            }
        }
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    public boolean hasTextureBackground()
    {
        return this.backgroundTextureProperty != null;
    }

    public boolean hasColorBackground()
    {
        return this.backgroundColorProperty != null;
    }

    public boolean hasTextureForeground()
    {
        return this.foregroundTextureProperty != null;
    }

    public boolean hasColorForeground()
    {
        return this.foregroundColorProperty != null;
    }

    public boolean hasBorder()
    {
        return this.borderColorProperty != null;
    }

    public boolean hasBorderImage()
    {
        return this.borderImageProperty != null;
    }

    public BaseProperty<Texture> backgroundTextureProperty()
    {
        if (this.backgroundTextureProperty == null)
            this.backgroundTextureProperty = new BaseProperty<>(Texture.EMPTY, "backgroundTextureProperty");
        return backgroundTextureProperty;
    }

    public BaseProperty<Color> backgroundColorProperty()
    {
        if (this.backgroundColorProperty == null)
            this.backgroundColorProperty = new BaseProperty<>(Color.ALPHA, "backgroundColorProperty");
        return backgroundColorProperty;
    }

    public BaseProperty<Texture> foregroundTextureProperty()
    {
        if (this.foregroundTextureProperty == null)
            this.foregroundTextureProperty = new BaseProperty<>(Texture.EMPTY, "foregroundTextureProperty");
        return foregroundTextureProperty;
    }

    public BaseProperty<Color> foregroundColorProperty()
    {
        if (this.foregroundColorProperty == null)
            this.foregroundColorProperty = new BaseProperty<>(Color.ALPHA, "foregroundColorProperty");
        return foregroundColorProperty;
    }

    public BaseProperty<Color> borderColorProperty()
    {
        if (this.borderColorProperty == null)
            this.borderColorProperty = new BaseProperty<>(Color.ALPHA, "borderColorProperty");
        return this.borderColorProperty;
    }

    public BaseProperty<Float> borderWidthLeftProperty()
    {
        if (this.borderWidthLeftProperty == null)
            this.borderWidthLeftProperty = new BaseProperty<>(1f, "borderWidthLeftProperty");
        return this.borderWidthLeftProperty;
    }

    public BaseProperty<Float> borderWidthRightProperty()
    {
        if (this.borderWidthRightProperty == null)
            this.borderWidthRightProperty = new BaseProperty<>(1f, "borderWidthRightProperty");
        return this.borderWidthRightProperty;
    }

    public BaseProperty<Float> borderWidthTopProperty()
    {
        if (this.borderWidthTopProperty == null)
            this.borderWidthTopProperty = new BaseProperty<>(1f, "borderWidthTopProperty");
        return this.borderWidthTopProperty;
    }

    public BaseProperty<Float> borderWidthBottomProperty()
    {
        if (this.borderWidthBottomProperty == null)
            this.borderWidthBottomProperty = new BaseProperty<>(1f, "borderWidthBottomProperty");
        return this.borderWidthBottomProperty;
    }

    public BaseProperty<Integer> borderRadiusTopLeftProperty()
    {
        if (this.borderRadiusTopLeftProperty == null)
            this.borderRadiusTopLeftProperty = new BaseProperty<>(0, "borderRadiusTopLeftProperty");
        return this.borderRadiusTopLeftProperty;
    }

    public BaseProperty<Integer> borderRadiusTopRightProperty()
    {
        if (this.borderRadiusTopRightProperty == null)
            this.borderRadiusTopRightProperty = new BaseProperty<>(0, "borderRadiusTopRightProperty");
        return this.borderRadiusTopRightProperty;
    }

    public BaseProperty<Integer> borderRadiusBottomLeftProperty()
    {
        if (this.borderRadiusBottomLeftProperty == null)
            this.borderRadiusBottomLeftProperty = new BaseProperty<>(0, "borderRadiusBottomLeftProperty");
        return this.borderRadiusBottomLeftProperty;
    }

    public BaseProperty<Integer> borderRadiusBottomRightProperty()
    {
        if (this.borderRadiusBottomRightProperty == null)
            this.borderRadiusBottomRightProperty = new BaseProperty<>(0, "borderRadiusBottomRightProperty");
        return this.borderRadiusBottomRightProperty;
    }

    public BaseProperty<Texture> borderImageProperty()
    {
        if (this.borderImageProperty == null)
            this.borderImageProperty = new BaseProperty<>(Texture.EMPTY, "borderImageProperty");
        return this.borderImageProperty;
    }

    public BaseProperty<RectBox> borderImageSliceProperty()
    {
        if (this.borderImageSliceProperty == null)
            this.borderImageSliceProperty = new BaseProperty<>(RectBox.build().all(1).create(),
                    "borderImageSliceProperty");
        return this.borderImageSliceProperty;
    }

    public BaseProperty<RectBox> borderImageWidthProperty()
    {
        if (this.borderImageWidthProperty == null)
            this.borderImageWidthProperty = new BaseProperty<>(RectBox.build().all(1).create(),
                    "borderImageWidthProperty");
        return this.borderImageWidthProperty;
    }

    public BaseProperty<RectBox> borderImageOutsetProperty()
    {
        if (this.borderImageOutsetProperty == null)
            this.borderImageOutsetProperty = new BaseProperty<>(RectBox.build().all(0).create(),
                    "borderImageOutsetProperty");
        return this.borderImageOutsetProperty;
    }

    public BaseProperty<Boolean> borderImageFillProperty()
    {
        if (this.borderImageFillProperty == null)
            this.borderImageFillProperty = new BaseProperty<>(false, "borderImageFillProperty");
        return this.borderImageFillProperty;
    }

    ////////////
    // VALUES //
    ////////////

    public Texture backgroundTexture()
    {
        return this.backgroundTextureProperty().getValue();
    }

    public void backgroundTexture(Texture texture)
    {
        this.backgroundTextureProperty().setValue(texture);
    }

    public Color backgroundColor()
    {
        return this.backgroundColorProperty().getValue();
    }

    public void backgroundColor(Color color)
    {
        this.backgroundColorProperty().setValue(color);
    }

    public Texture foregroundTexture()
    {
        return this.foregroundTextureProperty().getValue();
    }

    public void foregroundTexture(Texture texture)
    {
        this.foregroundTextureProperty().setValue(texture);
    }

    public Color foregroundColor()
    {
        return this.foregroundColorProperty().getValue();
    }

    public void foregroundColor(Color color)
    {
        this.foregroundColorProperty().setValue(color);
    }

    public Color borderColor()
    {
        return this.borderColorProperty().getValue();
    }

    public void borderColor(Color color)
    {
        this.borderColorProperty().setValue(color);
    }

    public float borderWidth(RectSide side)
    {
        switch (side)
        {
            case UP:
                return this.borderWidthTopProperty().getValue();
            case DOWN:
                return this.borderWidthBottomProperty().getValue();
            case LEFT:
                return this.borderWidthLeftProperty().getValue();
            case RIGHT:
                return this.borderWidthRightProperty().getValue();
            default:
                return 0;
        }
    }

    public void borderWidth(RectSide side, float width)
    {
        switch (side)
        {
            case UP:
                this.borderWidthTopProperty().setValue(width);
                break;
            case DOWN:
                this.borderWidthBottomProperty().setValue(width);
                break;
            case LEFT:
                this.borderWidthLeftProperty().setValue(width);
                break;
            case RIGHT:
                this.borderWidthRightProperty().setValue(width);
                break;
        }
    }

    public int borderRadius(RectCorner corner)
    {
        switch (corner)
        {
            case TOP_LEFT:
                return this.borderRadiusTopLeftProperty().getValue();
            case TOP_RIGHT:
                return this.borderRadiusTopRightProperty().getValue();
            case BOTTOM_LEFT:
                return this.borderRadiusBottomLeftProperty().getValue();
            case BOTTOM_RIGHT:
                return this.borderRadiusBottomRightProperty().getValue();
            default:
                return 0;
        }
    }

    public void borderRadius(RectCorner corner, int width)
    {
        switch (corner)
        {
            case TOP_LEFT:
                this.borderRadiusTopLeftProperty().setValue(width);
                break;
            case TOP_RIGHT:
                this.borderRadiusTopRightProperty().setValue(width);
                break;
            case BOTTOM_LEFT:
                this.borderRadiusBottomLeftProperty().setValue(width);
                break;
            case BOTTOM_RIGHT:
                this.borderRadiusBottomRightProperty().setValue(width);
                break;
        }
    }

    public Texture borderImage()
    {
        return this.borderImageProperty().getValue();
    }

    public void borderImage(Texture texture)
    {
        this.borderImageProperty().setValue(texture);
    }

    public RectBox borderImageSlice()
    {
        return this.borderImageSliceProperty().getValue();
    }

    public void borderImageSlice(RectBox box)
    {
        this.borderImageSliceProperty().setValue(box);
    }

    public RectBox borderImageWidth()
    {
        return this.borderImageWidthProperty().getValue();
    }

    public void borderImageWidth(RectBox box)
    {
        this.borderImageWidthProperty().setValue(box);
    }

    public RectBox borderImageOutset()
    {
        return this.borderImageOutsetProperty().getValue();
    }

    public void borderImageOutset(RectBox box)
    {
        this.borderImageOutsetProperty().setValue(box);
    }

    public boolean borderImageFill()
    {
        return this.borderImageFillProperty().getValue();
    }

    public void borderImageFill(boolean doFill)
    {
        this.borderImageFillProperty().setValue(doFill);
    }
}
