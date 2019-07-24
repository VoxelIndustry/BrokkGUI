package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;
import net.voxelindustry.hermod.IEventEmitter;

import javax.annotation.Nullable;

public class ComponentEvent extends HermodEvent
{
    public static final EventType<ComponentEvent>         ANY     = new EventType<>("COMPONENT_EVENT");
    public static final EventType<ComponentEvent.Added>   ADDED   = new EventType<>(ANY, "COMPONENT_ADDED_EVENT");
    public static final EventType<ComponentEvent.Removed> REMOVED = new EventType<>(ANY, "COMPONENT_REMOVED_EVENT_");

    @Nullable
    private final GuiComponent component;

    public ComponentEvent(GuiElement source, GuiComponent component)
    {
        super(source);

        this.component = component;
    }

    @Override
    public ComponentEvent copy(IEventEmitter source)
    {
        return new ComponentEvent((GuiElement) source, component);
    }

    @Nullable
    public GuiComponent getComponent()
    {
        return component;
    }

    public static class Added extends ComponentEvent
    {
        public Added(GuiElement source, GuiComponent component)
        {
            super(source, component);
        }

        @Override
        public Added copy(IEventEmitter source)
        {
            return new Added((GuiElement) source, getComponent());
        }
    }

    public static class Removed extends ComponentEvent
    {
        public Removed(GuiElement source, GuiComponent component)
        {
            super(source, component);
        }

        @Override
        public Removed copy(IEventEmitter source)
        {
            return new Removed((GuiElement) source, getComponent());
        }
    }
}
