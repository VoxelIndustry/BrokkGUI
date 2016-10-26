package fr.ourten.brokkgui.behavior;

import fr.ourten.brokkgui.control.GuiButtonBase;
import fr.ourten.brokkgui.event.ClickEvent;

public class GuiTogglableButtonBehavior<C extends GuiButtonBase & IGuiTogglable> extends GuiButtonBehavior<C>
{
    public GuiTogglableButtonBehavior(final C model)
    {
        super(model);
    }

    @Override
    public void onClick(final ClickEvent.Left event)
    {
        if (!this.getModel().isDisabled())
            this.getModel().setSelected(!this.getModel().isSelected());
    }
}