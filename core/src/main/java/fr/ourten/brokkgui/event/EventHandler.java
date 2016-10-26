package fr.ourten.brokkgui.event;

@FunctionalInterface
public interface EventHandler<T extends BrokkGuiEvent>
{
    void handle(T event);
}