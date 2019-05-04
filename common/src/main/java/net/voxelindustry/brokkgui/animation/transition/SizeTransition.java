package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.exp.component.GuiElement;

import java.util.concurrent.TimeUnit;

public class SizeTransition extends Transition
{
    private Float startX, startY;
    private Float endX, endY;

    private float fromX, fromY;
    private float translateX, translateY;

    public SizeTransition(GuiElement element, long duration, TimeUnit unit)
    {
        super(element, duration, unit);
    }

    @Override
    protected void apply(float interpolated)
    {
        if (element() == null)
            return;

        element().transform().size(fromX + (endX - fromX) * interpolated, fromY + (endY - fromY) * interpolated);
    }

    @Override
    public void restart()
    {
        super.restart();

        if (startX == null)
            fromX = element().transform().scaleX();
        else
            fromX = startX;

        if (startY == null)
            fromY = element().transform().scaleY();
        else
            fromY = startY;

        if (endX == null)
            endX = fromX + translateX;
        if (endY == null)
            endY = fromY + translateY;
    }

    public Float startX()
    {
        return startX;
    }

    public void startX(Float startX)
    {
        this.startX = startX;
    }

    public Float startY()
    {
        return startY;
    }

    public void startY(Float startY)
    {
        this.startY = startY;
    }

    public Float endX()
    {
        return endX;
    }

    public void endX(Float endX)
    {
        this.endX = endX;
    }

    public Float endY()
    {
        return endY;
    }

    public void endY(Float endY)
    {
        this.endY = endY;
    }

    public float translateX()
    {
        return translateX;
    }

    public void translateX(float translateX)
    {
        this.translateX = translateX;
    }

    public float translateY()
    {
        return translateY;
    }

    public void translateY(float translateY)
    {
        this.translateY = translateY;
    }
}
