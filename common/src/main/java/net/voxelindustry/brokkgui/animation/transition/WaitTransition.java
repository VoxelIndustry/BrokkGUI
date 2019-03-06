package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.component.GuiNode;

import java.util.concurrent.TimeUnit;

public class WaitTransition extends Transition
{
    public WaitTransition(GuiNode node, long duration, TimeUnit unit)
    {
        super(node, duration, unit);
    }

    @Override
    protected void apply(float interpolated)
    {
        // This animation does absolutely nothing.
    }
}
