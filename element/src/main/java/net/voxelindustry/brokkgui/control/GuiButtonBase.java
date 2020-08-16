package net.voxelindustry.brokkgui.control;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.ButtonComponent;
import net.voxelindustry.brokkgui.event.ActionEvent;
import net.voxelindustry.hermod.EventHandler;

public abstract class GuiButtonBase extends GuiLabeled
{
    private ButtonComponent buttonComponent;

    public GuiButtonBase(String text, GuiElement icon)
    {
        super(text, icon);
    }

    public GuiButtonBase(String text)
    {
        this(text, null);
    }

    public GuiButtonBase()
    {
        this("");
    }

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        buttonComponent = provide(ButtonComponent.class);
    }

    public ButtonComponent buttonComponent()
    {
        return buttonComponent;
    }

    public void activate()
    {
        buttonComponent().activate();
    }

    public void setOnActionEvent(EventHandler<ActionEvent> onActionEvent)
    {
        buttonComponent().setOnActionEvent(onActionEvent);
    }
}