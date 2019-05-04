package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.exp.component.GuiElement;

import java.util.concurrent.TimeUnit;

public class ScaleTransition extends Transition
{
    private Float startX, startY, startZ;
    private Float endX, endY, endZ;

    private float fromX, fromY, fromZ;
    private float translateX, translateY, translateZ;

    public ScaleTransition(GuiElement element, long duration, TimeUnit unit)
    {
        super(element, duration, unit);
    }

    @Override
    protected void apply(float interpolated)
    {
        if (element() == null)
            return;

        element().transform().scaleX(fromX + (endX - fromX) * interpolated);
        element().transform().scaleY(fromY + (endY - fromY) * interpolated);
        element().transform().scaleZ(fromZ + (endZ - fromZ) * interpolated);
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

        if (startZ == null)
            fromZ = element().transform().scaleZ();
        else
            fromZ = startZ;

        if (endX == null)
            endX = fromX + translateX;
        if (endY == null)
            endY = fromY + translateY;
        if (endZ == null)
            endZ = fromZ + translateZ;
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

    public Float startZ()
    {
        return startZ;
    }

    public void startZ(Float startZ)
    {
        this.startZ = startZ;
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

    public Float endZ()
    {
        return endZ;
    }

    public void endZ(Float endZ)
    {
        this.endZ = endZ;
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

    public float translateZ()
    {
        return translateZ;
    }

    public void translateZ(float translateZ)
    {
        this.translateZ = translateZ;
    }
}
