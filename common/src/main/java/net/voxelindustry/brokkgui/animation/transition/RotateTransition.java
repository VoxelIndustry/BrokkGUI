package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.component.GuiElement;

import java.util.concurrent.TimeUnit;

public class RotateTransition extends Transition
{
    private Float startAngle;
    private Float endAngle;

    private float fromAngle;
    private float rotateAngle;

    public RotateTransition(GuiElement element, long duration, TimeUnit unit)
    {
        super(element, duration, unit);
    }

    @Override
    protected void apply(float interpolated)
    {
        if (element() == null)
            return;

        element().transform().rotation().setAngle(fromAngle + (endAngle - fromAngle) * interpolated);
    }

    @Override
    public void restart()
    {
        super.restart();

        if (startAngle == null)
            fromAngle = element().transform().rotation().getAngle();
        else
            fromAngle = startAngle;

        if (endAngle == null)
            endAngle = fromAngle + rotateAngle;
    }

    public Float startAngle()
    {
        return startAngle;
    }

    public void startAngle(Float startAngle)
    {
        this.startAngle = startAngle;
    }

    public Float endAngle()
    {
        return endAngle;
    }

    public void endAngle(Float endAngle)
    {
        this.endAngle = endAngle;
    }

    public float rotateAngle()
    {
        return rotateAngle;
    }

    public void rotateAngle(float rotateAngle)
    {
        this.rotateAngle = rotateAngle;
    }
}
