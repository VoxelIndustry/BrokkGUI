package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.border.ColorBorderDrawer;
import net.voxelindustry.brokkgui.border.ImageBorderDrawer;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.RenderComponent;
import net.voxelindustry.brokkgui.component.RequiredOverride;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.data.Resource;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;
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

    protected Property<Color> borderColorProperty;

    protected Property<Float> borderWidthLeftProperty;
    protected Property<Float> borderWidthRightProperty;
    protected Property<Float> borderWidthTopProperty;
    protected Property<Float> borderWidthBottomProperty;

    protected Property<Integer> borderRadiusTopLeftProperty;
    protected Property<Integer> borderRadiusTopRightProperty;
    protected Property<Integer> borderRadiusBottomLeftProperty;
    protected Property<Integer> borderRadiusBottomRightProperty;

    protected Property<Texture> borderImageProperty;
    protected Property<RectBox> borderImageSliceProperty;
    protected Property<RectBox> borderImageWidthProperty;
    protected Property<RectBox> borderImageOutsetProperty;

    protected Property<Boolean> borderImageFillProperty;

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
    public void renderContent(IRenderCommandReceiver renderer, RenderPass pass, int mouseX, int mouseY)
    {
        float x = element().transform().leftPos();
        float y = element().transform().topPos();
        float z = element().transform().zLevel();

        if (pass == RenderPass.BACKGROUND)
        {
            if (hasTextureBackground() && backgroundTexture() != Texture.EMPTY)
            {
                SpriteBackgroundDrawer.drawBackground(this, renderer);
            }
            if (hasColorBackground() && backgroundColor().getAlpha() != 0)
            {
                Color background = backgroundColor();

                shape.drawColored(element().transform(), renderer, x, y, background, z, backgroundPosition());
            }

            if (hasBorder())
            {
                if (hasBorderImage())
                    ImageBorderDrawer.drawBorder(this, renderer);
                else
                    ColorBorderDrawer.drawBorder(this, renderer);
            }
        }
        else if (pass == RenderPass.FOREGROUND)
        {
            if (hasTextureForeground() && foregroundTexture() != Texture.EMPTY)
            {
                SpriteBackgroundDrawer.drawForeground(this, renderer);
            }
            if (hasColorForeground() && foregroundColor().getAlpha() != 0)
            {
                Color foreground = foregroundColor();

                shape.drawColored(element().transform(), renderer, x, y, foreground, z, foregroundPosition());
            }
        }
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
        return borderColorProperty != null;
    }

    @RequiredOverride
    public boolean hasBorderImage()
    {
        return borderImageProperty != null;
    }

    @RequiredOverride
    public Property<Texture> backgroundTextureProperty()
    {
        if (backgroundTextureProperty == null)
            backgroundTextureProperty = new Property<>(Texture.EMPTY);
        return backgroundTextureProperty;
    }

    @RequiredOverride
    public Property<SpriteRepeat> backgroundRepeatProperty()
    {
        if (backgroundRepeatProperty == null)
            backgroundRepeatProperty = new Property<>(SpriteRepeat.NONE);
        return backgroundRepeatProperty;
    }

    @RequiredOverride
    public Property<Resource> backgroundAnimationResourceProperty()
    {
        if (backgroundAnimationProperty == null)
            backgroundAnimationProperty = new Property<>(null);
        return backgroundAnimationProperty;
    }

    @RequiredOverride
    public Property<RectBox> backgroundPositionProperty()
    {
        if (backgroundPositionProperty == null)
            backgroundPositionProperty = new Property<>(RectBox.EMPTY);
        return backgroundPositionProperty;
    }

    @RequiredOverride
    public Property<RandomSpriteRotation> backgroundRandomRotationProperty()
    {
        if (backgroundRotationProperty == null)
            backgroundRotationProperty = new Property<>(null);
        return backgroundRotationProperty;
    }

    @RequiredOverride
    public Property<Color> backgroundColorProperty()
    {
        if (backgroundColorProperty == null)
            backgroundColorProperty = new Property<>(Color.ALPHA);
        return backgroundColorProperty;
    }

    @RequiredOverride
    public Property<Texture> foregroundTextureProperty()
    {
        if (foregroundTextureProperty == null)
            foregroundTextureProperty = new Property<>(Texture.EMPTY);
        return foregroundTextureProperty;
    }

    @RequiredOverride
    public Property<SpriteRepeat> foregroundRepeatProperty()
    {
        if (foregroundRepeatProperty == null)
            foregroundRepeatProperty = new Property<>(SpriteRepeat.NONE);
        return foregroundRepeatProperty;
    }

    @RequiredOverride
    public Property<Resource> foregroundAnimationResourceProperty()
    {
        if (foregroundAnimationProperty == null)
            foregroundAnimationProperty = new Property<>(null);
        return foregroundAnimationProperty;
    }

    @RequiredOverride
    public Property<RectBox> foregroundPositionProperty()
    {
        if (foregroundPositionProperty == null)
            foregroundPositionProperty = new Property<>(RectBox.EMPTY);
        return foregroundPositionProperty;
    }

    @RequiredOverride
    public Property<RandomSpriteRotation> foregroundRandomRotationProperty()
    {
        if (foregroundRotationProperty == null)
            foregroundRotationProperty = new Property<>(null);
        return foregroundRotationProperty;
    }

    @RequiredOverride
    public Property<Color> foregroundColorProperty()
    {
        if (foregroundColorProperty == null)
            foregroundColorProperty = new Property<>(Color.ALPHA);
        return foregroundColorProperty;
    }

    @RequiredOverride
    public Property<Color> borderColorProperty()
    {
        if (borderColorProperty == null)
            borderColorProperty = new Property<>(Color.ALPHA);
        return borderColorProperty;
    }

    @RequiredOverride
    public Property<Float> borderWidthLeftProperty()
    {
        if (borderWidthLeftProperty == null)
            borderWidthLeftProperty = new Property<>(1F);
        return borderWidthLeftProperty;
    }

    @RequiredOverride
    public Property<Float> borderWidthRightProperty()
    {
        if (borderWidthRightProperty == null)
            borderWidthRightProperty = new Property<>(1F);
        return borderWidthRightProperty;
    }

    @RequiredOverride
    public Property<Float> borderWidthTopProperty()
    {
        if (borderWidthTopProperty == null)
            borderWidthTopProperty = new Property<>(1F);
        return borderWidthTopProperty;
    }

    @RequiredOverride
    public Property<Float> borderWidthBottomProperty()
    {
        if (borderWidthBottomProperty == null)
            borderWidthBottomProperty = new Property<>(1F);
        return borderWidthBottomProperty;
    }

    @RequiredOverride
    public Property<Integer> borderRadiusTopLeftProperty()
    {
        if (borderRadiusTopLeftProperty == null)
            borderRadiusTopLeftProperty = new Property<>(0);
        return borderRadiusTopLeftProperty;
    }

    @RequiredOverride
    public Property<Integer> borderRadiusTopRightProperty()
    {
        if (borderRadiusTopRightProperty == null)
            borderRadiusTopRightProperty = new Property<>(0);
        return borderRadiusTopRightProperty;
    }

    @RequiredOverride
    public Property<Integer> borderRadiusBottomLeftProperty()
    {
        if (borderRadiusBottomLeftProperty == null)
            borderRadiusBottomLeftProperty = new Property<>(0);
        return borderRadiusBottomLeftProperty;
    }

    @RequiredOverride
    public Property<Integer> borderRadiusBottomRightProperty()
    {
        if (borderRadiusBottomRightProperty == null)
            borderRadiusBottomRightProperty = new Property<>(0);
        return borderRadiusBottomRightProperty;
    }

    @RequiredOverride
    public Property<Texture> borderImageProperty()
    {
        if (borderImageProperty == null)
            borderImageProperty = new Property<>(Texture.EMPTY);
        return borderImageProperty;
    }

    @RequiredOverride
    public Property<RectBox> borderImageSliceProperty()
    {
        if (borderImageSliceProperty == null)
            borderImageSliceProperty = new Property<>(RectBox.build().all(1).create());
        return borderImageSliceProperty;
    }

    @RequiredOverride
    public Property<RectBox> borderImageWidthProperty()
    {
        if (borderImageWidthProperty == null)
            borderImageWidthProperty = new Property<>(RectBox.build().all(1).create());
        return borderImageWidthProperty;
    }

    @RequiredOverride
    public Property<RectBox> borderImageOutsetProperty()
    {
        if (borderImageOutsetProperty == null)
            borderImageOutsetProperty = new Property<>(RectBox.build().all(0).create());
        return borderImageOutsetProperty;
    }

    @RequiredOverride
    public Property<Boolean> borderImageFillProperty()
    {
        if (borderImageFillProperty == null)
            borderImageFillProperty = new Property<>(Boolean.FALSE);
        return borderImageFillProperty;
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
            backgroundRotationArray = new SpriteRotation[]{rotation};
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
            foregroundRotationArray = new SpriteRotation[]{rotation};
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

    @RequiredOverride
    public Color borderColor()
    {
        return borderColorProperty().getValue();
    }

    @RequiredOverride
    public void borderColor(Color color)
    {
        borderColorProperty().setValue(color);
    }

    @RequiredOverride
    public float borderWidth()
    {
        return borderWidth(RectSide.UP);
    }

    @RequiredOverride
    public float borderWidth(RectSide side)
    {
        switch (side)
        {
            case UP:
                return borderWidthTopProperty().getValue();
            case DOWN:
                return borderWidthBottomProperty().getValue();
            case LEFT:
                return borderWidthLeftProperty().getValue();
            case RIGHT:
                return borderWidthRightProperty().getValue();
            default:
                return 0;
        }
    }

    @RequiredOverride
    public void borderWidth(RectSide side, float width)
    {
        switch (side)
        {
            case UP:
                borderWidthTopProperty().setValue(width);
                break;
            case DOWN:
                borderWidthBottomProperty().setValue(width);
                break;
            case LEFT:
                borderWidthLeftProperty().setValue(width);
                break;
            case RIGHT:
                borderWidthRightProperty().setValue(width);
                break;
        }
    }

    @RequiredOverride
    public int borderRadius(RectCorner corner)
    {
        switch (corner)
        {
            case TOP_LEFT:
                return borderRadiusTopLeftProperty().getValue();
            case TOP_RIGHT:
                return borderRadiusTopRightProperty().getValue();
            case BOTTOM_LEFT:
                return borderRadiusBottomLeftProperty().getValue();
            case BOTTOM_RIGHT:
                return borderRadiusBottomRightProperty().getValue();
            default:
                return 0;
        }
    }

    @RequiredOverride
    public void borderRadius(RectCorner corner, int width)
    {
        switch (corner)
        {
            case TOP_LEFT:
                borderRadiusTopLeftProperty().setValue(width);
                break;
            case TOP_RIGHT:
                borderRadiusTopRightProperty().setValue(width);
                break;
            case BOTTOM_LEFT:
                borderRadiusBottomLeftProperty().setValue(width);
                break;
            case BOTTOM_RIGHT:
                borderRadiusBottomRightProperty().setValue(width);
                break;
        }
    }

    @RequiredOverride
    public Texture borderImage()
    {
        return borderImageProperty().getValue();
    }

    @RequiredOverride
    public void borderImage(Texture texture)
    {
        borderImageProperty().setValue(texture);
    }

    @RequiredOverride
    public RectBox borderImageSlice()
    {
        return borderImageSliceProperty().getValue();
    }

    @RequiredOverride
    public void borderImageSlice(RectBox box)
    {
        borderImageSliceProperty().setValue(box);
    }

    @RequiredOverride
    public RectBox borderImageWidth()
    {
        return borderImageWidthProperty().getValue();
    }

    @RequiredOverride
    public void borderImageWidth(RectBox box)
    {
        borderImageWidthProperty().setValue(box);
    }

    @RequiredOverride
    public RectBox borderImageOutset()
    {
        return borderImageOutsetProperty().getValue();
    }

    @RequiredOverride
    public void borderImageOutset(RectBox box)
    {
        borderImageOutsetProperty().setValue(box);
    }

    @RequiredOverride
    public boolean borderImageFill()
    {
        return borderImageFillProperty().getValue();
    }

    @RequiredOverride
    public void borderImageFill(boolean doFill)
    {
        borderImageFillProperty().setValue(doFill);
    }
}
