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
import net.voxelindustry.brokkgui.paint.border.BorderPaint;
import net.voxelindustry.brokkgui.paint.outline.OutlinePaint;
import net.voxelindustry.brokkgui.sprite.RandomSpriteRotation;
import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.style.HeldPropertyState;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.optional.OutlineProperties;
import net.voxelindustry.brokkgui.style.optional.SpriteProperties;
import net.voxelindustry.brokkgui.style.optional.SpriteRandomRotationProperties;
import net.voxelindustry.brokkgui.style.shorthand.paint.BorderPaintStyleProperty;
import net.voxelindustry.brokkgui.style.shorthand.paint.OutlinePaintStyleProperty;

public class PaintStyle extends Paint
{
    private StyleComponent style;

    private final BorderPaintStyleProperty  borderProperty  = new BorderPaintStyleProperty("border");
    private final OutlinePaintStyleProperty outlineProperty = new OutlinePaintStyleProperty("outline");

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

        style.registerConditionalProperties("outline*", OutlineProperties.getInstance());

        style.registerProperty(borderProperty);
        style.registerProperty(outlineProperty);

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
    public BorderPaint border()
    {
        return borderProperty;
    }

    @Override
    public OutlinePaint outline()
    {
        return outlineProperty;
    }

    @Override
    public boolean hasBorder()
    {
        return !borderProperty.isEmpty();
    }

    @Override
    public boolean hasOutline()
    {
        return !outlineProperty.isEmpty();
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
