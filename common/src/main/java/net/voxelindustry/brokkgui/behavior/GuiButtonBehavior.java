package net.voxelindustry.brokkgui.behavior;

import net.voxelindustry.brokkgui.control.GuiButtonBase;
import net.voxelindustry.brokkgui.event.ClickEvent;

public class GuiButtonBehavior<C extends GuiButtonBase> extends GuiBehaviorBase<C>
{
    public GuiButtonBehavior(final C model)
    {
        super(model);

        this.getModel().getEventDispatcher().addHandler(ClickEvent.Left.TYPE, this::onClick);
    }

    public void onClick(final ClickEvent.Left event)
    {
        if (!this.getModel().isDisabled())
            this.getModel().activate();
    }
}