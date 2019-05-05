package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class ActionEvent extends GuiInputEvent
{
    public static final EventType<ActionEvent> TYPE = new EventType<>(ANY, "ACTION_INPUT_EVENT");

    public ActionEvent(GuiElement source)
    {
        super(source);
    }

    @Override
    public ActionEvent copy(IEventEmitter source)
    {
        return new ActionEvent((GuiElement) source);
    }
}