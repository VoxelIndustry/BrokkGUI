package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.hermod.EventType;

public class DisposeEvent extends GuiStateEvent
{
    public static final EventType<DisposeEvent> TYPE = new EventType<>(ANY, "DISPOSE_STATE_EVENT");

    public DisposeEvent(GuiNode source)
    {
        super(source);
    }
}
