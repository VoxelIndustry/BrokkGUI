package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.component.GuiNode;

import java.util.concurrent.TimeUnit;

public class SizeTransition extends Transition
{
    private Float startX, startY;
    private Float endX, endY;

    private float fromX, fromY;
    private float translateX, translateY;

    public SizeTransition(GuiNode node, long duration, TimeUnit unit)
    {
        super(node, duration, unit);
    }

    @Override
    protected void apply(float interpolated)
    {
        if (getNode() == null)
            return;

        getNode().setSize(fromX + (endX - fromX) * interpolated, fromY + (endY - fromY) * interpolated);
    }

    @Override
    public void restart()
    {
        super.restart();

        if (startX == null)
            fromX = getNode().getScaleX();
        else
            fromX = startX;

        if (startY == null)
            fromY = getNode().getScaleY();
        else
            fromY = startY;

        if (endX == null)
            endX = fromX + translateX;
        if (endY == null)
            endY = fromY + translateY;
    }

    public Float getStartX()
    {
        return startX;
    }

    public void setStartX(Float startX)
    {
        this.startX = startX;
    }

    public Float getStartY()
    {
        return startY;
    }

    public void setStartY(Float startY)
    {
        this.startY = startY;
    }

    public Float getEndX()
    {
        return endX;
    }

    public void setEndX(Float endX)
    {
        this.endX = endX;
    }

    public Float getEndY()
    {
        return endY;
    }

    public void setEndY(Float endY)
    {
        this.endY = endY;
    }

    public float getTranslateX()
    {
        return translateX;
    }

    public void setTranslateX(float translateX)
    {
        this.translateX = translateX;
    }

    public float getTranslateY()
    {
        return translateY;
    }

    public void setTranslateY(float translateY)
    {
        this.translateY = translateY;
    }
}
