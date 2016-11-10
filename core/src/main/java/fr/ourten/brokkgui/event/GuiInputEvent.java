package fr.ourten.brokkgui.event;

import fr.ourten.brokkgui.component.GuiNode;
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