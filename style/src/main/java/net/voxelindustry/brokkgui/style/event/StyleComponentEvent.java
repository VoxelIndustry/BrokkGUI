package net.voxelindustry.brokkgui.style.event;

import net.voxelindustry.brokkgui.event.GuiComponentEvent;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;
import net.voxelindustry.hermod.IEventEmitter;

public class StyleComponentEvent extends GuiComponentEvent<StyleComponent>
{
    public static final EventType<StyleComponentEvent> TYPE = new EventType<>("STYLE_COMPONENT_EVENT");

    public StyleComponentEvent(IEventEmitter source, StyleComponent component)
    {
        super(source, component);
    }

    @Override
    public HermodEvent copy(IEventEmitter source)
    {
        return new StyleComponentEvent(source, component());
    }
}
