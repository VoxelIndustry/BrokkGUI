package org.yggard.brokkgui.shape;

import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.GuiPaint;

import fr.ourten.teabeans.value.BaseProperty;

public abstract class GuiShape extends GuiNode
{
    private final BaseProperty<Float>    lineWeightProperty;
    private final BaseProperty<Color>    lineColorProperty;

    private final BaseProperty<GuiPaint> fillProperty;

    public GuiShape(String type)
    {
        super(type);
        this.lineWeightProperty = new BaseProperty<>(0F, "lineWeightProperty");
        this.lineColorProperty = new BaseProperty<>(Color.WHITE, "lineColorProperty");

        this.fillProperty = new BaseProperty<>(Color.AQUA, "fillProperty");
    }

    public BaseProperty<Float> getLineWeightProperty()
    {
        return this.lineWeightProperty;
    }

    public BaseProperty<Color> getLineColorProperty()
    {
        return this.lineColorProperty;
    }

    public BaseProperty<GuiPaint> getFillProperty()
    {
        return this.fillProperty;
    }

    public float getLineWeight()
    {
        return this.getLineWeightProperty().getValue();
    }

    public void setLineWeight(final float lineWeight)
    {
        this.getLineWeightProperty().setValue(lineWeight);
    }

    public Color getLineColor()
    {
        return this.getLineColorProperty().getValue();
    }

    public void setLineColor(final Color lineColor)
    {
        this.getLineColorProperty().setValue(lineColor);
    }

    public GuiPaint getFill()
    {
        return this.getFillProperty().getValue();
    }

    public void setFill(final GuiPaint fill)
    {
        this.getFillProperty().setValue(fill);
    }
}