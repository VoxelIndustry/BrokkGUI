package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.component.GuiNode;

import java.util.concurrent.TimeUnit;

public class FadeTransition extends Transition
{
    private Double startOpacity;
    private Double endOpacity;

    private double fromOpacity;
    private double opacity;

    public FadeTransition(GuiNode node, long duration, TimeUnit unit)
    {
        super(node, duration, unit);
    }

    @Override
    protected void apply(float interpolated)
    {
        if (getNode() == null)
            return;

        getNode().setOpacity(fromOpacity + (endOpacity - fromOpacity) * interpolated);
    }

    @Override
    public void restart()
    {
        super.restart();

        if (startOpacity == null)
            fromOpacity = getNode().getOpacity();
        else
            fromOpacity = startOpacity;

        if (endOpacity == null)
            endOpacity = fromOpacity + opacity;
    }

    public double getStartOpacity()
    {
        return startOpacity;
    }

    public void setStartOpacity(double startOpacity)
    {
        this.startOpacity = startOpacity;
    }

    public double getEndOpacity()
    {
        return endOpacity;
    }

    public void setEndOpacity(double endOpacity)
    {
        this.endOpacity = endOpacity;
    }

    public double getOpacity()
    {
        return opacity;
    }

    public void setOpacity(double opacity)
    {
        this.opacity = opacity;
    }
}
