package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.data.Rotation;
import net.voxelindustry.brokkgui.event.LayoutEvent;
import net.voxelindustry.brokkgui.shape.ScissorBox;
import net.voxelindustry.brokkgui.util.MouseInBoundsChecker;

import java.util.List;

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

    private final BaseProperty<Float> scaleXProperty;
    private final BaseProperty<Float> scaleYProperty;
    private final BaseProperty<Float> scaleZProperty;

    private final BaseProperty<Transform>     parentProperty;
    private final BaseListProperty<Transform> childrenListProperty;

    private MouseInBoundsChecker mouseInBoundsChecker;

    private boolean bindChild = true;

    private ScissorBox scissorBox;

    public Transform()
    {
        this.xPosProperty = new BaseProperty<>(0f, "xPosProperty");
        this.yPosProperty = new BaseProperty<>(0f, "yPosProperty");

        this.xTranslateProperty = new BaseProperty<>(0f, "xTranslateProperty");
        this.yTranslateProperty = new BaseProperty<>(0f, "yTranslateProperty");

        this.widthProperty = new BaseProperty<>(0f, "widthProperty");
        this.heightProperty = new BaseProperty<>(0f, "heightProperty");

        this.widthRatioProperty = new BaseProperty<>(-1f, "widthRatioProperty");
        this.heightRatioProperty = new BaseProperty<>(-1f, "heightRatioProperty");

        this.zLevelProperty = new BaseProperty<>(0f, "zLevelProperty");

        this.rotationProperty = new BaseProperty<>(Rotation.NONE, "rotationProperty");
        this.scaleXProperty = new BaseProperty<>(1f, "scaleXProperty");
        this.scaleYProperty = new BaseProperty<>(1f, "scaleYProperty");
        this.scaleZProperty = new BaseProperty<>(1f, "scaleZProperty");

        this.parentProperty = new BaseProperty<>(null, "parentProperty");
        this.childrenListProperty = new BaseListProperty<>(null, "childrenListProperty");

        this.childrenListProperty.addListener((ListValueChangeListener<Transform>) (obs, oldValue, newValue) ->
        {
            if (newValue != null)
                newValue.setParent(this);
            if (oldValue != null)
                oldValue.setParent(null);
        });

        this.mouseInBoundsChecker = MouseInBoundsChecker.DEFAULT;
    }

    ///////////////
    // HIERARCHY //
    ///////////////

    public BaseProperty<Transform> parentProperty()
    {
        return this.parentProperty;
    }

    public Transform parent()
    {
        return this.parentProperty().getValue();
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
                    new LayoutEvent.Remove(this.element()));

        this.parentProperty().setValue(parent);

        if (parent != null)
        {
            if (this.widthRatio() != -1)
                RelativeBindingHelper.bindWidthRelative(this, parent, this.widthRatioProperty());
            if (this.heightRatio() != -1)
                RelativeBindingHelper.bindHeightRelative(this, parent, this.heightRatioProperty());

            this.element().setWindow(parent.element().getWindow());

            if (this.element().getWindow() != null)
                element().getWindow().dispatchEvent(LayoutEvent.ADD, new LayoutEvent.Add(this.element()));
        }
    }

    /**
     * @return an immutable list
     */
    public List<Transform> children()
    {
        return this.childrenProperty().getValue();
    }

    public BaseListProperty<Transform> childrenProperty()
    {
        return this.childrenListProperty;
    }

    public void addChild(Transform element)
    {
        this.childrenProperty().add(element);

        if (this.bindChild())
            RelativeBindingHelper.bindToPos(element, this);
    }

    public void addChildren(Transform... elements)
    {
        for (Transform element : elements)
            this.addChild(element);
    }

    public void removeChild(Transform element)
    {
        this.childrenProperty().remove(element);

        element.xPosProperty().unbind();
        element.yPosProperty().unbind();
    }

    public void clearChildren()
    {
        this.childrenProperty().getValue().forEach(element ->
        {
            element.xPosProperty().unbind();
            element.yPosProperty().unbind();
        });
        this.childrenProperty().clear();
    }

    public boolean hasChild(Transform element)
    {
        return this.childrenProperty().contains(element);
    }

    public boolean bindChild()
    {
        return this.bindChild;
    }

    public void bindChild(boolean bindChild)
    {
        this.bindChild = bindChild;
    }

    ////////////////
    // TRANSFORMS //
    ////////////////

    public boolean isPointInside(int pointX, int pointY)
    {
        return this.mouseInBoundsChecker().test(this.element(), pointX, pointY);
    }

    public MouseInBoundsChecker mouseInBoundsChecker()
    {
        return this.mouseInBoundsChecker;
    }

    public void mouseInBoundsChecker(MouseInBoundsChecker checker)
    {
        this.mouseInBoundsChecker = checker;
    }

    public BaseProperty<Float> zLevelProperty()
    {
        return this.zLevelProperty;
    }

    public float zLevel()
    {
        return this.zLevelProperty().getValue();
    }

    public void zLevel(float zLevel)
    {
        this.zLevelProperty().setValue(zLevel);
    }

    public BaseProperty<Float> xPosProperty()
    {
        return this.xPosProperty;
    }

    public BaseProperty<Float> yPosProperty()
    {
        return this.yPosProperty;
    }

    public BaseProperty<Float> xTranslateProperty()
    {
        return this.xTranslateProperty;
    }

    public BaseProperty<Float> yTranslateProperty()
    {
        return this.yTranslateProperty;
    }

    public BaseProperty<Float> widthProperty()
    {
        return this.widthProperty;
    }

    public BaseProperty<Float> heightProperty()
    {
        return this.heightProperty;
    }

    public BaseProperty<Float> widthRatioProperty()
    {
        return this.widthRatioProperty;
    }

    public BaseProperty<Float> heightRatioProperty()
    {
        return this.heightRatioProperty;
    }

    public BaseProperty<Rotation> rotationProperty()
    {
        return rotationProperty;
    }

    public BaseProperty<Float> scaleXProperty()
    {
        return scaleXProperty;
    }

    public BaseProperty<Float> scaleYProperty()
    {
        return scaleYProperty;
    }

    public BaseProperty<Float> scaleZProperty()
    {
        return scaleZProperty;
    }

    /**
     * @return xPos, used for layout management, do not attempt to change the
     * property outside the layout scope.
     */
    public float xPos()
    {
        return this.xPosProperty().getValue();
    }

    /**
     * @return yPos, used for layout management, do not attempt to change the
     * property outside the layout scope.
     */
    public float yPos()
    {
        return this.yPosProperty().getValue();
    }

    /**
     * @return xTranslate, used for component positionning.
     */
    public float xTranslate()
    {
        return this.xTranslateProperty().getValue();
    }

    /**
     * Set the translation of this component to this specified value, usable outside
     * layout scope.
     *
     * @param xTranslate
     */
    public void xTranslate(float xTranslate)
    {
        this.xTranslateProperty().setValue(xTranslate);
    }

    /**
     * @return yTranslate, used for component positionning.
     */
    public float yTranslate()
    {
        return this.yTranslateProperty().getValue();
    }

    /**
     * Set the translation of this component to this specified value, usable outside
     * layout scope.
     *
     * @param yTranslate
     */
    public void yTranslate(float yTranslate)
    {
        this.yTranslateProperty().setValue(yTranslate);
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
        this.xTranslate(xTranslate);
        this.yTranslate(yTranslate);
    }

    public float width()
    {
        return this.widthProperty().getValue();
    }

    public void width(float width)
    {
        if (this.widthProperty().isBound())
            this.widthProperty().unbind();
        this.widthProperty().setValue(width);
    }

    public float height()
    {
        return this.heightProperty().getValue();
    }

    public void height(float height)
    {
        if (this.heightProperty().isBound())
            this.heightProperty().unbind();
        this.heightProperty().setValue(height);
    }

    /**
     * Method computing the left-pos of this Node box (pos.x + translate.x)
     *
     * @return the computed non-cached value
     */
    public float leftPos()
    {
        return this.xPos() + this.xTranslate();
    }

    /**
     * Method computing the top-pos of this Node box (pos.y + translate.y)
     *
     * @return the computed non-cached value
     */
    public float topPos()
    {
        return this.yPos() + this.yTranslate();
    }

    /**
     * Method computing the right-pos of this Node box (pos.x + translate.x + width)
     *
     * @return the computed non-cached value
     */
    public float rightPos()
    {
        return this.xPos() + this.xTranslate() + this.width();
    }

    /**
     * Method computing the bottom-pos of this Node box (pos.y + translate.y + height)
     *
     * @return the computed non-cached value
     */
    public float bottomPos()
    {
        return this.yPos() + this.yTranslate() + this.height();
    }

    /**
     * Helper method to set the width and height of the Node in one call.
     *
     * @param width
     * @param height
     */
    public void size(float width, float height)
    {
        this.width(width);
        this.height(height);
    }

    public float widthRatio()
    {
        return this.widthRatioProperty().getValue();
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
            this.widthProperty().unbind();
        else if (!this.widthProperty().isBound() && this.parent() != null)
            RelativeBindingHelper.bindWidthRelative(this, this.parent(), this.widthRatioProperty());
        this.widthRatioProperty().setValue(ratio);
    }

    public float heightRatio()
    {
        return this.heightRatioProperty().getValue();
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
            this.heightProperty().unbind();
        else if (!this.heightProperty().isBound() && this.parent() != null)
            RelativeBindingHelper.bindHeightRelative(this, this.parent(), this.heightRatioProperty());
        this.heightRatioProperty().setValue(ratio);
    }

    /**
     * Helper method to set the width ratio and height ratio in one call.
     *
     * @param widthRatio
     * @param heightRatio
     */
    public void sizeRatio(float widthRatio, float heightRatio)
    {
        this.widthRatio(widthRatio);
        this.heightRatio(heightRatio);
    }

    public Rotation rotation()
    {
        return this.rotationProperty().getValue();
    }

    public void rotate(Rotation rotation)
    {
        this.rotationProperty().setValue(rotation);
    }

    public float scaleX()
    {
        return this.scaleXProperty().getValue();
    }

    public void scaleX(float scaleX)
    {
        this.scaleXProperty().setValue(scaleX);
    }

    public float scaleY()
    {
        return this.scaleYProperty().getValue();
    }

    public void scaleY(float scaleY)
    {
        this.scaleYProperty().setValue(scaleY);
    }

    public float scaleZ()
    {
        return this.scaleZProperty().getValue();
    }

    public void scaleZ(float scaleZ)
    {
        this.scaleZProperty().setValue(scaleZ);
    }

    public void scale(float scale)
    {
        this.scaleX(scale);
        this.scaleY(scale);
        this.scaleZ(scale);
    }

    public ScissorBox scissorBox()
    {
        return scissorBox;
    }

    public void scissorBox(ScissorBox scissorBox)
    {
        this.scissorBox = scissorBox;
    }
}
