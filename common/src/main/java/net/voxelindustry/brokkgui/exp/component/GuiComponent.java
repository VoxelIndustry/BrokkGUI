package net.voxelindustry.brokkgui.exp.component;

import fr.ourten.teabeans.value.BaseProperty;

public abstract class GuiComponent
{
    private BaseProperty<GuiElement> elementProperty;

    public GuiComponent()
    {

    }

    protected void attach(GuiElement element)
    {
        this.elementProperty.setValue(element);
    }

    public GuiElement getElement()
    {
        return this.elementProperty.getValue();
    }

    public BaseProperty<GuiElement> getElementProperty()
    {
        return this.elementProperty;
    }
}
