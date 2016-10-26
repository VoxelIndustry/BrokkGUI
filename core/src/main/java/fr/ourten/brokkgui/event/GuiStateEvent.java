package fr.ourten.brokkgui.event;

import fr.ourten.brokkgui.component.GuiNode;

public class GuiStateEvent extends BrokkGuiEvent
{
    public static final EventType<GuiStateEvent> ANY = new EventType<>("STATE_EVENT");

    public GuiStateEvent(final GuiNode source)
    {
        super(source);
    }
}