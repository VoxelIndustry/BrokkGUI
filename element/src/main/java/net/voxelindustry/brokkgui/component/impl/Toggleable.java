package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.element.input.ToggleGroup;
import net.voxelindustry.brokkgui.style.StyleHolder;

public class Toggleable extends GuiComponent
{
    private final BaseProperty<ToggleGroup> toggleGroupProperty;
    private final BaseProperty<Boolean>     selectedProperty;

    private StyleHolder styleHolder;

    public Toggleable()
    {
        this.selectedProperty = new BaseProperty<>(false, "selectedProperty");
        this.toggleGroupProperty = new BaseProperty<>(null, "toggleGroupProperty");

        this.selectedProperty.addListener((obs, oldValue, newValue) ->
        {
            if (styleHolder == null)
                return;

            if (newValue)
                styleHolder.activePseudoClass().add("active");
            else
                styleHolder.activePseudoClass().remove("active");
        });
    }

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        if (element != null)
            styleHolder = element.get(StyleHolder.class);
    }

    public void toggleGroup(final ToggleGroup group)
    {
        this.toggleGroupProperty().setValue(group);
        group.addButton(this);
    }

    /**
     * @return the ToggleGroup to this Toggleable element belongs.
     */
    public ToggleGroup toggleGroup()
    {
        return this.toggleGroupProperty().getValue();
    }

    /**
     * @return the internal selected property of the Toggleable object.
     */
    public BaseProperty<Boolean> selectedProperty()
    {
        return this.selectedProperty;
    }

    public BaseProperty<ToggleGroup> toggleGroupProperty()
    {
        return this.toggleGroupProperty;
    }

    /**
     * @param selected boolean
     * @return the state after ToggleGroup checks. Useful when the
     * ToggleGroup might disable empty selection or when this Toggleable
     * is already selected.
     */
    public boolean select(boolean selected)
    {
        if (!selected && this.selected())
            return this.internalUnselect();
        if (selected && !this.selected())
            this.internalSelect();

        return this.selected();
    }

    /**
     * @return the state this Toggleable is in after the select action
     */
    private boolean internalSelect()
    {
        if (this.toggleGroup() == null)
        {
            this.selectedProperty().setValue(true);
            return true;
        }
        else
        {
            if (this.toggleGroup().selectedButton() == this)
                return false;
            this.toggleGroup().selectedButton(this);
            return true;
        }
    }

    /**
     * @return the state this Toggleable is in after the unselect action
     */
    private boolean internalUnselect()
    {
        if (this.toggleGroup() == null)
        {
            this.selectedProperty().setValue(false);
            return false;
        }
        if (this.toggleGroup().selectedButton() == this && !this.toggleGroup().allowNothing())
            return true;
        this.toggleGroup().selectedButton(null);
        return false;
    }

    /**
     * @return the value of the selected property. When a ToggleGroup is set
     * you are guaranteed that it's the group selected Toggleable.
     */
    public boolean selected()
    {
        return this.selectedProperty().getValue();
    }
}
