package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.animation.Animation;
import net.voxelindustry.brokkgui.animation.Interpolator;
import net.voxelindustry.brokkgui.animation.Interpolators;
import net.voxelindustry.brokkgui.event.DisposeEvent;
import net.voxelindustry.brokkgui.exp.component.GuiElement;

import java.util.concurrent.TimeUnit;

public abstract class Transition extends Animation
{
    private Interpolator interpolator;
    private GuiElement   element;

    public Transition(GuiElement element, long duration, TimeUnit unit)
    {
        super(duration, unit);

        this.interpolator = Interpolators.QUAD_BOTH;

        this.getProgressProperty().addListener(obs -> apply(interpolator.apply(getProgress())));

        this.element = element;
        this.element.getEventDispatcher().addHandler(DisposeEvent.TYPE, event -> this.complete());
    }

    protected abstract void apply(float interpolated);

    public Interpolator interpolator()
    {
        return interpolator;
    }

    public void interpolator(Interpolator interpolator)
    {
        this.interpolator = interpolator;
    }

    public GuiElement element()
    {
        if (element == null && getParent() instanceof Transition)
            return ((Transition) getParent()).element();
        return element;
    }
}
