package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;
import net.voxelindustry.hermod.IEventEmitter;

public abstract class GuiComponentEvent<T extends GuiComponent> extends HermodEvent
{
    public static final EventType<GuiComponentEvent<?>> ANY = new EventType<>("GUI_COMPONENT_EVENT");

    private final T component;

    public GuiComponentEvent(IEventEmitter source, T component)
    {
        super(source);

        this.component = component;
    }

    public T component()
    {
        return component;
    }
}
