package fr.ourten.brokkgui.event;

import java.util.List;

import com.google.common.collect.Lists;

public class EventHandlerWrapper<T extends BrokkGuiEvent>
{
    private final List<EventHandler<? super T>> eventHandlers;

    public EventHandlerWrapper()
    {
        this.eventHandlers = Lists.newArrayList();
    }

    @SuppressWarnings("unchecked")
    public void handle(final BrokkGuiEvent event)
    {
        final T internalEvent = (T) event;
        this.eventHandlers.forEach(handler -> handler.handle(internalEvent));
    }

    public void addHandler(final EventHandler<? super T> handler)
    {
        this.eventHandlers.add(handler);
    }

    public void removeHandler(final EventHandler<? super T> handler)
    {
        this.eventHandlers.remove(handler);
    }

    public boolean containsHandler(final EventHandler<? super T> handler)
    {
        return this.eventHandlers.contains(handler);
    }
}