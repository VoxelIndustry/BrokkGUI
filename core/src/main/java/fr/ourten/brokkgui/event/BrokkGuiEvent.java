package fr.ourten.brokkgui.event;

import fr.ourten.brokkgui.component.GuiNode;

public class BrokkGuiEvent
{
    private final GuiNode source;

    public BrokkGuiEvent(final GuiNode source)
    {
        this.source = source;
    }

    public GuiNode getSource()
    {
        return this.source;
    }
}