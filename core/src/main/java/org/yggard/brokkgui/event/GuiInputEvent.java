package org.yggard.brokkgui.event;

import org.yggard.brokkgui.component.GuiNode;

import fr.ourten.hermod.EventType;
import fr.ourten.hermod.HermodEvent;

public class GuiInputEvent extends HermodEvent
{
    public static final EventType<GuiInputEvent> ANY = new EventType<>("INPUT_EVENT");

    public GuiInputEvent(final GuiNode source)
    {
        super(source);
    }
}