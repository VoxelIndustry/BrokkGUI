package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.component.GuiElement;

import java.util.concurrent.TimeUnit;

public class RotateTransition extends Transition
{
    private Float startAngle;
    private Float endAngle;

    private float fromAngle;
    private float rotateAngle;

    public RotateTransition(GuiElement node, long duration, TimeUnit unit)
    {
        super(node, duration, unit);
    }

    @Override
    protected void apply(float interpolated)
    {
        if (getNode() == null)
            return;

        getNode().transform().rotation().setAngle(fromAngle + (endAngle - fromAngle) * interpolated);
    }

    @Override
    public void restart()
    {
        super.restart();

        if (startAngle == null)
            fromAngle = getNode().transform().rotation().getAngle();
        else
            fromAngle = startAngle;

        if (endAngle == null)
            endAngle = fromAngle + rotateAngle;
    }

    public Float getStartAngle()
    {
        return startAngle;
    }

    public void setStartAngle(Float startAngle)
    {
        this.startAngle = startAngle;
    }

    public Float getEndAngle()
    {
        return endAngle;
    }

    public void setEndAngle(Float endAngle)
    {
        this.endAngle = endAngle;
    }

    public float getRotateAngle()
    {
        return rotateAngle;
    }

    public void setRotateAngle(float rotateAngle)
    {
        this.rotateAngle = rotateAngle;
    }
}
