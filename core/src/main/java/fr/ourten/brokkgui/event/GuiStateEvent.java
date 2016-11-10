package fr.ourten.brokkgui.event;

import fr.ourten.brokkgui.component.GuiNode;
import fr.ourten.hermod.EventType;
import fr.ourten.hermod.HermodEvent;

public class GuiStateEvent extends HermodEvent
{
    public static final EventType<GuiStateEvent> ANY = new EventType<>("STATE_EVENT");

    public GuiStateEvent(final GuiNode source)
    {
        super(source);
    }
}