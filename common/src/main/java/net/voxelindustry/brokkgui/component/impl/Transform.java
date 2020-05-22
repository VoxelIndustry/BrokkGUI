package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.data.Position;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.data.Rotation;
import net.voxelindustry.brokkgui.data.Scale;
import net.voxelindustry.brokkgui.event.LayoutEvent;
import net.voxelindustry.brokkgui.shape.ScissorBox;
import net.voxelindustry.brokkgui.util.MouseInBoundsChecker;

import java.util.List;
import java.util.stream.Stream;

public class Transform extends GuiComponent
{
    private final BaseProperty<Float> xPosProperty;
    private final BaseProperty<Float> yPosProperty;
    private final BaseProperty<Float> xTranslateProperty;
    private final BaseProperty<Float> yTranslateProperty;
    private final BaseProperty<Float> widthProperty;
    private final BaseProperty<Float> heightProperty;
    private final BaseProperty<Float> widthRatioProperty;
    private final BaseProperty<Float> heightRatioProperty;
    private final BaseProperty<Float> zLevelProperty;

    private final BaseProperty<Rotation> rotationProperty;

    private final BaseProperty<Scale> scaleProperty;

    private final BaseProperty<Transform>     parentProperty;
    private final BaseListProperty<Transform> childrenListProperty;

    private MouseInBoundsChecker mouseInBoundsChecker;

    private boolean bindChild = true;

    private ScissorBox scissorBox;

    public Transform()
    {
        xPosProperty = new BaseProperty<>(0f, "xPosProperty");
        yPosProperty = new BaseProperty<>(0f, "yPosProperty");

        xTranslateProperty = new BaseProperty<>(0f, "xTranslateProperty");
        yTranslateProperty = new BaseProperty<>(0f, "yTranslateProperty");

        widthProperty = new BaseProperty<>(0f, "widthProperty");
        heightProperty = new BaseProperty<>(0f, "heightProperty");

        widthRatioProperty = new BaseProperty<>(-1f, "widthRatioProperty");
        heightRatioProperty = new BaseProperty<>(-1f, "heightRatioProperty");

        zLevelProperty = new BaseProperty<>(0f, "zLevelProperty");

        rotationProperty = new BaseProperty<>(Rotation.NONE, "rotationProperty");
        scaleProperty = new BaseProperty<>(null, "scaleXProperty");

        parentProperty = new BaseProperty<>(null, "parentProperty");
        childrenListProperty = new BaseListProperty<>(null, "childrenListProperty");

        childrenListProperty.addListener((ListValueChangeListener<Transform>) (obs, oldValue, newValue) ->
        {
            if (newValue != null)
                newValue.setParent(this);
            if (oldValue != null)
                oldValue.setParent(null);
        });

        mouseInBoundsChecker = MouseInBoundsChecker.DEFAULT;
    }

    ///////////////
    // HIERARCHY //
    ///////////////

    public BaseProperty<Transform> parentProperty()
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
        if (parentProperty().isPresent() && parent().element().getWindow() != null)
            parent().element().getWindow().dispatchEvent(LayoutEvent.REMOVE,
                    new LayoutEvent.Remove(element()));

        parentProperty().setValue(parent);

        if (parent != null)
        {
            if (widthRatio() != -1)
                RelativeBindingHelper.bindWidthRelative(this, parent, widthRatioProperty());
            if (heightRatio() != -1)
                RelativeBindingHelper.bindHeightRelative(this, parent, heightRatioProperty());

            element().setWindow(parent.element().getWindow());

            if (element().getWindow() != null)
                element().getWindow().dispatchEvent(LayoutEvent.ADD, new LayoutEvent.Add(element()));
        }
    }

    /**
     * @return an immutable list
     */
    public List<Transform> children()
    {
        return childrenProperty().getValue();
    }

    public BaseListProperty<Transform> childrenProperty()
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

    public BaseProperty<Float> zLevelProperty()
    {
        return zLevelProperty;
    }

    public float zLevel()
    {
        return zLevelProperty().getValue();
    }

    public void zLevel(float zLevel)
    {
        zLevelProperty().setValue(zLevel);
    }

    public BaseProperty<Float> xPosProperty()
    {
        return xPosProperty;
    }

    public BaseProperty<Float> yPosProperty()
    {
        return yPosProperty;
    }

    public BaseProperty<Float> xTranslateProperty()
    {
        return xTranslateProperty;
    }

    public BaseProperty<Float> yTranslateProperty()
    {
        return yTranslateProperty;
    }

    public BaseProperty<Float> widthProperty()
    {
        return widthProperty;
    }

    public BaseProperty<Float> heightProperty()
    {
        return heightProperty;
    }

    public BaseProperty<Float> widthRatioProperty()
    {
        return widthRatioProperty;
    }

    public BaseProperty<Float> heightRatioProperty()
    {
        return heightRatioProperty;
    }

    public BaseProperty<Rotation> rotationProperty()
    {
        return rotationProperty;
    }

    public BaseProperty<Scale> scaleProperty()
    {
        return scaleProperty;
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

    public ScissorBox scissorBox()
    {
        return scissorBox;
    }

    public void scissorBox(ScissorBox scissorBox)
    {
        this.scissorBox = scissorBox;
    }

    public float screenXToNodeX(float screenX)
    {
        return screenX / scaleX() - leftPos();
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
}
