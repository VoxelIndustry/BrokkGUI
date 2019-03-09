package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class DisposeEvent extends GuiStateEvent
{
    public static final EventType<DisposeEvent> TYPE = new EventType<>(ANY, "DISPOSE_STATE_EVENT");

    public DisposeEvent(GuiNode source)
    {
        super(source);
    }

    @Override
    public DisposeEvent copy(IEventEmitter source)
    {
        return new DisposeEvent((GuiNode) source);
    }
}
