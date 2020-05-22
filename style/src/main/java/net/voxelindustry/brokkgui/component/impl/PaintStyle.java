package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.style.HeldPropertyState;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.optional.BorderImageProperties;
import net.voxelindustry.brokkgui.style.optional.BorderProperties;
import net.voxelindustry.brokkgui.style.optional.SpriteProperties;
import net.voxelindustry.brokkgui.style.optional.SpriteRandomRotationProperties;

public class PaintStyle extends Paint
{
    private StyleComponent style;

    public PaintStyle()
    {

    }

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        if (!element.has(StyleComponent.class))
            throw new GuiComponentException("Cannot attach PaintStyle to an element not having StyleComponent!");

        style = element.get(StyleComponent.class);

        style.registerConditionalProperties("background*", SpriteProperties.getBackgroundInstance());
        style.registerConditionalProperties("foreground*", SpriteProperties.getForegroundInstance());

        style.registerConditionalProperties("background-rotation", SpriteRandomRotationProperties.getBackgroundInstance());
        style.registerConditionalProperties("foreground-rotation", SpriteRandomRotationProperties.getForegroundInstance());

        style.registerConditionalProperties("border*", BorderProperties.getInstance());
        style.registerConditionalProperties("border-image*", BorderImageProperties.getInstance());
    }

    private StyleComponent style()
    {
        return style;
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    @Override
    public boolean hasTextureBackground()
    {
        return this.style().doesHoldProperty("background-texture") == HeldPropertyState.PRESENT;
    }

    @Override
    public boolean hasColorBackground()
    {
        return this.style().doesHoldProperty("background-color") == HeldPropertyState.PRESENT;
    }

    @Override
    public boolean hasTextureForeground()
    {
        return this.style().doesHoldProperty("foreground-texture") == HeldPropertyState.PRESENT;
    }

    @Override
    public boolean hasColorForeground()
    {
        return this.style().doesHoldProperty("foreground-color") == HeldPropertyState.PRESENT;
    }

    @Override
    public BaseProperty<Texture> backgroundTextureProperty()
    {
        if (this.backgroundTextureProperty == null)
            this.backgroundTextureProperty = style().getOrCreateProperty("background-texture", Texture.class);
        return backgroundTextureProperty;
    }

    @Override
    public BaseProperty<Color> backgroundColorProperty()
    {
        if (this.backgroundColorProperty == null)
            this.backgroundColorProperty = style().getOrCreateProperty("background-color", Color.class);
        return backgroundColorProperty;
    }

    @Override
    public BaseProperty<Texture> foregroundTextureProperty()
    {
        if (this.foregroundTextureProperty == null)
            this.foregroundTextureProperty = style().getOrCreateProperty("foreground-texture", Texture.class);
        return foregroundTextureProperty;
    }

    @Override
    public BaseProperty<Color> foregroundColorProperty()
    {
        if (this.foregroundColorProperty == null)
            this.foregroundColorProperty = style().getOrCreateProperty("foreground-color", Color.class);
        return foregroundColorProperty;
    }

    @Override
    public BaseProperty<Color> borderColorProperty()
    {
        if (this.borderColorProperty == null)
            this.borderColorProperty = style().getOrCreateProperty("border-color", Color.class);
        return this.borderColorProperty;
    }

    @Override
    public BaseProperty<Float> borderWidthLeftProperty()
    {
        if (this.borderWidthLeftProperty == null)
            this.borderWidthLeftProperty = style().getOrCreateProperty("border-left-width", Float.class);
        return this.borderWidthLeftProperty;
    }

    @Override
    public BaseProperty<Float> borderWidthRightProperty()
    {
        if (this.borderWidthRightProperty == null)
            this.borderWidthRightProperty = style().getOrCreateProperty("border-right-width", Float.class);
        return this.borderWidthRightProperty;
    }

    @Override
    public BaseProperty<Float> borderWidthTopProperty()
    {
        if (this.borderWidthTopProperty == null)
            this.borderWidthTopProperty = style().getOrCreateProperty("border-top-width", Float.class);
        return this.borderWidthTopProperty;
    }

    @Override
    public BaseProperty<Float> borderWidthBottomProperty()
    {
        if (this.borderWidthBottomProperty == null)
            this.borderWidthBottomProperty = style().getOrCreateProperty("border-bottom-width", Float.class);
        return this.borderWidthBottomProperty;
    }

    @Override
    public BaseProperty<Integer> borderRadiusTopLeftProperty()
    {
        if (this.borderRadiusTopLeftProperty == null)
            this.borderRadiusTopLeftProperty = style().getOrCreateProperty("border-top-left-radius", Integer.class);
        return this.borderRadiusTopLeftProperty;
    }

    @Override
    public BaseProperty<Integer> borderRadiusTopRightProperty()
    {
        if (this.borderRadiusTopRightProperty == null)
            this.borderRadiusTopRightProperty = style().getOrCreateProperty("border-top-right-radius", Integer.class);
        return this.borderRadiusTopRightProperty;
    }

    @Override
    public BaseProperty<Integer> borderRadiusBottomLeftProperty()
    {
        if (this.borderRadiusBottomLeftProperty == null)
            this.borderRadiusBottomLeftProperty = style().getOrCreateProperty("border-bottom-left-radius", Integer.class);
        return this.borderRadiusBottomLeftProperty;
    }

    @Override
    public BaseProperty<Integer> borderRadiusBottomRightProperty()
    {
        if (this.borderRadiusBottomRightProperty == null)
            this.borderRadiusBottomRightProperty = style().getOrCreateProperty("border-bottom-right-radius", Integer.class);
        return this.borderRadiusBottomRightProperty;
    }

    @Override
    public BaseProperty<Texture> borderImageProperty()
    {
        if (this.borderImageProperty == null)
            this.borderImageProperty = style().getOrCreateProperty("border-image-source", Texture.class);
        return this.borderImageProperty;
    }

    @Override
    public BaseProperty<RectBox> borderImageSliceProperty()
    {
        if (this.borderImageSliceProperty == null)
            this.borderImageSliceProperty = style().getOrCreateProperty("border-image-slice", RectBox.class);
        return this.borderImageSliceProperty;
    }

    @Override
    public BaseProperty<RectBox> borderImageWidthProperty()
    {
        if (this.borderImageWidthProperty == null)
            this.borderImageWidthProperty = style().getOrCreateProperty("border-image-width", RectBox.class);
        return this.borderImageWidthProperty;
    }

    @Override
    public BaseProperty<RectBox> borderImageOutsetProperty()
    {
        if (this.borderImageOutsetProperty == null)
            this.borderImageOutsetProperty = style().getOrCreateProperty("border-image-outset", RectBox.class);
        return this.borderImageOutsetProperty;
    }

    @Override
    public BaseProperty<Boolean> borderImageFillProperty()
    {
        if (this.borderImageFillProperty == null)
            this.borderImageFillProperty = style().getOrCreateProperty("border-image-fill", Boolean.class);
        return this.borderImageFillProperty;
    }

    ////////////
    // VALUES //
    ////////////

    @Override
    public Texture backgroundTexture()
    {
        return this.style().getValue("background-texture", Texture.class, Texture.EMPTY);
    }

    @Override
    public void backgroundTexture(Texture texture)
    {
        this.style().setPropertyDirect("background-texture", texture, Texture.class);
    }

    @Override
    public Color backgroundColor()
    {
        return this.style().getValue("background-color", Color.class, Color.ALPHA);
    }

    @Override
    public void backgroundColor(Color color)
    {
        this.style().setPropertyDirect("background-color", color, Color.class);
    }

    @Override
    public Texture foregroundTexture()
    {
        return this.style().getValue("foreground-texture", Texture.class, Texture.EMPTY);
    }

    @Override
    public void foregroundTexture(Texture texture)
    {
        this.style().setPropertyDirect("foreground-texture", texture, Texture.class);
    }

    @Override
    public Color foregroundColor()
    {
        return this.style().getValue("foreground-color", Color.class, Color.ALPHA);
    }

    @Override
    public void foregroundColor(Color color)
    {
        this.style().setPropertyDirect("foreground-color", color, Color.class);
    }

    @Override
    public boolean hasBorder()
    {
        return this.style().doesHoldProperty("border-width") == HeldPropertyState.PRESENT;
    }

    @Override
    public boolean hasBorderImage()
    {
        return this.style().doesHoldProperty("border-image-source") == HeldPropertyState.PRESENT;
    }

    @Override
    public float borderWidth(RectSide side)
    {
        return this.style().getValue("border-" + side.getCssString() + "-width", Float.class, 0f);
    }

    @Override
    public void borderWidth(RectSide side, float borderWidth)
    {
        this.style().setPropertyDirect("border-" + side.getCssString() + "-width", borderWidth, Float.class);
    }

    @Override
    public int borderRadius(RectCorner corner)
    {
        return this.style().getValue("border-" + corner.getCssString() + "-radius", Integer.class, 0);
    }

    @Override
    public void borderRadius(RectCorner corner, int radius)
    {
        this.style().setPropertyDirect("border-" + corner.getCssString() + "-radius", radius, Integer.class);
    }

    @Override
    public Color borderColor()
    {
        return this.style().getValue("border-color", Color.class, Color.ALPHA);
    }

    @Override
    public void borderColor(Color color)
    {
        this.style().setPropertyDirect("border-color", color, Color.class);
    }

    @Override
    public Texture borderImage()
    {
        return this.style().getValue("border-image-source", Texture.class, Texture.EMPTY);
    }

    @Override
    public void borderImage(Texture texture)
    {
        this.style().setPropertyDirect("border-image-source", texture, Texture.class);
    }

    @Override
    public RectBox borderImageSlice()
    {
        return this.style().getValue("border-image-slice", RectBox.class, RectBox.ONE);
    }

    @Override
    public void borderImageSlice(RectBox box)
    {
        this.style().setPropertyDirect("border-image-slice", box, RectBox.class);
    }

    @Override
    public RectBox borderImageWidth()
    {
        return this.style().getValue("border-image-width", RectBox.class, RectBox.ONE);
    }

    @Override
    public void borderImageWidth(RectBox box)
    {
        this.style().setPropertyDirect("border-image-width", box, RectBox.class);
    }

    @Override
    public RectBox borderImageOutset()
    {
        return this.style().getValue("border-image-outset", RectBox.class, RectBox.EMPTY);
    }

    @Override
    public void borderImageOutset(RectBox box)
    {
        this.style().setPropertyDirect("border-image-outset", box, RectBox.class);
    }

    @Override
    public boolean borderImageFill()
    {
        return this.style().getValue("border-image-fill", Boolean.class, Boolean.FALSE);
    }

    @Override
    public void borderImageFill(boolean doFill)
    {
        this.style().setPropertyDirect("border-image-fill", doFill, Boolean.class);
    }
}
