package net.voxelindustry.brokkgui.component.impl;

import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.event.ActionEvent;
import net.voxelindustry.brokkgui.event.ClickEvent;

public class ButtonComponent extends GuiComponent
{
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
}
