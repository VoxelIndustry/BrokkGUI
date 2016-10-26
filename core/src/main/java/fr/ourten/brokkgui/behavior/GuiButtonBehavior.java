package fr.ourten.brokkgui.behavior;

import fr.ourten.brokkgui.control.GuiButtonBase;
import fr.ourten.brokkgui.event.ClickEvent;

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