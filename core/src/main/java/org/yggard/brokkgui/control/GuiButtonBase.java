package org.yggard.brokkgui.control;

import org.yggard.brokkgui.event.ActionEvent;
import org.yggard.hermod.EventHandler;

import fr.ourten.teabeans.value.BaseProperty;

public abstract class GuiButtonBase extends GuiLabeled
{
    private final BaseProperty<Boolean> activatedProperty;

    private EventHandler<ActionEvent>   onActionEvent;

    public GuiButtonBase(final String text)
    {
        super(text);
        this.activatedProperty = new BaseProperty<>(false, "activatedProperty");
    }

    public GuiButtonBase()
    {
        this("");
    }

    public BaseProperty<Boolean> getActivatedProperty()
    {
        return this.activatedProperty;
    }

    public boolean isActivated()
    {
        return this.getActivatedProperty().getValue();
    }

    public void setActivated(final boolean activated)
    {
        this.getActivatedProperty().setValue(activated);
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
