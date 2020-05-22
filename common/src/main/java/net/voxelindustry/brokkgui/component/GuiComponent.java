package net.voxelindustry.brokkgui.component;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.hermod.EventDispatcher;
import net.voxelindustry.hermod.IEventEmitter;

public abstract class GuiComponent implements IEventEmitter
{
    private BaseProperty<GuiElement> elementProperty;

    public GuiComponent()
    {
        this.elementProperty = new BaseProperty<>(null, "elementProperty");
    }

    public void attach(GuiElement element)
    {
        this.elementProperty.setValue(element);
    }

    public GuiElement element()
    {
        return this.elementProperty.getValue();
    }

    public boolean hasElement()
    {
        return this.elementProperty.isPresent();
    }

    public BaseProperty<GuiElement> getElementProperty()
    {
        return this.elementProperty;
    }

    public Transform transform()
    {
        return this.element().transform();
    }

    @Override
    public EventDispatcher getEventDispatcher()
    {
        return element().getEventDispatcher();
    }
}
