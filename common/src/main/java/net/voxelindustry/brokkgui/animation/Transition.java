package net.voxelindustry.brokkgui.animation;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.event.DisposeEvent;

import java.util.concurrent.TimeUnit;

public abstract class Transition extends Animation
{
    private Interpolator interpolator;
    private GuiNode      node;

    public Transition(GuiNode node, long duration, TimeUnit unit)
    {
        super(duration, unit);

        this.interpolator = Interpolators.QUAD_BOTH;

        this.getProgressProperty().addListener(obs -> apply(interpolator.apply(getProgress())));

        this.node = node;
        this.node.getEventDispatcher().addHandler(DisposeEvent.TYPE, event -> this.complete());
    }

    protected abstract void apply(float interpolated);

    public Interpolator getInterpolator()
    {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator)
    {
        this.interpolator = interpolator;
    }

    public GuiNode getNode()
    {
        return node;
    }
}
