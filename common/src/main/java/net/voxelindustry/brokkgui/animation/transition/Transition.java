package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.animation.Animation;
import net.voxelindustry.brokkgui.animation.Interpolator;
import net.voxelindustry.brokkgui.animation.Interpolators;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.event.DisposeEvent;

import java.util.concurrent.TimeUnit;

public abstract class Transition extends Animation
{
    private Interpolator interpolator;
    private GuiElement   node;

    public Transition(GuiElement node, long duration, TimeUnit unit)
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

    public GuiElement getNode()
    {
        if (node == null && getParent() instanceof Transition)
            return ((Transition) getParent()).getNode();
        return node;
    }
}
