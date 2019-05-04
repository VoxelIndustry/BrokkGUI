package net.voxelindustry.brokkgui.exp.component;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.data.Rotation;
import net.voxelindustry.brokkgui.event.LayoutEvent;
import net.voxelindustry.brokkgui.shape.ScissorBox;

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
    }

    ///////////////
    // HIERARCHY //
    ///////////////

    public BaseProperty<Transform> parentProperty()
    {
        return this.parentProperty;
    }

    public Transform getParent()
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
        if (parentProperty().isPresent() && getParent().getElement().getWindow() != null)
            getParent().getElement().getWindow().dispatchEvent(LayoutEvent.REMOVE,
                    new LayoutEvent.Remove(this.getElement()));

        this.parentProperty().setValue(parent);

        if (parent != null)
        {
            if (this.getWidthRatio() != -1)
                RelativeBindingHelper.bindWidthRelative(this, parent, this.widthRatioProperty());
            if (this.getHeightRatio() != -1)
                RelativeBindingHelper.bindHeightRelative(this, parent, this.heightRatioProperty());

            this.getElement().setWindow(parent.getElement().getWindow());

            if (this.getElement().getWindow() != null)
                getElement().getWindow().dispatchEvent(LayoutEvent.ADD, new LayoutEvent.Add(this.getElement()));
        }
    }

    /**
     * @return an immutable list
     */
    public List<Transform> getChildren()
    {
        return this.childrenProperty().getValue();
    }

    protected BaseListProperty<Transform> childrenProperty()
    {
        return this.childrenListProperty;
    }

    public void addChild(Transform element)
    {
        this.childrenProperty().add(element);
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
        this.childrenProperty().getValue().forEach(node ->
        {
            node.xPosProperty().unbind();
            node.yPosProperty().unbind();
        });
        this.childrenProperty().clear();
    }

    public boolean hasChild(Transform element)
    {
        return this.childrenProperty().contains(element);
    }

    ////////////////
    // TRANSFORMS //
    ////////////////

    public boolean isPointInside(int pointX, int pointY)
    {
        return this.getxPos() + this.getxTranslate() < pointX
                && pointX < this.getxPos() + this.getxTranslate() + this.getWidth()
                && this.getyPos() + this.getyTranslate() < pointY
                && pointY < this.getyPos() + this.getyTranslate() + this.getHeight();
    }

    public BaseProperty<Float> zLevelProperty()
    {
        return this.zLevelProperty;
    }

    public float getzLevel()
    {
        return this.zLevelProperty().getValue();
    }

    public void setzLevel(float zLevel)
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
     * property outside the layouting scope.
     */
    public float getxPos()
    {
        return this.xPosProperty().getValue();
    }

    /**
     * @return yPos, used for layout management, do not attempt to change the
     * property outside the layouting scope.
     */
    public float getyPos()
    {
        return this.yPosProperty().getValue();
    }

    /**
     * @return xTranslate, used for component positionning.
     */
    public float getxTranslate()
    {
        return this.xTranslateProperty().getValue();
    }

    /**
     * Set the translation of this component to this specified value, usable outside
     * layouting scope.
     *
     * @param xTranslate
     */
    public void setxTranslate(float xTranslate)
    {
        this.xTranslateProperty().setValue(xTranslate);
    }

    /**
     * @return yTranslate, used for component positionning.
     */
    public float getyTranslate()
    {
        return this.yTranslateProperty().getValue();
    }

    /**
     * Set the translation of this component to this specified value, usable outside
     * layouting scope.
     *
     * @param yTranslate
     */
    public void setyTranslate(float yTranslate)
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
    public void setTranslate(float xTranslate, float yTranslate)
    {
        this.setxTranslate(xTranslate);
        this.setyTranslate(yTranslate);
    }

    public float getWidth()
    {
        return this.widthProperty().getValue();
    }

    public void setWidth(float width)
    {
        if (this.widthProperty().isBound())
            this.widthProperty().unbind();
        this.widthProperty().setValue(width);
    }

    public float getHeight()
    {
        return this.heightProperty().getValue();
    }

    public void setHeight(float height)
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
    public float getLeftPos()
    {
        return this.getxPos() + this.getxTranslate();
    }

    /**
     * Method computing the top-pos of this Node box (pos.y + translate.y)
     *
     * @return the computed non-cached value
     */
    public float getTopPos()
    {
        return this.getyPos() + this.getyTranslate();
    }

    /**
     * Method computing the right-pos of this Node box (pos.x + translate.x + width)
     *
     * @return the computed non-cached value
     */
    public float getRightPos()
    {
        return this.getxPos() + this.getxTranslate() + this.getWidth();
    }

    /**
     * Method computing the bottom-pos of this Node box (pos.y + translate.y + height)
     *
     * @return the computed non-cached value
     */
    public float getBottomPos()
    {
        return this.getyPos() + this.getyTranslate() + this.getHeight();
    }

    /**
     * Helper method to set the width and height of the Node in one call.
     *
     * @param width
     * @param height
     */
    public void setSize(float width, float height)
    {
        this.setWidth(width);
        this.setHeight(height);
    }

    public float getWidthRatio()
    {
        return this.widthRatioProperty().getValue();
    }

    /**
     * Allow to set the width of the node relatively to the width of his parentProperty. Use
     * absolute setWidth to cancel the binding or set the width ratio to -1
     *
     * @param ratio
     */
    public void setWidthRatio(float ratio)
    {
        if (ratio == -1)
            this.widthProperty().unbind();
        else if (!this.widthProperty().isBound() && this.getParent() != null)
            RelativeBindingHelper.bindWidthRelative(this, this.getParent(), this.widthRatioProperty());
        this.widthRatioProperty().setValue(ratio);
    }

    public float getHeightRatio()
    {
        return this.heightRatioProperty().getValue();
    }

    /**
     * Allow to set the height of the node relatively to the height of his parentProperty.
     * Use absolute setHeight to cancel the binding or set the height ratio to -1
     *
     * @param ratio
     */
    public void setHeightRatio(float ratio)
    {
        if (ratio == -1)
            this.heightProperty().unbind();
        else if (!this.heightProperty().isBound() && this.getParent() != null)
            RelativeBindingHelper.bindHeightRelative(this, this.getParent(), this.heightRatioProperty());
        this.heightRatioProperty().setValue(ratio);
    }

    /**
     * Helper method to set the width ratio and height ratio in one call.
     *
     * @param widthRatio
     * @param heightRatio
     */
    public void setSizeRatio(float widthRatio, float heightRatio)
    {
        this.setWidthRatio(widthRatio);
        this.setHeightRatio(heightRatio);
    }

    public Rotation getRotation()
    {
        return this.rotationProperty().getValue();
    }

    public void setRotation(Rotation rotation)
    {
        this.rotationProperty().setValue(rotation);
    }

    public float getScaleX()
    {
        return this.scaleXProperty().getValue();
    }

    public void setScaleX(float scaleX)
    {
        this.scaleXProperty().setValue(scaleX);
    }

    public float getScaleY()
    {
        return this.scaleYProperty().getValue();
    }

    public void setScaleY(float scaleY)
    {
        this.scaleYProperty().setValue(scaleY);
    }

    public float getScaleZ()
    {
        return this.scaleZProperty().getValue();
    }

    public void setScaleZ(float scaleZ)
    {
        this.scaleZProperty().setValue(scaleZ);
    }

    public void setScale(float scale)
    {
        this.setScaleX(scale);
        this.setScaleY(scale);
        this.setScaleZ(scale);
    }

    public ScissorBox getScissorBox()
    {
        return scissorBox;
    }

    public void setScissorBox(ScissorBox scissorBox)
    {
        this.scissorBox = scissorBox;
    }
}
