package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.component.GuiElement;

import java.util.concurrent.TimeUnit;

public class TranslateTransition extends Transition
{
    private Float startX, startY;
    private Float endX, endY;

    private float fromX, fromY;
    private float translateX, translateY;

    public TranslateTransition(GuiElement element, long millis)
    {
        this(element, millis, TimeUnit.MILLISECONDS);
    }

    public TranslateTransition(GuiElement element, long duration, TimeUnit unit)
    {
        super(element, duration, unit);
    }

    @Override
    protected void apply(float interpolated)
    {
        if (element() == null)
            return;

        element().transform().translate(fromX + (endX - fromX) * interpolated, fromY + (endY - fromY) * interpolated);
    }

    @Override
    public void restart()
    {
        super.restart();

        if (startX == null)
            fromX = element().transform().xTranslate();
        else
            fromX = startX;

        if (startY == null)
            fromY = element().transform().yTranslate();
        else
            fromY = startY;

        if (endX == null)
            endX = fromX + translateX;
        if (endY == null)
            endY = fromY + translateY;
    }

    public float startX()
    {
        return startX;
    }

    public void startX(float startX)
    {
        this.startX = startX;
    }

    public float startY()
    {
        return startY;
    }

    public void startY(float startY)
    {
        this.startY = startY;
    }

    public float endX()
    {
        return endX;
    }

    public void endX(float endX)
    {
        this.endX = endX;
    }

    public float endY()
    {
        return endY;
    }

    public void endY(float endY)
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
