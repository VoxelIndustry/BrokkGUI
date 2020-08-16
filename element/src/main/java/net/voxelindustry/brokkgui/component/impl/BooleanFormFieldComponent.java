package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectSide;

public class BooleanFormFieldComponent extends GuiComponent
{
    private final Property<RectSide>   buttonSideProperty;
    private final Property<GuiElement> buttonNodeProperty;

    public BooleanFormFieldComponent()
    {
        buttonSideProperty = new Property<>(RectSide.LEFT);
        buttonNodeProperty = new Property<>(null);
    }

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        if (!element.has(ToggleButtonComponent.class))
            throw new GuiComponentException("Cannot attach RadioButtonComponent to an element not having ToggleButtonComponent!");
    }

    public Property<RectSide> getButtonSideProperty()
    {
        return buttonSideProperty;
    }

    public Property<GuiElement> getButtonNodeProperty()
    {
        return buttonNodeProperty;
    }
}
