package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.exp.component.GuiElement;

import java.util.concurrent.TimeUnit;

public class FadeTransition extends Transition
{
    private Double startOpacity;
    private Double endOpacity;

    private double fromOpacity;
    private double opacity;

    public FadeTransition(GuiElement element, long duration, TimeUnit unit)
    {
        super(element, duration, unit);
    }

    @Override
    protected void apply(float interpolated)
    {
        if (element() == null)
            return;

        element().setOpacity(fromOpacity + (endOpacity - fromOpacity) * interpolated);
    }

    @Override
    public void restart()
    {
        super.restart();

        if (startOpacity == null)
            fromOpacity = element().getOpacity();
        else
            fromOpacity = startOpacity;

        if (endOpacity == null)
            endOpacity = fromOpacity + opacity;
    }

    public double startOpacity()
    {
        return startOpacity;
    }

    public void startOpacity(double startOpacity)
    {
        this.startOpacity = startOpacity;
    }

    public double endOpacity()
    {
        return endOpacity;
    }

    public void endOpacity(double endOpacity)
    {
        this.endOpacity = endOpacity;
    }

    public double opacity()
    {
        return opacity;
    }

    public void opacity(double opacity)
    {
        this.opacity = opacity;
    }
}
