package net.voxelindustry.brokkgui.component.impl;

import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.event.ActionEvent;
import net.voxelindustry.brokkgui.event.ClickEvent;
import net.voxelindustry.hermod.EventHandler;

public class ButtonComponent extends GuiComponent
{
    private EventHandler<ActionEvent> onActionEvent;

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        element.getEventDispatcher().addHandler(ClickEvent.Left.TYPE, this::onClick);
    }

    public void onClick(ClickEvent.Left event)
    {
        if (!element().isDisabled())
            activate();
    }

    public void activate()
    {
        getEventDispatcher().dispatchEvent(ActionEvent.TYPE, new ActionEvent(element(), this));
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
