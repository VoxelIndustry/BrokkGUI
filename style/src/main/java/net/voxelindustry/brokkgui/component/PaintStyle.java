package net.voxelindustry.brokkgui.component;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.style.HeldPropertyState;
import net.voxelindustry.brokkgui.style.StyleHolder;
import net.voxelindustry.brokkgui.style.optional.BorderImageProperties;
import net.voxelindustry.brokkgui.style.optional.BorderProperties;

public class PaintStyle extends Paint
{
    private StyleHolder style;

    public PaintStyle()
    {

    }

    @Override
    protected void attach(GuiElement element)
    {
        super.attach(element);

        if (!element.has(StyleHolder.class))
            throw new GuiComponentException("Cannot attach PaintStyle to an element not having StyleHolder!");

        this.style = element.get(StyleHolder.class);

        style.registerProperty("background-color", Color.ALPHA, Color.class);
        style.registerProperty("foreground-color", Color.ALPHA, Color.class);

        style.registerProperty("background-texture", Texture.EMPTY, Texture.class);
        style.registerProperty("foreground-texture", Texture.EMPTY, Texture.class);

        style.registerConditionalProperties("border*", BorderProperties.getInstance());
        style.registerConditionalProperties("border-image*", BorderImageProperties.getInstance());
    }

    private StyleHolder getStyle()
    {
        return style;
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    @Override
    public boolean hasTextureBackground()
    {
        return this.getStyle().doesHoldProperty("background-texture") == HeldPropertyState.PRESENT;
    }

    @Override
    public boolean hasColorBackground()
    {
        return this.getStyle().doesHoldProperty("background-color") == HeldPropertyState.PRESENT;
    }

    @Override
    public boolean hasTextureForeground()
    {
        return this.getStyle().doesHoldProperty("foreground-texture") == HeldPropertyState.PRESENT;
    }

    @Override
    public boolean hasColorForeground()
    {
        return this.getStyle().doesHoldProperty("foreground-color") == HeldPropertyState.PRESENT;
    }

    @Override
    public BaseProperty<Texture> backgroundTextureProperty()
    {
        return getStyle().getOrCreateProperty("background-texture", Texture.class);
    }

    @Override
    public BaseProperty<Color> backgroundColorProperty()
    {
        return getStyle().getOrCreateProperty("background-color", Color.class);
    }

    @Override
    public BaseProperty<Texture> foregroundTextureProperty()
    {
        return getStyle().getOrCreateProperty("foreground-texture", Texture.class);
    }

    @Override
    public BaseProperty<Color> foregroundColorProperty()
    {
        return getStyle().getOrCreateProperty("foreground-color", Color.class);
    }

    @Override
    public BaseProperty<Color> borderColorProperty()
    {
        return getStyle().getOrCreateProperty("border-color", Color.class);
    }

    @Override
    public BaseProperty<Float> borderWidthLeftProperty()
    {
        return getStyle().getOrCreateProperty("border-left-width", Float.class);
    }

    @Override
    public BaseProperty<Float> borderWidthRightProperty()
    {
        return getStyle().getOrCreateProperty("border-right-width", Float.class);
    }

    @Override
    public BaseProperty<Float> borderWidthTopProperty()
    {
        return getStyle().getOrCreateProperty("border-top-width", Float.class);
    }

    @Override
    public BaseProperty<Float> borderWidthBottomProperty()
    {
        return getStyle().getOrCreateProperty("border-bottom-width", Float.class);
    }

    @Override
    public BaseProperty<Integer> borderRadiusTopLeftProperty()
    {
        return getStyle().getOrCreateProperty("border-top-left-radius", Integer.class);
    }

    @Override
    public BaseProperty<Integer> borderRadiusTopRightProperty()
    {
        return getStyle().getOrCreateProperty("border-top-right-radius", Integer.class);
    }

    @Override
    public BaseProperty<Integer> borderRadiusBottomLeftProperty()
    {
        return getStyle().getOrCreateProperty("border-bottom-left-radius", Integer.class);
    }

    @Override
    public BaseProperty<Integer> borderRadiusBottomRightProperty()
    {
        return getStyle().getOrCreateProperty("border-bottom-right-radius", Integer.class);
    }

    @Override
    public BaseProperty<Texture> borderImageProperty()
    {
        return getStyle().getOrCreateProperty("border-image-source", Texture.class);
    }

    @Override
    public BaseProperty<RectBox> borderImageSliceProperty()
    {
        return getStyle().getOrCreateProperty("border-image-slice", RectBox.class);
    }

    @Override
    public BaseProperty<RectBox> borderImageWidthProperty()
    {
        return getStyle().getOrCreateProperty("border-image-width", RectBox.class);
    }

    @Override
    public BaseProperty<RectBox> borderImageOutsetProperty()
    {
        return getStyle().getOrCreateProperty("border-image-outset", RectBox.class);
    }

    @Override
    public BaseProperty<Boolean> borderImageFillProperty()
    {
        return getStyle().getOrCreateProperty("border-image-fill", Boolean.class);
    }

    ////////////
    // VALUES //
    ////////////

    @Override
    public Texture backgroundTexture()
    {
        return this.getStyle().getStyleValue("background-texture", Texture.class, Texture.EMPTY);
    }

    @Override
    public void backgroundTexture(Texture texture)
    {
        this.getStyle().setPropertyDirect("background-texture", texture, Texture.class);
    }

    @Override
    public Color backgroundColor()
    {
        return this.getStyle().getStyleValue("background-color", Color.class, Color.ALPHA);
    }

    @Override
    public void backgroundColor(Color color)
    {
        this.getStyle().setPropertyDirect("background-color", color, Color.class);
    }

    @Override
    public Texture foregroundTexture()
    {
        return this.getStyle().getStyleValue("foreground-texture", Texture.class, Texture.EMPTY);
    }

    @Override
    public void foregroundTexture(Texture texture)
    {
        this.getStyle().setPropertyDirect("foreground-texture", texture, Texture.class);
    }

    @Override
    public Color foregroundColor()
    {
        return this.getStyle().getStyleValue("foreground-color", Color.class, Color.ALPHA);
    }

    @Override
    public void foregroundColor(Color color)
    {
        this.getStyle().setPropertyDirect("foreground-color", color, Color.class);
    }

    @Override
    public boolean hasBorder()
    {
        return this.getStyle().doesHoldProperty("border-width") == HeldPropertyState.PRESENT;
    }

    @Override
    public boolean hasBorderImage()
    {
        return this.getStyle().doesHoldProperty("border-image-source") == HeldPropertyState.PRESENT;
    }

    @Override
    public float borderWidth(RectSide side)
    {
        return this.getStyle().getStyleValue("border-" + side.getCssString() + "-width", Float.class, 0f);
    }

    @Override
    public void borderWidth(RectSide side, float borderWidth)
    {
        this.getStyle().setPropertyDirect("border-" + side.getCssString() + "-width", borderWidth, Float.class);
    }

    @Override
    public int borderRadius(RectCorner corner)
    {
        return this.getStyle().getStyleValue("border-" + corner.getCssString() + "-radius", Integer.class, 0);
    }

    @Override
    public void borderRadius(RectCorner corner, int radius)
    {
        this.getStyle().setPropertyDirect("border-" + corner.getCssString() + "-radius", radius, Integer.class);
    }

    @Override
    public Color borderColor()
    {
        return this.getStyle().getStyleValue("border-color", Color.class, Color.ALPHA);
    }

    @Override
    public void borderColor(Color color)
    {
        this.getStyle().setPropertyDirect("border-color", color, Color.class);
    }

    @Override
    public Texture borderImage()
    {
        return this.getStyle().getStyleValue("border-image-source", Texture.class, Texture.EMPTY);
    }

    @Override
    public void borderImage(Texture texture)
    {
        this.getStyle().setPropertyDirect("border-image-source", texture, Texture.class);
    }

    @Override
    public RectBox borderImageSlice()
    {
        return this.getStyle().getStyleValue("border-image-slice", RectBox.class, RectBox.ONE);
    }

    @Override
    public void borderImageSlice(RectBox box)
    {
        this.getStyle().setPropertyDirect("border-image-slice", box, RectBox.class);
    }

    @Override
    public RectBox borderImageWidth()
    {
        return this.getStyle().getStyleValue("border-image-width", RectBox.class, RectBox.ONE);
    }

    @Override
    public void borderImageWidth(RectBox box)
    {
        this.getStyle().setPropertyDirect("border-image-width", box, RectBox.class);
    }

    @Override
    public RectBox borderImageOutset()
    {
        return this.getStyle().getStyleValue("border-image-outset", RectBox.class, RectBox.EMPTY);
    }

    @Override
    public void borderImageOutset(RectBox box)
    {
        this.getStyle().setPropertyDirect("border-image-outset", box, RectBox.class);
    }

    @Override
    public boolean borderImageFill()
    {
        return this.getStyle().getStyleValue("border-image-fill", Boolean.class, false);
    }

    @Override
    public void borderImageFill(boolean doFill)
    {
        this.getStyle().setPropertyDirect("border-image-fill", doFill, Boolean.class);
    }
}
