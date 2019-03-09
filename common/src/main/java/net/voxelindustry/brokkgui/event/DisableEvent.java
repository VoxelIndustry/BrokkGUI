package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class DisableEvent extends GuiStateEvent
{
    public static final EventType<DisableEvent> TYPE = new EventType<>(ANY, "DISABLE_STATE_EVENT");

    private final boolean disabled;

    public DisableEvent(GuiNode source)
    {
        this(source, false);
    }

    public DisableEvent(GuiNode source, boolean disabled)
    {
        super(source);

        this.disabled = disabled;
    }

    public boolean isDisabled()
    {
        return this.disabled;
    }

    @Override
    public DisableEvent copy(IEventEmitter source)
    {
        return new DisableEvent((GuiNode) source, isDisabled());
    }
}