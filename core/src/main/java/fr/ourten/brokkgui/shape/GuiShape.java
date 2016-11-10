package fr.ourten.brokkgui.shape;

import fr.ourten.brokkgui.component.GuiNode;
import fr.ourten.brokkgui.paint.Color;
import fr.ourten.teabeans.value.BaseProperty;

public abstract class GuiShape extends GuiNode
{
    private final BaseProperty<Color> colorProperty;
    private final BaseProperty<Float> lineWeightProperty;
    private final BaseProperty<Color> lineColorProperty;

    public GuiShape()
    {
        this.colorProperty = new BaseProperty<>(Color.WHITE, "colorProperty");
        this.lineWeightProperty = new BaseProperty<>(0F, "lineWeightProperty");
        this.lineColorProperty = new BaseProperty<>(Color.WHITE, "lineColorProperty");
    }

    public BaseProperty<Color> getColorProperty()
    {
        return this.colorProperty;
    }

    public BaseProperty<Float> getLineWeightProperty()
    {
        return this.lineWeightProperty;
    }

    public BaseProperty<Color> getLineColorProperty()
    {
        return this.lineColorProperty;
    }

    public Color getColor()
    {
        return this.getColorProperty().getValue();
    }

    public void setColor(final Color color)
    {
        this.getColorProperty().setValue(color);
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
}