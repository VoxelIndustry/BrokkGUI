package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.exp.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;
import net.voxelindustry.hermod.IEventEmitter;

public class GuiStateEvent extends HermodEvent
{
    public static final EventType<GuiStateEvent> ANY = new EventType<>("STATE_EVENT");

    public GuiStateEvent(GuiElement source)
    {
        super(source);
    }

    @Override
    public GuiStateEvent copy(IEventEmitter source)
    {
        return new GuiStateEvent((GuiElement) source);
    }
}