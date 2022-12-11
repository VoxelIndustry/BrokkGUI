package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.border.ColorBorderDrawer;
import net.voxelindustry.brokkgui.border.ImageBorderDrawer;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.RenderComponent;
import net.voxelindustry.brokkgui.component.RequiredOverride;
import net.voxelindustry.brokkgui.data.FillMethod;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.data.Resource;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.border.BorderPaint;
import net.voxelindustry.brokkgui.paint.outline.OutlinePaint;
import net.voxelindustry.brokkgui.shape.ShapeDefinition;
import net.voxelindustry.brokkgui.sprite.RandomSpriteRotation;
import net.voxelindustry.brokkgui.sprite.SpriteAnimationInstance;
import net.voxelindustry.brokkgui.sprite.SpriteBackgroundDrawer;
import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.sprite.SpriteRotation;
import net.voxelindustry.brokkgui.sprite.Texture;

public class Paint extends GuiComponent implements RenderComponent
{
    private ShapeDefinition shape;

    protected Property<Texture> backgroundTextureProperty;
    protected Property<Color>   backgroundColorProperty;

    protected Property<SpriteRepeat> backgroundRepeatProperty;
    protected Property<Resource>     backgroundAnimationProperty;
    protected Property<RectBox>      backgroundPositionProperty;

    protected Property<RandomSpriteRotation> backgroundRotationProperty;

    protected Property<Texture> foregroundTextureProperty;
    protected Property<Color>   foregroundColorProperty;

    protected Property<SpriteRepeat> foregroundRepeatProperty;
    protected Property<Resource>     foregroundAnimationProperty;
    protected Property<RectBox>      foregroundPositionProperty;

    protected Property<RandomSpriteRotation> foregroundRotationProperty;

    protected BorderPaint border = BorderPaint.EMPTY;
    protected OutlinePaint outline = OutlinePaint.EMPTY;

    protected Property<Float>   outlineWidthLeftProperty;
    protected Property<Float>   outlineWidthRightProperty;
    protected Property<Float>   outlineWidthTopProperty;
    protected Property<Float>   outlineWidthBottomProperty;
    protected Property<Integer> outlineRadiusTopLeftProperty;
    protected Property<Integer> outlineRadiusTopRightProperty;
    protected Property<Integer> outlineRadiusBottomLeftProperty;
    protected Property<Integer> outlineRadiusBottomRightProperty;

    protected Property<FillMethod> fillMethodProperty;
    protected Property<Float>      fillAmountProperty;

    private SpriteAnimationInstance backgroundAnimation;
    private SpriteAnimationInstance foregroundAnimation;

    private SpriteRotation[] backgroundRotationArray;
    private SpriteRotation[] foregroundRotationArray;

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

        if (hasElement())
            element().transform().mouseInBoundsChecker(shape::isMouseInside);
    }

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        element.transform().mouseInBoundsChecker((node, mouseX, mouseY) -> shape.isMouseInside(node, mouseX, mouseY));
    }

    @Override
    public void renderContent(IRenderCommandReceiver renderer, float mouseX, float mouseY)
    {
        float x = element().transform().leftPos();
        float y = element().transform().topPos();
        float z = element().transform().zLevel();

        if (fillAmount() < 1)
        {
            if (fillMethod() == FillMethod.HORIZONTAL)
                renderer.pushMask(transform().leftPos(), transform().topPos(), transform().leftPos() + transform().width() * fillAmount(), transform().bottomPos());
            else if (fillMethod() == FillMethod.VERTICAL)
                renderer.pushMask(transform().leftPos(), transform().topPos(), transform().rightPos(), transform().topPos() + transform().height() * fillAmount());
        }

        // BACKGROUND
        if (hasTextureBackground() && backgroundTexture() != Texture.EMPTY)
        {
            SpriteBackgroundDrawer.drawBackground(this, renderer);
        }
        else if (hasColorBackground() && backgroundColor().getAlpha() != 0)
        {
            Color background = backgroundColor();

            shape.drawColored(element().transform(), renderer, x, y, background, z, backgroundPosition());
        }

        // FOREGROUND
        if (hasTextureForeground() && foregroundTexture() != Texture.EMPTY)
        {
            SpriteBackgroundDrawer.drawForeground(this, renderer);
        }
        else if (hasColorForeground() && foregroundColor().getAlpha() != 0)
        {
            Color foreground = foregroundColor();

            shape.drawColored(element().transform(), renderer, x, y, foreground, z, foregroundPosition());
        }

        if (fillAmount() < 1)
        {
            renderer.popMask();
        }

        if (hasBorder())
        {
            if (hasBorderImage())
                ImageBorderDrawer.drawBorder(this, renderer);
            else
                ColorBorderDrawer.drawBorder(this, renderer);
        }

        if (hasOutline())
            ColorBorderDrawer.drawOutline(this, renderer);
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    @RequiredOverride
    public boolean hasTextureBackground()
    {
        return backgroundTextureProperty != null;
    }

    @RequiredOverride
    public boolean hasColorBackground()
    {
        return backgroundColorProperty != null;
    }

    @RequiredOverride
    public boolean hasTextureForeground()
    {
        return foregroundTextureProperty != null;
    }

    @RequiredOverride
    public boolean hasColorForeground()
    {
        return foregroundColorProperty != null;
    }

    @RequiredOverride
    public boolean hasBorder()
    {
        return border() != BorderPaint.EMPTY;
    }

    @RequiredOverride
    public boolean hasOutline()
    {
        return outline() != OutlinePaint.EMPTY;
    }

    public boolean hasBorderImage()
    {
        return hasBorder() && (border().image() != null && border().image() != Texture.EMPTY);
    }

    @RequiredOverride
    public Property<Texture> backgroundTextureProperty()
    {
        if (backgroundTextureProperty == null)
            backgroundTextureProperty = createRenderProperty(Texture.EMPTY);
        return backgroundTextureProperty;
    }

    @RequiredOverride
    public Property<SpriteRepeat> backgroundRepeatProperty()
    {
        if (backgroundRepeatProperty == null)
            backgroundRepeatProperty = createRenderProperty(SpriteRepeat.NONE);
        return backgroundRepeatProperty;
    }

    @RequiredOverride
    public Property<Resource> backgroundAnimationResourceProperty()
    {
        if (backgroundAnimationProperty == null)
            backgroundAnimationProperty = createRenderProperty(null);
        return backgroundAnimationProperty;
    }

    @RequiredOverride
    public Property<RectBox> backgroundPositionProperty()
    {
        if (backgroundPositionProperty == null)
            backgroundPositionProperty = createRenderProperty(RectBox.EMPTY);
        return backgroundPositionProperty;
    }

    @RequiredOverride
    public Property<RandomSpriteRotation> backgroundRandomRotationProperty()
    {
        if (backgroundRotationProperty == null)
            backgroundRotationProperty = createRenderProperty(null);
        return backgroundRotationProperty;
    }

    @RequiredOverride
    public Property<Color> backgroundColorProperty()
    {
        if (backgroundColorProperty == null)
            backgroundColorProperty = createRenderProperty(Color.ALPHA);
        return backgroundColorProperty;
    }

    @RequiredOverride
    public Property<Texture> foregroundTextureProperty()
    {
        if (foregroundTextureProperty == null)
            foregroundTextureProperty = createRenderProperty(Texture.EMPTY);
        return foregroundTextureProperty;
    }

    @RequiredOverride
    public Property<SpriteRepeat> foregroundRepeatProperty()
    {
        if (foregroundRepeatProperty == null)
            foregroundRepeatProperty = createRenderProperty(SpriteRepeat.NONE);
        return foregroundRepeatProperty;
    }

    @RequiredOverride
    public Property<Resource> foregroundAnimationResourceProperty()
    {
        if (foregroundAnimationProperty == null)
            foregroundAnimationProperty = createRenderProperty(null);
        return foregroundAnimationProperty;
    }

    @RequiredOverride
    public Property<RectBox> foregroundPositionProperty()
    {
        if (foregroundPositionProperty == null)
            foregroundPositionProperty = createRenderProperty(RectBox.EMPTY);
        return foregroundPositionProperty;
    }

    @RequiredOverride
    public Property<RandomSpriteRotation> foregroundRandomRotationProperty()
    {
        if (foregroundRotationProperty == null)
            foregroundRotationProperty = createRenderProperty(null);
        return foregroundRotationProperty;
    }

    @RequiredOverride
    public Property<Color> foregroundColorProperty()
    {
        if (foregroundColorProperty == null)
            foregroundColorProperty = createRenderProperty(Color.ALPHA);
        return foregroundColorProperty;
    }

    @RequiredOverride
    public Property<Float> outlineWidthLeftProperty()
    {
        if (outlineWidthLeftProperty == null)
            outlineWidthLeftProperty = createRenderProperty(1F);
        return outlineWidthLeftProperty;
    }

    @RequiredOverride
    public Property<Float> outlineWidthRightProperty()
    {
        if (outlineWidthRightProperty == null)
            outlineWidthRightProperty = createRenderProperty(1F);
        return outlineWidthRightProperty;
    }

    @RequiredOverride
    public Property<Float> outlineWidthTopProperty()
    {
        if (outlineWidthTopProperty == null)
            outlineWidthTopProperty = createRenderProperty(1F);
        return outlineWidthTopProperty;
    }

    @RequiredOverride
    public Property<Float> outlineWidthBottomProperty()
    {
        if (outlineWidthBottomProperty == null)
            outlineWidthBottomProperty = createRenderProperty(1F);
        return outlineWidthBottomProperty;
    }

    @RequiredOverride
    public Property<Integer> outlineRadiusTopLeftProperty()
    {
        if (outlineRadiusTopLeftProperty == null)
            outlineRadiusTopLeftProperty = createRenderProperty(0);
        return outlineRadiusTopLeftProperty;
    }

    @RequiredOverride
    public Property<Integer> outlineRadiusTopRightProperty()
    {
        if (outlineRadiusTopRightProperty == null)
            outlineRadiusTopRightProperty = createRenderProperty(0);
        return outlineRadiusTopRightProperty;
    }

    @RequiredOverride
    public Property<Integer> outlineRadiusBottomLeftProperty()
    {
        if (outlineRadiusBottomLeftProperty == null)
            outlineRadiusBottomLeftProperty = createRenderProperty(0);
        return outlineRadiusBottomLeftProperty;
    }

    @RequiredOverride
    public Property<Integer> outlineRadiusBottomRightProperty()
    {
        if (outlineRadiusBottomRightProperty == null)
            outlineRadiusBottomRightProperty = createRenderProperty(0);
        return outlineRadiusBottomRightProperty;
    }

    @RequiredOverride
    public Property<FillMethod> fillMethodProperty()
    {
        if (fillMethodProperty == null)
            fillMethodProperty = createRenderProperty(FillMethod.HORIZONTAL);
        return fillMethodProperty;
    }

    @RequiredOverride
    public Property<Float> fillAmountProperty()
    {
        if (fillAmountProperty == null)
            fillAmountProperty = createRenderProperty(1F);
        return fillAmountProperty;
    }

    ////////////
    // VALUES //
    ////////////

    @RequiredOverride
    public Texture backgroundTexture()
    {
        return backgroundTextureProperty().getValue();
    }

    @RequiredOverride
    public void backgroundTexture(Texture texture)
    {
        backgroundTextureProperty().setValue(texture);
    }

    @RequiredOverride
    public SpriteRepeat backgroundRepeat()
    {
        return backgroundRepeatProperty().getValue();
    }

    @RequiredOverride
    public void backgroundRepeat(SpriteRepeat spriteRepeat)
    {
        backgroundRepeatProperty().setValue(spriteRepeat);
    }

    @RequiredOverride
    public Resource backgroundAnimationResource()
    {
        return backgroundAnimationResourceProperty().getValue();
    }

    @RequiredOverride
    public void backgroundAnimationResource(Resource resource)
    {
        backgroundAnimationResourceProperty().setValue(resource);
    }

    @RequiredOverride
    public RectBox backgroundPosition()
    {
        return backgroundPositionProperty().getValue();
    }

    @RequiredOverride
    public void backgroundPosition(RectBox position)
    {
        backgroundPositionProperty().setValue(position);
    }

    @RequiredOverride
    public RandomSpriteRotation backgroundRandomRotation()
    {
        return backgroundRandomRotationProperty().getValue();
    }

    @RequiredOverride
    public void backgroundRandomRotation(RandomSpriteRotation rotation)
    {
        backgroundRandomRotationProperty().setValue(rotation);
    }

    public SpriteAnimationInstance backgroundAnimation()
    {
        return backgroundAnimation;
    }

    public void backgroundAnimation(SpriteAnimationInstance backgroundAnimation)
    {
        this.backgroundAnimation = backgroundAnimation;
    }

    public SpriteRotation[] backgroundRotationArray()
    {
        return backgroundRotationArray;
    }

    public void backgroundRotationArray(SpriteRotation[] backgroundRotationArray)
    {
        this.backgroundRotationArray = backgroundRotationArray;
    }

    public SpriteRotation backgroundRotation()
    {
        return backgroundRotationArray == null ? SpriteRotation.NONE : backgroundRotationArray[0];
    }

    public void backgroundRotation(SpriteRotation rotation)
    {
        if (backgroundRotationArray == null)
            backgroundRotationArray = new SpriteRotation[] { rotation };
        else
            backgroundRotationArray[0] = rotation;
    }

    @RequiredOverride
    public Color backgroundColor()
    {
        return backgroundColorProperty().getValue();
    }

    @RequiredOverride
    public void backgroundColor(Color color)
    {
        backgroundColorProperty().setValue(color);
    }

    @RequiredOverride
    public Texture foregroundTexture()
    {
        return foregroundTextureProperty().getValue();
    }

    @RequiredOverride
    public void foregroundTexture(Texture texture)
    {
        foregroundTextureProperty().setValue(texture);
    }

    @RequiredOverride
    public SpriteRepeat foregroundRepeat()
    {
        return foregroundRepeatProperty().getValue();
    }

    @RequiredOverride
    public void foregroundRepeat(SpriteRepeat spriteRepeat)
    {
        foregroundRepeatProperty().setValue(spriteRepeat);
    }

    @RequiredOverride
    public Resource foregroundAnimationResource()
    {
        return foregroundAnimationResourceProperty().getValue();
    }

    @RequiredOverride
    public void foregroundAnimationResource(Resource resource)
    {
        foregroundAnimationResourceProperty().setValue(resource);
    }

    @RequiredOverride
    public RectBox foregroundPosition()
    {
        return foregroundPositionProperty().getValue();
    }

    @RequiredOverride
    public void foregroundPosition(RectBox position)
    {
        foregroundPositionProperty().setValue(position);
    }

    @RequiredOverride
    public RandomSpriteRotation foregroundRandomRotation()
    {
        return foregroundRandomRotationProperty().getValue();
    }

    @RequiredOverride
    public void foregroundRandomRotation(RandomSpriteRotation rotation)
    {
        foregroundRandomRotationProperty().setValue(rotation);
    }

    public SpriteAnimationInstance foregroundAnimation()
    {
        return foregroundAnimation;
    }

    public void foregroundAnimation(SpriteAnimationInstance foregroundAnimation)
    {
        this.foregroundAnimation = foregroundAnimation;
    }

    public SpriteRotation[] foregroundRotationArray()
    {
        return foregroundRotationArray;
    }

    public void foregroundRotationArray(SpriteRotation[] foregroundRotationArray)
    {
        this.foregroundRotationArray = foregroundRotationArray;
    }

    public SpriteRotation foregroundRotation()
    {
        return foregroundRotationArray == null ? SpriteRotation.NONE : foregroundRotationArray[0];
    }

    public void foregroundRotation(SpriteRotation rotation)
    {
        if (foregroundRotationArray == null)
            foregroundRotationArray = new SpriteRotation[] { rotation };
        else
            foregroundRotationArray[0] = rotation;
    }

    @RequiredOverride
    public Color foregroundColor()
    {
        return foregroundColorProperty().getValue();
    }

    @RequiredOverride
    public void foregroundColor(Color color)
    {
        foregroundColorProperty().setValue(color);
    }

    public BorderPaint border()
    {
        return border;
    }

    public OutlinePaint outline()
    {
        return outline;
    }

    @RequiredOverride
    public float outlineWidth()
    {
        return outlineWidth(RectSide.UP);
    }

    @RequiredOverride
    public float outlineWidth(RectSide side)
    {
        return switch (side)
                {
                    case UP -> outlineWidthTopProperty().getValue();
                    case DOWN -> outlineWidthBottomProperty().getValue();
                    case LEFT -> outlineWidthLeftProperty().getValue();
                    case RIGHT -> outlineWidthRightProperty().getValue();
                };
    }

    @RequiredOverride
    public void outlineWidth(float width)
    {
        outlineWidthTopProperty().setValue(width);
        outlineWidthBottomProperty().setValue(width);
        outlineWidthLeftProperty().setValue(width);
        outlineWidthRightProperty().setValue(width);
    }

    @RequiredOverride
    public void outlineWidth(RectSide side, float width)
    {
        switch (side)
        {
            case UP -> outlineWidthTopProperty().setValue(width);
            case DOWN -> outlineWidthBottomProperty().setValue(width);
            case LEFT -> outlineWidthLeftProperty().setValue(width);
            case RIGHT -> outlineWidthRightProperty().setValue(width);
        }
    }

    @RequiredOverride
    public int outlineRadius(RectCorner corner)
    {
        return switch (corner)
                {
                    case TOP_LEFT -> outlineRadiusTopLeftProperty().getValue();
                    case TOP_RIGHT -> outlineRadiusTopRightProperty().getValue();
                    case BOTTOM_LEFT -> outlineRadiusBottomLeftProperty().getValue();
                    case BOTTOM_RIGHT -> outlineRadiusBottomRightProperty().getValue();
                };
    }

    @RequiredOverride
    public void outlineRadius(RectCorner corner, int width)
    {
        switch (corner)
        {
            case TOP_LEFT -> outlineRadiusTopLeftProperty().setValue(width);
            case TOP_RIGHT -> outlineRadiusTopRightProperty().setValue(width);
            case BOTTOM_LEFT -> outlineRadiusBottomLeftProperty().setValue(width);
            case BOTTOM_RIGHT -> outlineRadiusBottomRightProperty().setValue(width);
        }
    }

    @RequiredOverride
    public FillMethod fillMethod()
    {
        return fillMethodProperty().getValue();
    }

    @RequiredOverride
    public void fillMethod(FillMethod fillMethod)
    {
        fillMethodProperty().setValue(fillMethod);
    }

    @RequiredOverride
    public float fillAmount()
    {
        return fillAmountProperty().getValue();
    }

    @RequiredOverride
    public void fillAmount(float fillAmount)
    {
        fillAmountProperty().setValue(fillAmount);
    }
}
