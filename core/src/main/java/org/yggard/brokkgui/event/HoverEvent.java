package org.yggard.brokkgui.event;

import org.yggard.brokkgui.component.GuiNode;
import org.yggard.hermod.EventType;

public class HoverEvent extends GuiStateEvent
{
    public static final EventType<HoverEvent> TYPE = new EventType<>(GuiStateEvent.ANY, "HOVER_STATE_EVENT");

    private final boolean                     entering;

    public HoverEvent(final GuiNode source)
    {
        this(source, false);
    }

    public HoverEvent(final GuiNode source, final boolean entering)
    {
        super(source);

        this.entering = entering;
    }

    public boolean isEntering()
    {
        return this.entering;
    }
}