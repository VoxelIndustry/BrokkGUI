package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.ButtonComponent;
import net.voxelindustry.brokkgui.control.GuiButtonBase;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.IEventEmitter;

public class ActionEvent extends GuiInputEvent
{
    public static final EventType<ActionEvent> TYPE = new EventType<>(ANY, "ACTION_INPUT_EVENT");

    private final ButtonComponent button;

    public ActionEvent(GuiElement source, ButtonComponent button)
    {
        super(source);
        this.button = button;
    }

    @Override
    public ActionEvent copy(IEventEmitter source)
    {
        return new ActionEvent((GuiButtonBase) source, button);
    }
}