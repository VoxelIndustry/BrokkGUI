package fr.ourten.brokkgui.event;

import fr.ourten.brokkgui.component.GuiNode;

public class GuiInputEvent extends BrokkGuiEvent
{
    public static final EventType<GuiInputEvent> ANY = new EventType<>("INPUT_EVENT");

    public GuiInputEvent(final GuiNode source)
    {
        super(source);
    }
}