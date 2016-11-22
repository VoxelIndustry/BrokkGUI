package org.yggard.brokkgui.control;

import org.yggard.brokkgui.event.ActionEvent;
import org.yggard.hermod.EventHandler;

public abstract class GuiButtonBase extends GuiLabeled
{
    private EventHandler<ActionEvent> onActionEvent;

    public GuiButtonBase(final String text)
    {
        super(text);
    }

    public GuiButtonBase()
    {
        this("");
    }

    public void activate()
    {
        this.getEventDispatcher().dispatchEvent(ActionEvent.TYPE, new ActionEvent(this));
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

    public void setOnActionEvent(final EventHandler<ActionEvent> onActionEvent)
    {
        this.getEventDispatcher().addHandler(ActionEvent.TYPE, this.onActionEvent);
        this.onActionEvent = onActionEvent;
        this.getEventDispatcher().removeHandler(ActionEvent.TYPE, this.onActionEvent);
    }
}