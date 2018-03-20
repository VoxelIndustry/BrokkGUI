package org.yggard.brokkgui.control;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.element.GuiLabel;
import org.yggard.brokkgui.event.ActionEvent;
import org.yggard.hermod.EventHandler;

public abstract class GuiButtonBase extends GuiControl
{
    private EventHandler<ActionEvent> onActionEvent;

    private final GuiLabel label;

    private final BaseProperty<Boolean> expandToLabel;

    public GuiButtonBase(String type, String text, GuiNode icon)
    {
        super(type);

        this.label = new GuiLabel(text, icon);
        this.addChild(this.label);

        this.expandToLabel = new BaseProperty<>(false, "expandToLabelProperty");
    }

    public GuiButtonBase(String type, String text)
    {
        this(type, text, null);
    }

    public GuiButtonBase(String type)
    {
        this(type, "");
    }

    public void activate()
    {
        this.getEventDispatcher().dispatchEvent(ActionEvent.TYPE, new ActionEvent(this));
    }

    public GuiLabel getLabel()
    {
        return label;
    }

    public BaseProperty<Boolean> getExpandToLabelProperty()
    {
        return expandToLabel;
    }

    public void setExpandToLabel(boolean expandToLabel)
    {
        this.getExpandToLabelProperty().setValue(expandToLabel);
    }

    public boolean expandToLabel()
    {
        return this.getExpandToLabelProperty().getValue();
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

    public void setOnActionEvent(final EventHandler<ActionEvent> onActionEvent)
    {
        this.getEventDispatcher().removeHandler(ActionEvent.TYPE, this.onActionEvent);
        this.onActionEvent = onActionEvent;
        this.getEventDispatcher().addHandler(ActionEvent.TYPE, this.onActionEvent);
    }
}