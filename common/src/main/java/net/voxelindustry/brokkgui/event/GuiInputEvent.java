package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;
import net.voxelindustry.hermod.IEventEmitter;

public class GuiInputEvent extends HermodEvent
{
    public static final EventType<GuiInputEvent> ANY = new EventType<>("INPUT_EVENT");

    public GuiInputEvent(final GuiElement source)
    {
        super(source);
    }

    @Override
    public GuiInputEvent copy(IEventEmitter source)
    {
        return new GuiInputEvent((GuiElement) source);
    }
}