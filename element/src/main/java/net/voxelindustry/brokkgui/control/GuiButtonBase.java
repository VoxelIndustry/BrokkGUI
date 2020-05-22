package net.voxelindustry.brokkgui.control;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.event.ActionEvent;
import net.voxelindustry.hermod.EventHandler;

import javax.annotation.Nonnull;

public abstract class GuiButtonBase extends GuiSkinedElement
{
    private EventHandler<ActionEvent> onActionEvent;

    private final GuiLabeled label;

    private final BaseProperty<Boolean> expandToLabel;

    public GuiButtonBase(String text, GuiElement icon)
    {
        label = createGuiLabel(text, icon);
        addChild(label);

        expandToLabel = new BaseProperty<>(true, "expandToLabelProperty");
    }

    public GuiButtonBase(String text)
    {
        this(text, null);
    }

    public GuiButtonBase()
    {
        this("");
    }

    protected abstract GuiLabeled createGuiLabel(String text, GuiElement icon);

    public void activate()
    {
        getEventDispatcher().dispatchEvent(ActionEvent.TYPE, new ActionEvent(this));
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
        getExpandToLabelProperty().setValue(expandToLabel);
    }

    public boolean expandToLabel()
    {
        return getExpandToLabelProperty().getValue();
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

    public GuiElement getIcon()
    {
        return label.getIcon();
    }

    public void setIcon(GuiElement icon)
    {
        label.setIcon(icon);
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

    public void setOnActionEvent(EventHandler<ActionEvent> onActionEvent)
    {
        getEventDispatcher().removeHandler(ActionEvent.TYPE, this.onActionEvent);
        this.onActionEvent = onActionEvent;
        getEventDispatcher().addHandler(ActionEvent.TYPE, this.onActionEvent);
    }
}