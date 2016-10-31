package fr.ourten.brokkgui.event;

public class BrokkGuiEvent
{
    private final IGuiEventEmitter source;

    public BrokkGuiEvent(final IGuiEventEmitter source)
    {
        this.source = source;
    }

    public IGuiEventEmitter getSource()
    {
        return this.source;
    }
}