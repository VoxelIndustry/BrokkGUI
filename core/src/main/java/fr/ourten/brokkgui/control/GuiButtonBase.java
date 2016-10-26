package fr.ourten.brokkgui.control;

import fr.ourten.brokkgui.event.ActionEvent;
import fr.ourten.brokkgui.event.EventHandler;
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
        return this.activatedProperty.getValue();
    }

    public void setActivated(final boolean activated)
    {
        this.activatedProperty.setValue(activated);
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
