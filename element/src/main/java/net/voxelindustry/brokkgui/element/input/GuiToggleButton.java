package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.ToggleButtonComponent;
import net.voxelindustry.brokkgui.control.GuiButtonBase;
import net.voxelindustry.brokkgui.event.SelectEvent;
import net.voxelindustry.hermod.EventHandler;

public class GuiToggleButton extends GuiButtonBase
{
    private ToggleButtonComponent toggleButtonComponent;

    public GuiToggleButton(String text, GuiElement icon)
    {
        super(text, icon);
    }

    public GuiToggleButton(String text)
    {
        this(text, null);
    }

    public GuiToggleButton()
    {
        this("");
    }

    @Override
    public String type()
    {
        return "button";
    }

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        toggleButtonComponent = provide(ToggleButtonComponent.class);
    }

    public ToggleButtonComponent toggleButtonComponent()
    {
        return toggleButtonComponent;
    }

    public void setToggleGroup(GuiToggleGroup group)
    {
        toggleButtonComponent.setToggleGroup(group);
    }

    public GuiToggleGroup getToggleGroup()
    {
        return toggleButtonComponent.getToggleGroup();
    }

    public Property<Boolean> getSelectedProperty()
    {
        return toggleButtonComponent.getSelectedProperty();
    }

    public Property<GuiToggleGroup> getToggleGroupProperty()
    {
        return toggleButtonComponent.toggleGroupProperty();
    }

    public boolean setSelected(boolean selected)
    {
        return toggleButtonComponent.setSelected(selected);
    }

    public void setOnSelectEvent(EventHandler<SelectEvent> onSelectEvent)
    {
        toggleButtonComponent.setOnSelectEvent(onSelectEvent);
    }
}