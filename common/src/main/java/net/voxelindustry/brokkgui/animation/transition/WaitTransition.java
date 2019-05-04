package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.exp.component.GuiElement;

import java.util.concurrent.TimeUnit;

public class WaitTransition extends Transition
{
    public WaitTransition(GuiElement element, long duration, TimeUnit unit)
    {
        super(element, duration, unit);
    }

    @Override
    protected void apply(float interpolated)
    {
        // This animation does absolutely nothing.
    }
}
