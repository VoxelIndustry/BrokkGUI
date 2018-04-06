package org.yggard.brokkgui.control;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.element.GuiLabel;
import org.yggard.brokkgui.event.ActionEvent;
import org.yggard.hermod.EventHandler;

import javax.annotation.Nonnull;

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

    public String getText()
    {
        return label.getText();
    }

    public void setText(@Nonnull String text)
    {
        label.setText(text);
    }

    public String getEllipsis()
    {
        return label.getEllipsis();
    }

    public void setEllipsis(String ellipsis)
    {
        label.setEllipsis(ellipsis);
    }

    public GuiNode getIcon()
    {
        return label.getIcon();
    }

    public void setIcon(GuiNode icon)
    {
        label.setIcon(icon);
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