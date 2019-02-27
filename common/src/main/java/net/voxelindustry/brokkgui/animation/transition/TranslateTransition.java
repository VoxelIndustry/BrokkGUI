package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.component.GuiNode;

import java.util.concurrent.TimeUnit;

public class TranslateTransition extends Transition
{
    private Float startX, startY;
    private Float endX, endY;

    private float fromX, fromY;
    private float translateX, translateY;

    public TranslateTransition(GuiNode node, long millis)
    {
        this(node, millis, TimeUnit.MILLISECONDS);
    }

    public TranslateTransition(GuiNode node, long duration, TimeUnit unit)
    {
        super(node, duration, unit);
    }

    @Override
    protected void apply(float interpolated)
    {
        if (getNode() == null)
            return;

        getNode().setTranslate(fromX + (endX - fromX) * interpolated, fromY + (endY - fromY) * interpolated);
    }

    @Override
    public void restart()
    {
        super.restart();

        if (startX == null)
            fromX = getNode().getxTranslate();
        else
            fromX = startX;

        if (startY == null)
            fromY = getNode().getyTranslate();
        else
            fromY = startY;

        if (endX == null)
            endX = fromX + translateX;
        if (endY == null)
            endY = fromY + translateY;
    }

    public float getStartX()
    {
        return startX;
    }

    public void setStartX(float startX)
    {
        this.startX = startX;
    }

    public float getStartY()
    {
        return startY;
    }

    public void setStartY(float startY)
    {
        this.startY = startY;
    }

    public float getEndX()
    {
        return endX;
    }

    public void setEndX(float endX)
    {
        this.endX = endX;
    }

    public float getEndY()
    {
        return endY;
    }

    public void setEndY(float endY)
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
