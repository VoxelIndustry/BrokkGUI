package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.binding.Expression;
import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.property.specific.FloatProperty;
import fr.ourten.teabeans.value.Observable;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.RequiredOverride;
import net.voxelindustry.brokkgui.data.Position;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.data.Rotation;
import net.voxelindustry.brokkgui.data.Scale;
import net.voxelindustry.brokkgui.event.TransformLayoutEvent;
import net.voxelindustry.brokkgui.text.GuiOverflow;
import net.voxelindustry.brokkgui.util.MouseInBoundsChecker;

import java.util.List;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Transform extends GuiComponent
{
    private final FloatProperty xPosProperty;
    private final FloatProperty yPosProperty;
    private final FloatProperty xTranslateProperty;
    private final FloatProperty yTranslateProperty;

    private final FloatProperty xOffsetProperty;
    private final FloatProperty yOffsetProperty;

    private final FloatProperty     widthProperty;
    private final FloatProperty     heightProperty;
    private final FloatProperty     widthRatioProperty;
    private final FloatProperty     heightRatioProperty;
    private final Expression<Float> zDepthProperty;
    private final FloatProperty     zTranslateProperty;

    protected Property<Float>   borderWidthLeftProperty;
    protected Property<Float>   borderWidthRightProperty;
    protected Property<Float>   borderWidthTopProperty;
    protected Property<Float>   borderWidthBottomProperty;
    protected Property<Integer> borderRadiusTopLeftProperty;
    protected Property<Integer> borderRadiusTopRightProperty;
    protected Property<Integer> borderRadiusBottomLeftProperty;
    protected Property<Integer> borderRadiusBottomRightProperty;

    private final Property<GuiOverflow> overflowProperty = createRenderProperty(GuiOverflow.VISIBLE);

    private final Property<Rotation> rotationProperty;

    private final Property<Scale> scaleProperty;

    private final Property<Transform>     parentProperty;
    private final ListProperty<Transform> childrenListProperty;

    private MouseInBoundsChecker mouseInBoundsChecker;

    private boolean bindChild = true;

    public Transform()
    {
        xPosProperty = createRenderPropertyFloat(0F);
        yPosProperty = createRenderPropertyFloat(0F);

        xTranslateProperty = createRenderPropertyFloat(0F);
        yTranslateProperty = createRenderPropertyFloat(0F);

        xOffsetProperty = createRenderPropertyFloat(0F);
        yOffsetProperty = createRenderPropertyFloat(0F);

        widthProperty = createRenderPropertyFloat(0F);
        heightProperty = createRenderPropertyFloat(0F);

        widthRatioProperty = new FloatProperty(-1F);
        heightRatioProperty = new FloatProperty(-1F);

        zDepthProperty = new Expression<>(() ->
        {
            element().markRenderDirty();
            return parentProperty().isPresent() ? parent().zLevel() : 0;
        });
        zTranslateProperty = createRenderPropertyFloat(1F);

        rotationProperty = createRenderProperty(Rotation.NONE);
        scaleProperty = createRenderProperty(null);

        parentProperty = new Property<>(null);
        childrenListProperty = new ListProperty<>(null);

        childrenListProperty.addChangeListener((ListValueChangeListener<Transform>) (obs, oldValue, newValue) ->
        {
            if (newValue != null)
                newValue.setParent(this);
            if (oldValue != null)
                oldValue.setParent(null);
        });

        mouseInBoundsChecker = MouseInBoundsChecker.DEFAULT;

        widthProperty().addChangeListener(this::notifyParentOfLayoutChange);
        heightProperty().addChangeListener(this::notifyParentOfLayoutChange);
        xPosProperty().addChangeListener(this::notifyParentOfLayoutChange);
        yPosProperty().addChangeListener(this::notifyParentOfLayoutChange);
        xTranslateProperty().addChangeListener(this::notifyParentOfLayoutChange);
        yTranslateProperty().addChangeListener(this::notifyParentOfLayoutChange);

        overflowProperty().addChangeListener((obs, oldValue, newValue) ->
        {
            if (newValue == GuiOverflow.SCROLL)
                element().provide(Scrollable.class);
            else if (oldValue == GuiOverflow.SCROLL)
                element().remove(Scrollable.class);
        });
    }

    ///////////////
    // HIERARCHY //
    ///////////////

    public Property<Transform> parentProperty()
    {
        return parentProperty;
    }

    public Transform parent()
    {
        return parentProperty().getValue();
    }

    /**
     * Internal method for the layout system. A developer should never have to
     * use it.
     *
     * @param parent transform
     */
    public void setParent(Transform parent)
    {
        parentProperty().setValue(parent);

        if (parent != null)
        {
            if (widthRatio() != -1)
                RelativeBindingHelper.bindWidthRelative(this, parent, widthRatioProperty());
            if (heightRatio() != -1)
                RelativeBindingHelper.bindHeightRelative(this, parent, heightRatioProperty());

            zDepthProperty.getDependencies().clear();
            zDepthProperty.getDependencies().add(parent.zDepthExpression());
            zDepthProperty.getDependencies().add(parent.zTranslateProperty());

            element().setWindow(parent.element().getWindow());
            parent.notifyOfLayoutChange(this);
        }
        else
            element().setWindow(null);
    }

    public void notifyParentOfLayoutChange(Observable observable)
    {
        if (parent() != null)
            parent().notifyOfLayoutChange(this);
    }

    public void notifyOfLayoutChange(Transform child)
    {
        getEventDispatcher().singletonQueue().dispatch(TransformLayoutEvent.TYPE, new TransformLayoutEvent(this, child));
    }

    /**
     * @return an immutable list
     */
    public List<Transform> children()
    {
        return childrenProperty().getValue();
    }

    public ListProperty<Transform> childrenProperty()
    {
        return childrenListProperty;
    }

    public int childCount()
    {
        return childrenProperty().size();
    }

    public void addChild(Transform element)
    {
        childrenProperty().add(element);

        if (bindChild())
            RelativeBindingHelper.bindToPos(element, this);
    }

    public void addChildren(Transform... elements)
    {
        for (Transform element : elements)
            addChild(element);
    }

    public void removeChild(Transform element)
    {
        childrenProperty().remove(element);

        element.xPosProperty().unbind();
        element.yPosProperty().unbind();
    }

    public void clearChildren()
    {
        childrenProperty().getValue().forEach(element ->
        {
            element.xPosProperty().unbind();
            element.yPosProperty().unbind();
        });
        childrenProperty().clear();
    }

    public Stream<Transform> streamChildren()
    {
        return childrenProperty().getModifiableValue().stream();
    }

    public boolean hasChild(Transform element)
    {
        return childrenProperty().contains(element);
    }

    public boolean bindChild()
    {
        return bindChild;
    }

    public void bindChild(boolean bindChild)
    {
        this.bindChild = bindChild;
    }

    ////////////////
    // TRANSFORMS //
    ////////////////

    public boolean isPointInside(float pointX, float pointY)
    {
        return mouseInBoundsChecker().test(element(), pointX, pointY);
    }

    public MouseInBoundsChecker mouseInBoundsChecker()
    {
        return mouseInBoundsChecker;
    }

    public void mouseInBoundsChecker(MouseInBoundsChecker checker)
    {
        mouseInBoundsChecker = checker;
    }

    public Expression<Float> zDepthExpression()
    {
        return zDepthProperty;
    }

    public float zDepth()
    {
        return zDepthExpression().getValue();
    }

    /**
     * @return sum of the zDepth inherited property from the parent and the zTranslate offset of this child
     */
    public float zLevel()
    {
        return zDepthExpression().getValue() + zTranslate();
    }

    public FloatProperty zTranslateProperty()
    {
        return zTranslateProperty;
    }

    public float zTranslate()
    {
        return zTranslateProperty().getValue();
    }

    public void zTranslate(float zTranslate)
    {
        zTranslateProperty().setValue(zTranslate);
    }

    public FloatProperty xPosProperty()
    {
        return xPosProperty;
    }

    public FloatProperty yPosProperty()
    {
        return yPosProperty;
    }

    public FloatProperty xTranslateProperty()
    {
        return xTranslateProperty;
    }

    public FloatProperty yTranslateProperty()
    {
        return yTranslateProperty;
    }

    public FloatProperty xOffsetProperty()
    {
        return xOffsetProperty;
    }

    public FloatProperty yOffsetProperty()
    {
        return yOffsetProperty;
    }

    public FloatProperty widthProperty()
    {
        return widthProperty;
    }

    public FloatProperty heightProperty()
    {
        return heightProperty;
    }

    public FloatProperty widthRatioProperty()
    {
        return widthRatioProperty;
    }

    public FloatProperty heightRatioProperty()
    {
        return heightRatioProperty;
    }

    public Property<Rotation> rotationProperty()
    {
        return rotationProperty;
    }

    public Property<Scale> scaleProperty()
    {
        return scaleProperty;
    }

    public Property<GuiOverflow> overflowProperty()
    {
        return overflowProperty;
    }

    @RequiredOverride
    public Property<Float> borderWidthLeftProperty()
    {
        if (borderWidthLeftProperty == null)
            borderWidthLeftProperty = createRenderProperty(1F);
        return borderWidthLeftProperty;
    }

    @RequiredOverride
    public Property<Float> borderWidthRightProperty()
    {
        if (borderWidthRightProperty == null)
            borderWidthRightProperty = createRenderProperty(1F);
        return borderWidthRightProperty;
    }

    @RequiredOverride
    public Property<Float> borderWidthTopProperty()
    {
        if (borderWidthTopProperty == null)
            borderWidthTopProperty = createRenderProperty(1F);
        return borderWidthTopProperty;
    }

    @RequiredOverride
    public Property<Float> borderWidthBottomProperty()
    {
        if (borderWidthBottomProperty == null)
            borderWidthBottomProperty = createRenderProperty(1F);
        return borderWidthBottomProperty;
    }

    @RequiredOverride
    public Property<Integer> borderRadiusTopLeftProperty()
    {
        if (borderRadiusTopLeftProperty == null)
            borderRadiusTopLeftProperty = createRenderProperty(0);
        return borderRadiusTopLeftProperty;
    }

    @RequiredOverride
    public Property<Integer> borderRadiusTopRightProperty()
    {
        if (borderRadiusTopRightProperty == null)
            borderRadiusTopRightProperty = createRenderProperty(0);
        return borderRadiusTopRightProperty;
    }

    @RequiredOverride
    public Property<Integer> borderRadiusBottomLeftProperty()
    {
        if (borderRadiusBottomLeftProperty == null)
            borderRadiusBottomLeftProperty = createRenderProperty(0);
        return borderRadiusBottomLeftProperty;
    }

    @RequiredOverride
    public Property<Integer> borderRadiusBottomRightProperty()
    {
        if (borderRadiusBottomRightProperty == null)
            borderRadiusBottomRightProperty = createRenderProperty(0);
        return borderRadiusBottomRightProperty;
    }

    /**
     * @return xPos, used for layout management, do not attempt to change the
     * property outside the layout scope.
     */
    public float xPos()
    {
        return xPosProperty().getValue();
    }

    /**
     * @return yPos, used for layout management, do not attempt to change the
     * property outside the layout scope.
     */
    public float yPos()
    {
        return yPosProperty().getValue();
    }

    /**
     * @return xTranslate, used for component positionning.
     */
    public float xTranslate()
    {
        return xTranslateProperty().getValue();
    }

    /**
     * Set the translation of this component to this specified value, usable outside
     * layout scope.
     *
     * @param xTranslate
     */
    public void xTranslate(float xTranslate)
    {
        xTranslateProperty().setValue(xTranslate);
    }

    /**
     * @return yTranslate, used for component positionning.
     */
    public float yTranslate()
    {
        return yTranslateProperty().getValue();
    }

    /**
     * Set the translation of this component to this specified value, usable outside
     * layout scope.
     *
     * @param yTranslate
     */
    public void yTranslate(float yTranslate)
    {
        yTranslateProperty().setValue(yTranslate);
    }

    /**
     * Helper method to set the translation in one call.
     * <p>
     * Set the translation of this component to this specified value, usable outside
     * layouting scope.
     *
     * @param xTranslate
     * @param yTranslate
     */
    public void translate(float xTranslate, float yTranslate)
    {
        xTranslate(xTranslate);
        yTranslate(yTranslate);
    }

    public float width()
    {
        return widthProperty().getValue();
    }

    public void width(float width)
    {
        if (widthProperty().isBound())
            widthProperty().unbind();
        widthProperty().setValue(width);
    }

    public float height()
    {
        return heightProperty().getValue();
    }

    public void height(float height)
    {
        if (heightProperty().isBound())
            heightProperty().unbind();
        heightProperty().setValue(height);
    }

    /**
     * Method computing the left-pos of this Node box (pos.x + translate.x)
     *
     * @return the computed non-cached value
     */
    public float leftPos()
    {
        return xPos() + xTranslate();
    }

    /**
     * Method computing the top-pos of this Node box (pos.y + translate.y)
     *
     * @return the computed non-cached value
     */
    public float topPos()
    {
        return yPos() + yTranslate();
    }

    /**
     * Method computing the right-pos of this Node box (pos.x + translate.x + width)
     *
     * @return the computed non-cached value
     */
    public float rightPos()
    {
        return xPos() + xTranslate() + width();
    }

    /**
     * Method computing the bottom-pos of this Node box (pos.y + translate.y + height)
     *
     * @return the computed non-cached value
     */
    public float bottomPos()
    {
        return yPos() + yTranslate() + height();
    }

    public float borderBoxLeft()
    {
        return leftPos() - borderWidth(RectSide.LEFT);
    }

    public float borderBoxTop()
    {
        return topPos() - borderWidth(RectSide.UP);
    }

    public float borderBoxBottom()
    {
        return bottomPos() + borderWidth(RectSide.DOWN);
    }

    public float borderBoxRight()
    {
        return rightPos() + borderWidth(RectSide.RIGHT);
    }

    /**
     * @return the most restrictive left pos of the clipBox between the left pos of this element borderbox or of its parent clipbox.
     * If overflow is visible then the parent clipbox is used. In case of a visible overflow and no parent the value -1 is returned.
     * The -1 special case is used to disable sending the clipBox command to the IRenderCommandReceiver
     */
    public float clipBoxLeft()
    {
        if (parentProperty().isPresent())
        {
            if (overflow() != GuiOverflow.VISIBLE)
                return max(parent().inheritedClipBoxLeft(), borderBoxLeft());

            return parent().inheritedClipBoxLeft();
        }
        if (overflow() != GuiOverflow.VISIBLE)
            return borderBoxLeft();
        return -1;
    }

    private float inheritedClipBoxLeft()
    {
        if (parentProperty().isPresent())
        {
            if (overflow() != GuiOverflow.VISIBLE)
                return max(parent().inheritedClipBoxLeft(), leftPos());

            return parent().inheritedClipBoxLeft();
        }
        if (overflow() != GuiOverflow.VISIBLE)
            return leftPos();
        return -1;
    }

    /**
     * @return the most restrictive top pos of the clipBox between the top pos of this element borderbox or of its parent clipbox.
     * If overflow is visible then the parent clipbox is used. In case of a visible overflow and no parent the value -1 is returned.
     * The -1 special case is used to disable sending the clipBox command to the IRenderCommandReceiver
     */
    public float clipBoxTop()
    {
        if (parentProperty().isPresent())
        {
            if (overflow() != GuiOverflow.VISIBLE)
                return max(parent().inheritedClipBoxTop(), borderBoxTop());

            return parent().inheritedClipBoxTop();
        }
        if (overflow() != GuiOverflow.VISIBLE)
            return borderBoxTop();
        return -1;
    }

    private float inheritedClipBoxTop()
    {
        if (parentProperty().isPresent())
        {
            if (overflow() != GuiOverflow.VISIBLE)
                return max(parent().inheritedClipBoxTop(), topPos());

            return parent().inheritedClipBoxTop();
        }
        if (overflow() != GuiOverflow.VISIBLE)
            return topPos();
        return -1;
    }

    /**
     * @return the most restrictive right pos of the clipBox between the right pos of this element borderbox or of its parent clipbox.
     * If overflow is visible then the parent clipbox is used. In case of a visible overflow and no parent the value -1 is returned.
     * The -1 special case is used to disable sending the clipBox command to the IRenderCommandReceiver
     */
    public float clipBoxRight()
    {
        if (parentProperty().isPresent())
        {
            if (overflow() != GuiOverflow.VISIBLE)
                return min(parent().inheritedClipBoxRight(), borderBoxRight());

            return parent().inheritedClipBoxRight();
        }
        if (overflow() != GuiOverflow.VISIBLE)
            return borderBoxRight();
        return Float.MAX_VALUE;
    }

    private float inheritedClipBoxRight()
    {
        if (parentProperty().isPresent())
        {
            if (overflow() != GuiOverflow.VISIBLE)
                return min(parent().inheritedClipBoxRight(), rightPos());

            return parent().inheritedClipBoxRight();
        }
        if (overflow() != GuiOverflow.VISIBLE)
            return rightPos();
        return Float.MAX_VALUE;
    }

    /**
     * @return the most restrictive bottom pos of the clipBox between the bottom pos of this element borderbox or of its parent clipbox.
     * If overflow is visible then the parent clipbox is used. In case of a visible overflow and no parent the value -1 is returned.
     * The -1 special case is used to disable sending the clipBox command to the IRenderCommandReceiver
     */
    public float clipBoxBottom()
    {
        if (parentProperty().isPresent())
        {
            if (overflow() != GuiOverflow.VISIBLE)
                return min(parent().inheritedClipBoxBottom(), borderBoxBottom());

            return parent().inheritedClipBoxBottom();
        }
        if (overflow() != GuiOverflow.VISIBLE)
            return borderBoxBottom();
        return Float.MAX_VALUE;
    }

    private float inheritedClipBoxBottom()
    {
        if (parentProperty().isPresent())
        {
            if (overflow() != GuiOverflow.VISIBLE)
                return min(parent().inheritedClipBoxBottom(), bottomPos());

            return parent().inheritedClipBoxBottom();
        }
        if (overflow() != GuiOverflow.VISIBLE)
            return bottomPos();
        return Float.MAX_VALUE;
    }

    /**
     * Helper method to set the width and height of the Node in one call.
     *
     * @param width
     * @param height
     */
    public void size(float width, float height)
    {
        width(width);
        height(height);
    }

    public float widthRatio()
    {
        return widthRatioProperty().getValue();
    }

    /**
     * Allow to set the width of the node relatively to the width of his parentProperty. Use
     * absolute setWidth to cancel the binding or set the width ratio to -1
     *
     * @param ratio
     */
    public void widthRatio(float ratio)
    {
        if (ratio == -1)
            widthProperty().unbind();
        else if (!widthProperty().isBound() && parent() != null)
            RelativeBindingHelper.bindWidthRelative(this, parent(), widthRatioProperty());
        widthRatioProperty().setValue(ratio);
    }

    public float heightRatio()
    {
        return heightRatioProperty().getValue();
    }

    /**
     * Allow to set the height of the node relatively to the height of his parentProperty.
     * Use absolute setHeight to cancel the binding or set the height ratio to -1
     *
     * @param ratio
     */
    public void heightRatio(float ratio)
    {
        if (ratio == -1)
            heightProperty().unbind();
        else if (!heightProperty().isBound() && parent() != null)
            RelativeBindingHelper.bindHeightRelative(this, parent(), heightRatioProperty());
        heightRatioProperty().setValue(ratio);
    }

    /**
     * Helper method to set the width ratio and height ratio in one call.
     *
     * @param widthRatio
     * @param heightRatio
     */
    public void sizeRatio(float widthRatio, float heightRatio)
    {
        widthRatio(widthRatio);
        heightRatio(heightRatio);
    }

    public Rotation rotation()
    {
        return rotationProperty().getValue();
    }

    public void rotate(Rotation rotation)
    {
        rotationProperty().setValue(rotation);
    }

    public Scale scale()
    {
        return scaleProperty().getValue();
    }

    public void scale(Scale scale)
    {
        scaleProperty().setValue(scale);
    }

    public float scaleX()
    {
        if (scaleProperty().isPresent())
            return scaleProperty().getValue().getX();
        return 1;
    }

    public void scaleX(float scaleX)
    {
        if (scaleProperty().isPresent())
            scale(new Scale(scaleX, scaleY(), scaleZ(), scalePivot()));
        else
            scale(new Scale(scaleX, 0, 0, null));
    }

    public float scaleY()
    {
        if (scaleProperty().isPresent())

            return scaleProperty().getValue().getY();
        return 1;
    }

    public void scaleY(float scaleY)
    {
        if (scaleProperty().isPresent())
            scale(new Scale(scaleX(), scaleY, scaleZ(), scalePivot()));
        else
            scale(new Scale(0, scaleY, 0, null));
    }

    public float scaleZ()
    {
        if (scaleProperty().isPresent())
            return scaleProperty().getValue().getY();
        return 1;
    }

    public void scaleZ(float scaleZ)
    {
        if (scaleProperty().isPresent())
            scale(new Scale(scaleX(), scaleY(), 0, scalePivot()));
        else
            scale(new Scale(0, 0, scaleZ, null));
    }

    public void scale(float scale)
    {
        if (scaleProperty().isPresent())
            scale(new Scale(scale, scale, scale, scalePivot()));
        else
            scale(new Scale(scale, scale, scale, null));
    }

    public Position scalePivot()
    {
        if (scaleProperty().isPresent())
            return scale().getPivot().orElse(null);
        return null;
    }

    public void scalePivot(Position scalePivot)
    {
        if (scaleProperty().isPresent())
            scale(new Scale(scaleX(), scaleY(), scaleZ(), scalePivot));
        else
            scale(new Scale(1, 1, 1, scalePivot));
    }

    public GuiOverflow overflow()
    {
        return overflowProperty().getValue();
    }

    public void overflow(GuiOverflow overflow)
    {
        overflowProperty().setValue(overflow);
    }

    public float screenXToNodeX(float screenX)
    {
        return screenX / scaleX() - rightPos();
    }

    public float screenYToNodeY(float screenY)
    {
        return screenY / scaleY() - topPos();
    }

    public float nodeXToScreenX(float nodeX)
    {
        return (leftPos() + nodeX) * scaleX();
    }

    public float nodeYToScreenY(float nodeY)
    {
        return (topPos() + nodeY) * scaleY();
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
    public void borderWidth(float width)
    {
        borderWidthTopProperty().setValue(width);
        borderWidthBottomProperty().setValue(width);
        borderWidthLeftProperty().setValue(width);
        borderWidthRightProperty().setValue(width);
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
}
