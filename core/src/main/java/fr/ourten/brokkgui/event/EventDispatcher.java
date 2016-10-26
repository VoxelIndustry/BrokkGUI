package fr.ourten.brokkgui.event;

import java.util.HashMap;

import com.google.common.collect.Maps;

public class EventDispatcher
{
    private final HashMap<EventType<? extends BrokkGuiEvent>, EventHandlerWrapper<? extends BrokkGuiEvent>> handlers;

    public EventDispatcher()
    {
        this.handlers = Maps.newHashMap();
    }

    public void dispatchEvent(final EventType<? extends BrokkGuiEvent> type, final BrokkGuiEvent event)
    {
        this.handlers.forEach((eventType, handler) ->
        {
            if (type == eventType || type.isSubType(eventType))
                handler.handle(event);
        });
    }

    public <T extends BrokkGuiEvent> void addHandler(final EventType<T> type, final EventHandler<? super T> handler)
    {
        final EventHandlerWrapper<T> wrapperHandler = this.internalGetHandler(type);
        wrapperHandler.addHandler(handler);
    }

    @SuppressWarnings("unchecked")
    public <T extends BrokkGuiEvent> void removeHandler(final EventType<T> type, final EventHandler<T> handler)
    {
        final EventHandlerWrapper<T> wrapperHandler = (EventHandlerWrapper<T>) this.handlers.get(type);

        if (wrapperHandler != null)
            wrapperHandler.removeHandler(handler);
    }

    @SuppressWarnings("unchecked")
    private <T extends BrokkGuiEvent> EventHandlerWrapper<T> internalGetHandler(final EventType<T> type)
    {
        EventHandlerWrapper<T> wrapperHandler = (EventHandlerWrapper<T>) this.handlers.get(type);
        if (wrapperHandler == null)
        {
            wrapperHandler = new EventHandlerWrapper<>();
            this.handlers.put(type, wrapperHandler);
        }
        return wrapperHandler;
    }
}