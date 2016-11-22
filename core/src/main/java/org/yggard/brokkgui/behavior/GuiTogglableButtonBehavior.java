package org.yggard.brokkgui.behavior;

import org.yggard.brokkgui.control.GuiButtonBase;
import org.yggard.brokkgui.event.ClickEvent;

public class GuiTogglableButtonBehavior<C extends GuiButtonBase & IGuiTogglable> extends GuiButtonBehavior<C>
{
    public GuiTogglableButtonBehavior(final C model)
    {
        super(model);
    }

    @Override
    public void onClick(final ClickEvent.Left event)
    {
        super.onClick(event);

        if (!this.getModel().isDisabled())
            this.getModel().setSelected(!this.getModel().isSelected());
    }
}