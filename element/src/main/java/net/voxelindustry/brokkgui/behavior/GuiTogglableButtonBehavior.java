package net.voxelindustry.brokkgui.behavior;

import net.voxelindustry.brokkgui.behavior.GuiButtonBehavior;
import net.voxelindustry.brokkgui.element.IGuiTogglable;
import net.voxelindustry.brokkgui.control.GuiButtonBase;
import net.voxelindustry.brokkgui.event.ClickEvent;

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