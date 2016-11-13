package org.yggard.brokkgui.event;

import org.yggard.brokkgui.component.GuiNode;
import org.yggard.hermod.EventType;
import org.yggard.hermod.HermodEvent;

public class GuiStateEvent extends HermodEvent
{
    public static final EventType<GuiStateEvent> ANY = new EventType<>("STATE_EVENT");

    public GuiStateEvent(final GuiNode source)
    {
        super(source);
    }
}