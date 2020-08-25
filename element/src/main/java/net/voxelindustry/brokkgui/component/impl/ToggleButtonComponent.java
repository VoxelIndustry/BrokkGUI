package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.element.IGuiToggleable;
import net.voxelindustry.brokkgui.element.input.GuiToggleGroup;
import net.voxelindustry.brokkgui.event.ActionEvent;
import net.voxelindustry.brokkgui.event.SelectEvent;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.hermod.EventHandler;

public class ToggleButtonComponent extends GuiComponent implements IGuiToggleable
{
    private ButtonComponent button;
    private StyleComponent  style;

    private final Property<GuiToggleGroup>  toggleGroupProperty;
    private final Property<Boolean>         selectedProperty;
    private       EventHandler<SelectEvent> onSelectEvent;

    public ToggleButtonComponent()
    {
        selectedProperty = new Property<>(false);
        toggleGroupProperty = new Property<>(null);
    }

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        button = element.get(ButtonComponent.class);

        if (button == null)
            throw new GuiComponentException("ToggleButtonComponent must be applied to an element after a ButtonComponent. element=" + element.getClass().getSimpleName());

        style = element.get(StyleComponent.class);

        if (style == null)
            throw new GuiComponentException("ToggleButtonComponent must be applied to an element after a StyleComponent. element=" + element.getClass().getSimpleName());

        selectedProperty.addListener((obs, oldValue, newValue) ->
        {
            if (newValue)
                style.activePseudoClass().add("active");
            else
                style.activePseudoClass().remove("active");
            getEventDispatcher().dispatchEvent(SelectEvent.TYPE, new SelectEvent(element(), isSelected()));
        });

        getEventDispatcher().addHandler(ActionEvent.TYPE, this::onAction);
    }

    private void onAction(ActionEvent event)
    {
        if (!element().isDisabled())
            setSelected(!isSelected());
    }

    @Override
    public GuiToggleGroup getToggleGroup()
    {
        return toggleGroupProperty().getValue();
    }

    @Override
    public void setToggleGroup(GuiToggleGroup group)
    {
        toggleGroupProperty().setValue(group);
        group.addButton(this);
    }

    @Override
    public boolean setSelected(boolean selected)
    {
        if (!selected && isSelected())
        {
            if (getToggleGroup() == null)
            {
                getSelectedProperty().setValue(false);
                return false;
            }
            if (getToggleGroup().getSelectedButton() == this && !getToggleGroup().allowNothing())
                return true;
            getToggleGroup().setSelectedButton(null);
            return false;
        }
        if (selected && !isSelected())
        {
            if (getToggleGroup() == null)
            {
                getSelectedProperty().setValue(true);
                return true;
            }
            else
            {
                if (getToggleGroup().getSelectedButton() == this)
                    return true;
                getToggleGroup().setSelectedButton(this);
                return true;
            }
        }
        return isSelected();
    }

    @Override
    public Property<Boolean> getSelectedProperty()
    {
        return selectedProperty;
    }

    public Property<GuiToggleGroup> toggleGroupProperty()
    {
        return toggleGroupProperty;
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

    public void setOnSelectEvent(EventHandler<SelectEvent> onSelectEvent)
    {
        getEventDispatcher().removeHandler(SelectEvent.TYPE, this.onSelectEvent);
        this.onSelectEvent = onSelectEvent;
        getEventDispatcher().addHandler(SelectEvent.TYPE, this.onSelectEvent);
    }
}