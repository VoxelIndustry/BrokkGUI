package org.yggard.brokkgui.behavior;

import org.yggard.brokkgui.control.GuiButtonBase;
import org.yggard.brokkgui.event.ClickEvent;

public class GuiButtonBehavior<C extends GuiButtonBase> extends GuiBehaviorBase<C>
{
    public GuiButtonBehavior(final C model)
    {
        super(model);

        this.getModel().getEventDispatcher().addHandler(ClickEvent.Left.TYPE, this::onClick);
    }

    public void onClick(final ClickEvent.Left event)
    {

    }
}