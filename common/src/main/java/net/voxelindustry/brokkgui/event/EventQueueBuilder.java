package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.hermod.EventQueue;
import net.voxelindustry.hermod.IEventEmitter;

import java.util.function.Predicate;

public class EventQueueBuilder
{
    public static EventQueue singleton(IEventEmitter emitter)
    {
        return new EventQueue().addDispatcher(emitter.getEventDispatcher());
    }

    public static EventQueue fromTarget(GuiElement target)
    {
        EventQueue eventQueue = new EventQueue();
        eventQueue.addDispatcher(target.getEventDispatcher());

        Transform node = target.transform().parent();
        while (node != null)
        {
            eventQueue.addDispatcherFirst(node.getEventDispatcher());
            node = node.parent();
        }

        return eventQueue;
    }

    public static EventQueue allChildren(GuiElement root)
    {
        EventQueue eventQueue = new EventQueue();
        eventQueue.addDispatcher(root.getEventDispatcher());

        allChildren(root.transform(), eventQueue);
        return eventQueue;
    }

    private static void allChildren(Transform root, EventQueue eventQueue)
    {
        for (Transform child : root.children())
            eventQueue.addDispatcher(child.getEventDispatcher());

        for (Transform child : root.children())
            allChildren(child, eventQueue);
    }

    public static EventQueue allChildrenMatching(GuiElement root, Predicate<GuiElement> predicate)
    {
        EventQueue eventQueue = new EventQueue();
        eventQueue.addDispatcher(root.getEventDispatcher());

        allChildrenMatching(root.transform(), predicate, eventQueue);
        return eventQueue;
    }

    private static void allChildrenMatching(Transform root, Predicate<GuiElement> predicate, EventQueue eventQueue)
    {
        for (Transform child : root.children())
        {
            if (predicate.test(child.element()))
                eventQueue.addDispatcher(child.getEventDispatcher());
        }

        for (Transform child : root.children())
        {
            if (predicate.test(child.element()))
                allChildren(child, eventQueue);
        }
    }

    public static Predicate<GuiElement> isPointInside(float pointX, float pointY)
    {
        return element -> element.transform().isPointInside(pointX, pointY);
    }

    public static Predicate<GuiElement> isEnabled   = element -> !element.isDisabled();
    public static Predicate<GuiElement> isDisabled  = GuiElement::isDisabled;
    public static Predicate<GuiElement> isVisible   = GuiElement::isVisible;
    public static Predicate<GuiElement> isInvisible = element -> !element.isVisible();
}
