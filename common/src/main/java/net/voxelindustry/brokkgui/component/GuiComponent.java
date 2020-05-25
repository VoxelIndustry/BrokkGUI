package net.voxelindustry.brokkgui.component;

import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.hermod.EventDispatcher;
import net.voxelindustry.hermod.IEventEmitter;

public abstract class GuiComponent implements IEventEmitter
{
    private GuiElement element;
    private Transform  transform;

    public void attach(GuiElement element)
    {
        this.element = element;
        transform = element.transform();
    }

    public GuiElement element()
    {
        return element;
    }

    public boolean hasElement()
    {
        return element == null;
    }

    public Transform transform()
    {
        return transform;
    }

    @Override
    public EventDispatcher getEventDispatcher()
    {
        return element().getEventDispatcher();
    }
}
