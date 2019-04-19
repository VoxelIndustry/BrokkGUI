package net.voxelindustry.brokkgui.control;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.event.ActionEvent;
import net.voxelindustry.hermod.EventHandler;

import javax.annotation.Nonnull;

public abstract class GuiButtonBase extends GuiElement
{
    private EventHandler<ActionEvent> onActionEvent;

    private final GuiLabeled label;

    private final BaseProperty<Boolean> expandToLabel;

    public GuiButtonBase(String type, String text, GuiNode icon)
    {
        super(type);

        this.label = this.createGuiLabel(text, icon);
        this.addChild(this.label);

        this.expandToLabel = new BaseProperty<>(true, "expandToLabelProperty");
    }

    public GuiButtonBase(String type, String text)
    {
        this(type, text, null);
    }

    public GuiButtonBase(String type)
    {
        this(type, "");
    }

    protected abstract GuiLabeled createGuiLabel(String text, GuiNode icon);

    @Override
    public void setWidth(final float width)
    {
        if (this.getWidthProperty().isBound() && this.expandToLabel())
            this.getWidthProperty().unbind();
        super.setWidth(width);
    }

    @Override
    public void setHeight(final float height)
    {
        if (this.getHeightProperty().isBound() && this.expandToLabel())
            this.getHeightProperty().unbind();
        super.setHeight(height);
    }

    public void activate()
    {
        this.getEventDispatcher().dispatchEvent(ActionEvent.TYPE, new ActionEvent(this));
    }

    public GuiLabeled getLabel()
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