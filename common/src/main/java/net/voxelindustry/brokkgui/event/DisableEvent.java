package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.hermod.EventType;

public class DisableEvent extends GuiStateEvent
{
    public static final EventType<DisableEvent> TYPE = new EventType<>(ANY, "DISABLE_STATE_EVENT");

    private final boolean                       disabled;

    public DisableEvent(final GuiNode source)
    {
        this(source, false);
    }

    public DisableEvent(final GuiNode source, final boolean disabled)
    {
        super(source);

        this.disabled = disabled;
    }

    public boolean isDisabled()
    {
        return this.disabled;
    }
}