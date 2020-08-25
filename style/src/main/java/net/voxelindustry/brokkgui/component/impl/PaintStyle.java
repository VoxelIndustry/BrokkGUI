package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.data.Resource;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.sprite.RandomSpriteRotation;
import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
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
        return style().doesHoldProperty("background-texture") == HeldPropertyState.PRESENT;
    }

    @Override
    public boolean hasColorBackground()
    {
        return style().doesHoldProperty("background-color") == HeldPropertyState.PRESENT;
    }

    @Override
    public boolean hasTextureForeground()
    {
        return style().doesHoldProperty("foreground-texture") == HeldPropertyState.PRESENT;
    }

    @Override
    public boolean hasColorForeground()
    {
        return style().doesHoldProperty("foreground-color") == HeldPropertyState.PRESENT;
    }

    @Override
    public Property<Texture> backgroundTextureProperty()
    {
        if (backgroundTextureProperty == null)
            backgroundTextureProperty = style().getOrCreateProperty("background-texture", Texture.class);
        return backgroundTextureProperty;
    }

    @Override
    public Property<SpriteRepeat> backgroundRepeatProperty()
    {
        if (backgroundRepeatProperty == null)
            backgroundRepeatProperty = style.getOrCreateProperty("background-repeat", SpriteRepeat.class);
        return backgroundRepeatProperty;
    }

    @Override
    public Property<Resource> backgroundAnimationResourceProperty()
    {
        if (backgroundAnimationProperty == null)
            backgroundAnimationProperty = style.getOrCreateProperty("background-animation", Resource.class);
        return backgroundAnimationProperty;
    }

    @Override
    public Property<RectBox> backgroundPositionProperty()
    {
        if (backgroundPositionProperty == null)
            backgroundPositionProperty = style.getOrCreateProperty("background-position", RectBox.class);
        return backgroundPositionProperty;
    }

    @Override
    public Property<RandomSpriteRotation> backgroundRandomRotationProperty()
    {
        if (backgroundRotationProperty == null)
            backgroundRotationProperty = style.getOrCreateProperty("background-rotation", RandomSpriteRotation.class);
        return backgroundRotationProperty;
    }

    @Override
    public Property<Color> backgroundColorProperty()
    {
        if (backgroundColorProperty == null)
            backgroundColorProperty = style().getOrCreateProperty("background-color", Color.class);
        return backgroundColorProperty;
    }

    @Override
    public Property<Texture> foregroundTextureProperty()
    {
        if (foregroundTextureProperty == null)
            foregroundTextureProperty = style().getOrCreateProperty("foreground-texture", Texture.class);
        return foregroundTextureProperty;
    }

    @Override
    public Property<SpriteRepeat> foregroundRepeatProperty()
    {
        if (foregroundRepeatProperty == null)
            foregroundRepeatProperty = style.getOrCreateProperty("foreground-repeat", SpriteRepeat.class);
        return foregroundRepeatProperty;
    }

    @Override
    public Property<Resource> foregroundAnimationResourceProperty()
    {
        if (foregroundAnimationProperty == null)
            foregroundAnimationProperty = style.getOrCreateProperty("foreground-animation", Resource.class);
        return foregroundAnimationProperty;
    }

    @Override
    public Property<RectBox> foregroundPositionProperty()
    {
        if (foregroundPositionProperty == null)
            foregroundPositionProperty = style.getOrCreateProperty("foreground-position", RectBox.class);
        return foregroundPositionProperty;
    }

    @Override
    public Property<RandomSpriteRotation> foregroundRandomRotationProperty()
    {
        if (foregroundRotationProperty == null)
            foregroundRotationProperty = style.getOrCreateProperty("foreground-rotation", RandomSpriteRotation.class);
        return foregroundRotationProperty;
    }

    @Override
    public Property<Color> foregroundColorProperty()
    {
        if (foregroundColorProperty == null)
            foregroundColorProperty = style().getOrCreateProperty("foreground-color", Color.class);
        return foregroundColorProperty;
    }

    @Override
    public Property<Color> borderColorProperty()
    {
        if (borderColorProperty == null)
            borderColorProperty = style().getOrCreateProperty("border-color", Color.class);
        return borderColorProperty;
    }

    @Override
    public Property<Float> borderWidthLeftProperty()
    {
        if (borderWidthLeftProperty == null)
            borderWidthLeftProperty = style().getOrCreateProperty("border-left-width", Float.class);
        return borderWidthLeftProperty;
    }

    @Override
    public Property<Float> borderWidthRightProperty()
    {
        if (borderWidthRightProperty == null)
            borderWidthRightProperty = style().getOrCreateProperty("border-right-width", Float.class);
        return borderWidthRightProperty;
    }

    @Override
    public Property<Float> borderWidthTopProperty()
    {
        if (borderWidthTopProperty == null)
            borderWidthTopProperty = style().getOrCreateProperty("border-top-width", Float.class);
        return borderWidthTopProperty;
    }

    @Override
    public Property<Float> borderWidthBottomProperty()
    {
        if (borderWidthBottomProperty == null)
            borderWidthBottomProperty = style().getOrCreateProperty("border-bottom-width", Float.class);
        return borderWidthBottomProperty;
    }

    @Override
    public Property<Integer> borderRadiusTopLeftProperty()
    {
        if (borderRadiusTopLeftProperty == null)
            borderRadiusTopLeftProperty = style().getOrCreateProperty("border-top-left-radius", Integer.class);
        return borderRadiusTopLeftProperty;
    }

    @Override
    public Property<Integer> borderRadiusTopRightProperty()
    {
        if (borderRadiusTopRightProperty == null)
            borderRadiusTopRightProperty = style().getOrCreateProperty("border-top-right-radius", Integer.class);
        return borderRadiusTopRightProperty;
    }

    @Override
    public Property<Integer> borderRadiusBottomLeftProperty()
    {
        if (borderRadiusBottomLeftProperty == null)
            borderRadiusBottomLeftProperty = style().getOrCreateProperty("border-bottom-left-radius", Integer.class);
        return borderRadiusBottomLeftProperty;
    }

    @Override
    public Property<Integer> borderRadiusBottomRightProperty()
    {
        if (borderRadiusBottomRightProperty == null)
            borderRadiusBottomRightProperty = style().getOrCreateProperty("border-bottom-right-radius", Integer.class);
        return borderRadiusBottomRightProperty;
    }

    @Override
    public Property<Texture> borderImageProperty()
    {
        if (borderImageProperty == null)
            borderImageProperty = style().getOrCreateProperty("border-image-source", Texture.class);
        return borderImageProperty;
    }

    @Override
    public Property<RectBox> borderImageSliceProperty()
    {
        if (borderImageSliceProperty == null)
            borderImageSliceProperty = style().getOrCreateProperty("border-image-slice", RectBox.class);
        return borderImageSliceProperty;
    }

    @Override
    public Property<RectBox> borderImageWidthProperty()
    {
        if (borderImageWidthProperty == null)
            borderImageWidthProperty = style().getOrCreateProperty("border-image-width", RectBox.class);
        return borderImageWidthProperty;
    }

    @Override
    public Property<RectBox> borderImageOutsetProperty()
    {
        if (borderImageOutsetProperty == null)
            borderImageOutsetProperty = style().getOrCreateProperty("border-image-outset", RectBox.class);
        return borderImageOutsetProperty;
    }

    @Override
    public Property<Boolean> borderImageFillProperty()
    {
        if (borderImageFillProperty == null)
            borderImageFillProperty = style().getOrCreateProperty("border-image-fill", Boolean.class);
        return borderImageFillProperty;
    }

    ////////////
    // VALUES //
    ////////////

    @Override
    public Texture backgroundTexture()
    {
        return style().getValue("background-texture", Texture.class, Texture.EMPTY);
    }

    @Override
    public void backgroundTexture(Texture texture)
    {
        style().setPropertyDirect("background-texture", texture, Texture.class);
    }

    @Override
    public SpriteRepeat backgroundRepeat()
    {
        return style().getValue("background-repeat", SpriteRepeat.class, SpriteRepeat.NONE);
    }

    @Override
    public void backgroundRepeat(SpriteRepeat spriteRepeat)
    {
        style().setPropertyDirect("background-repeat", spriteRepeat, SpriteRepeat.class);
    }

    @Override
    public Resource backgroundAnimationResource()
    {
        return style().getValue("background-animation", Resource.class, null);
    }

    @Override
    public void backgroundAnimationResource(Resource resource)
    {
        style().setPropertyDirect("background-animation", resource, Resource.class);
    }

    @Override
    public RectBox backgroundPosition()
    {
        return style().getValue("background-position", RectBox.class, RectBox.EMPTY);
    }

    @Override
    public void backgroundPosition(RectBox position)
    {
        style().setPropertyDirect("background-position", position, RectBox.class);
    }

    @Override
    public RandomSpriteRotation backgroundRandomRotation()
    {
        return style().getValue("background-rotation", RandomSpriteRotation.class, null);
    }

    @Override
    public void backgroundRandomRotation(RandomSpriteRotation rotation)
    {
        style().setPropertyDirect("background-rotation", rotation, RandomSpriteRotation.class);
    }

    @Override
    public Color backgroundColor()
    {
        return style().getValue("background-color", Color.class, Color.ALPHA);
    }

    @Override
    public void backgroundColor(Color color)
    {
        style().setPropertyDirect("background-color", color, Color.class);
    }

    @Override
    public Texture foregroundTexture()
    {
        return style().getValue("foreground-texture", Texture.class, Texture.EMPTY);
    }

    @Override
    public void foregroundTexture(Texture texture)
    {
        style().setPropertyDirect("foreground-texture", texture, Texture.class);
    }

    @Override
    public SpriteRepeat foregroundRepeat()
    {
        return style().getValue("foreground-repeat", SpriteRepeat.class, SpriteRepeat.NONE);
    }

    @Override
    public void foregroundRepeat(SpriteRepeat spriteRepeat)
    {
        style().setPropertyDirect("foreground-repeat", spriteRepeat, SpriteRepeat.class);
    }

    @Override
    public Resource foregroundAnimationResource()
    {
        return style().getValue("foreground-animation", Resource.class, null);
    }

    @Override
    public void foregroundAnimationResource(Resource resource)
    {
        style().setPropertyDirect("foreground-animation", resource, Resource.class);
    }

    @Override
    public RectBox foregroundPosition()
    {
        return style().getValue("foreground-position", RectBox.class, RectBox.EMPTY);
    }

    @Override
    public void foregroundPosition(RectBox position)
    {
        style().setPropertyDirect("foreground-position", position, RectBox.class);
    }

    @Override
    public RandomSpriteRotation foregroundRandomRotation()
    {
        return style().getValue("foreground-rotation", RandomSpriteRotation.class, null);
    }

    @Override
    public void foregroundRandomRotation(RandomSpriteRotation rotation)
    {
        style().setPropertyDirect("foreground-rotation", rotation, RandomSpriteRotation.class);
    }

    @Override
    public Color foregroundColor()
    {
        return style().getValue("foreground-color", Color.class, Color.ALPHA);
    }

    @Override
    public void foregroundColor(Color color)
    {
        style().setPropertyDirect("foreground-color", color, Color.class);
    }

    @Override
    public boolean hasBorder()
    {
        return style().doesHoldProperty("border-width") == HeldPropertyState.PRESENT;
    }

    @Override
    public boolean hasBorderImage()
    {
        return style().doesHoldProperty("border-image-source") == HeldPropertyState.PRESENT;
    }

    @Override
    public float borderWidth()
    {
        return style().getValue("border-top-width", Float.class, 0F);
    }

    @Override
    public float borderWidth(RectSide side)
    {
        return style().getValue("border-" + side.getCssString() + "-width", Float.class, 0F);
    }

    @Override
    public void borderWidth(RectSide side, float borderWidth)
    {
        style().setPropertyDirect("border-" + side.getCssString() + "-width", borderWidth, Float.class);
    }

    @Override
    public int borderRadius(RectCorner corner)
    {
        return style().getValue("border-" + corner.getCssString() + "-radius", Integer.class, 0);
    }

    @Override
    public void borderRadius(RectCorner corner, int radius)
    {
        style().setPropertyDirect("border-" + corner.getCssString() + "-radius", radius, Integer.class);
    }

    @Override
    public Color borderColor()
    {
        return style().getValue("border-color", Color.class, Color.ALPHA);
    }

    @Override
    public void borderColor(Color color)
    {
        style().setPropertyDirect("border-color", color, Color.class);
    }

    @Override
    public Texture borderImage()
    {
        return style().getValue("border-image-source", Texture.class, Texture.EMPTY);
    }

    @Override
    public void borderImage(Texture texture)
    {
        style().setPropertyDirect("border-image-source", texture, Texture.class);
    }

    @Override
    public RectBox borderImageSlice()
    {
        return style().getValue("border-image-slice", RectBox.class, RectBox.ONE);
    }

    @Override
    public void borderImageSlice(RectBox box)
    {
        style().setPropertyDirect("border-image-slice", box, RectBox.class);
    }

    @Override
    public RectBox borderImageWidth()
    {
        return style().getValue("border-image-width", RectBox.class, RectBox.ONE);
    }

    @Override
    public void borderImageWidth(RectBox box)
    {
        style().setPropertyDirect("border-image-width", box, RectBox.class);
    }

    @Override
    public RectBox borderImageOutset()
    {
        return style().getValue("border-image-outset", RectBox.class, RectBox.EMPTY);
    }

    @Override
    public void borderImageOutset(RectBox box)
    {
        style().setPropertyDirect("border-image-outset", box, RectBox.class);
    }

    @Override
    public boolean borderImageFill()
    {
        return style().getValue("border-image-fill", Boolean.class, Boolean.FALSE);
    }

    @Override
    public void borderImageFill(boolean doFill)
    {
        style().setPropertyDirect("border-image-fill", doFill, Boolean.class);
    }
}
