package org.yggard.brokkgui.event;

import org.yggard.brokkgui.control.GuiButtonBase;
import org.yggard.hermod.EventType;

public class ActionEvent extends GuiInputEvent
{
    public static final EventType<ActionEvent> TYPE = new EventType<>(GuiInputEvent.ANY, "ACTION_INPUT_EVENT");

    public ActionEvent(final GuiButtonBase source)
    {
        super(source);
    }
}