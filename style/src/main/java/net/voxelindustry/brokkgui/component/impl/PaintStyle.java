package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.FillMethod;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.data.Resource;
import net.voxelindustry.brokkgui.sprite.RandomSpriteRotation;
import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.style.HeldPropertyState;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.optional.BorderImageProperties;
import net.voxelindustry.brokkgui.style.optional.BorderProperties;
import net.voxelindustry.brokkgui.style.optional.OutlineProperties;
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

        style.registerConditionalProperties("outline*", OutlineProperties.getInstance());

        style.registerProperty("fill-method", FillMethod.HORIZONTAL, FillMethod.class);
        style.registerProperty("fill-amount", 1F, Float.class);
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
    public Property<RectBox> borderBoxProperty()
    {
        if (borderBoxProperty == null)
            borderBoxProperty = style().getOrCreateProperty("border-box", RectBox.class);
        return borderBoxProperty;
    }

    @Override
    public Property<RectBox> outlineBoxProperty()
    {
        if (outlineBoxProperty == null)
            outlineBoxProperty = style().getOrCreateProperty("outline-box", RectBox.class);
        return outlineBoxProperty;
    }

    @Override
    public Property<Color> outlineColorProperty()
    {
        if (outlineColorProperty == null)
            outlineColorProperty = style().getOrCreateProperty("outline-color", Color.class);
        return outlineColorProperty;
    }

    @Override
    public Property<Float> outlineWidthLeftProperty()
    {
        if (outlineWidthLeftProperty == null)
            outlineWidthLeftProperty = style().getOrCreateProperty("outline-left-width", Float.class);
        return outlineWidthLeftProperty;
    }

    @Override
    public Property<Float> outlineWidthRightProperty()
    {
        if (outlineWidthRightProperty == null)
            outlineWidthRightProperty = style().getOrCreateProperty("outline-right-width", Float.class);
        return outlineWidthRightProperty;
    }

    @Override
    public Property<Float> outlineWidthTopProperty()
    {
        if (outlineWidthTopProperty == null)
            outlineWidthTopProperty = style().getOrCreateProperty("outline-top-width", Float.class);
        return outlineWidthTopProperty;
    }

    @Override
    public Property<Float> outlineWidthBottomProperty()
    {
        if (outlineWidthBottomProperty == null)
            outlineWidthBottomProperty = style().getOrCreateProperty("outline-bottom-width", Float.class);
        return outlineWidthBottomProperty;
    }

    @Override
    public Property<Integer> outlineRadiusTopLeftProperty()
    {
        if (outlineRadiusTopLeftProperty == null)
            outlineRadiusTopLeftProperty = style().getOrCreateProperty("outline-top-left-radius", Integer.class);
        return outlineRadiusTopLeftProperty;
    }

    @Override
    public Property<Integer> outlineRadiusTopRightProperty()
    {
        if (outlineRadiusTopRightProperty == null)
            outlineRadiusTopRightProperty = style().getOrCreateProperty("outline-top-right-radius", Integer.class);
        return outlineRadiusTopRightProperty;
    }

    @Override
    public Property<Integer> outlineRadiusBottomLeftProperty()
    {
        if (outlineRadiusBottomLeftProperty == null)
            outlineRadiusBottomLeftProperty = style().getOrCreateProperty("outline-bottom-left-radius", Integer.class);
        return outlineRadiusBottomLeftProperty;
    }

    @Override
    public Property<Integer> outlineRadiusBottomRightProperty()
    {
        if (outlineRadiusBottomRightProperty == null)
            outlineRadiusBottomRightProperty = style().getOrCreateProperty("outline-bottom-right-radius", Integer.class);
        return outlineRadiusBottomRightProperty;
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

    @Override
    public Property<FillMethod> fillMethodProperty()
    {
        if (fillMethodProperty == null)
            fillMethodProperty = style().getOrCreateProperty("fill-method", FillMethod.class);
        return fillMethodProperty;
    }

    @Override
    public Property<Float> fillAmountProperty()
    {
        if (fillAmountProperty == null)
            fillAmountProperty = style().getOrCreateProperty("fill-amount", Float.class);
        return fillAmountProperty;
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
    public boolean hasOutline()
    {
        return style().doesHoldProperty("outline-width") == HeldPropertyState.PRESENT;
    }

    @Override
    public boolean hasBorderImage()
    {
        return style().doesHoldProperty("border-image-source") == HeldPropertyState.PRESENT;
    }

    @Override
    public RectBox borderBox()
    {
        return style().getValue("border-box", RectBox.class, RectBox.EMPTY);
    }

    @Override
    public void borderBox(RectBox borderBox)
    {
        style().setPropertyDirect("border-box", borderBox, RectBox.class);
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
    public Color outlineColor()
    {
        return style().getValue("outline-color", Color.class, Color.ALPHA);
    }

    @Override
    public void outlineColor(Color color)
    {
        style().setPropertyDirect("outline-color", color, Color.class);
    }

    @Override
    public float outlineWidth()
    {
        return style().getValue("outline-top-width", Float.class, 0F);
    }

    @Override
    public float outlineWidth(RectSide side)
    {
        return style().getValue("outline-" + side.getCssString() + "-width", Float.class, 0F);
    }

    @Override
    public void outlineWidth(float width)
    {
        outlineWidth(RectSide.UP, width);
        outlineWidth(RectSide.DOWN, width);
        outlineWidth(RectSide.LEFT, width);
        outlineWidth(RectSide.RIGHT, width);
    }

    @Override
    public void outlineWidth(RectSide side, float outlineWidth)
    {
        style().setPropertyDirect("outline-" + side.getCssString() + "-width", outlineWidth, Float.class);
    }

    @Override
    public int outlineRadius(RectCorner corner)
    {
        return style().getValue("outline-" + corner.getCssString() + "-radius", Integer.class, 0);
    }

    @Override
    public void outlineRadius(RectCorner corner, int radius)
    {
        style().setPropertyDirect("outline-" + corner.getCssString() + "-radius", radius, Integer.class);
    }

    @Override
    public RectBox outlineBox()
    {
        return style().getValue("outline-box", RectBox.class, RectBox.EMPTY);
    }

    @Override
    public void outlineBox(RectBox outlineBox)
    {
        style().setPropertyDirect("outline-box", outlineBox, RectBox.class);
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

    @Override
    public FillMethod fillMethod()
    {
        return style().getValue("fill-method", FillMethod.class, FillMethod.HORIZONTAL);
    }

    @Override
    public void fillMethod(FillMethod fillMethod)
    {
        style().setPropertyDirect("fill-method", fillMethod, FillMethod.class);
    }

    @Override
    public float fillAmount()
    {
        return style().getValue("fill-amount", Float.class, 1F);
    }

    @Override
    public void fillAmount(float fillAmount)
    {
        style().setPropertyDirect("fill-amount", fillAmount, Float.class);
    }
}
