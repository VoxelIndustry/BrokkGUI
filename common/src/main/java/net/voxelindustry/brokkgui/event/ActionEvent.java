package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.control.GuiButtonBase;
import net.voxelindustry.hermod.EventType;

public class ActionEvent extends GuiInputEvent
{
    public static final EventType<ActionEvent> TYPE = new EventType<>(ANY, "ACTION_INPUT_EVENT");

    public ActionEvent(final GuiButtonBase source)
    {
        super(source);
    }
}