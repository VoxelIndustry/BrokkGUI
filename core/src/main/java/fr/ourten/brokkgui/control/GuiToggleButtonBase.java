package fr.ourten.brokkgui.control;

import fr.ourten.brokkgui.behavior.IGuiTogglable;
import fr.ourten.teabeans.value.BaseProperty;

public abstract class GuiToggleButtonBase extends GuiButtonBase implements IGuiTogglable
{
    private final BaseProperty<Boolean>        selectedProperty;
    private final BaseProperty<GuiToggleGroup> toggleGroupProperty;

    public GuiToggleButtonBase()
    {
        super();
        this.selectedProperty = new BaseProperty<>(false, "selectedProperty");
        this.toggleGroupProperty = new BaseProperty<>(null, "toggleGroupProperty");
    }

    @Override
    public boolean setSelected(final boolean selected)
    {
        if (!selected)
            if (this.getToggleGroup() == null || this.getToggleGroup().allowNothing()
                    || this.getToggleGroup().getSelectedButton() != this)
                this.selectedProperty.setValue(false);
        if (selected)
            if (this.getToggleGroup() != null && this.getToggleGroup().getSelectedButton() != this)
            {
                this.getToggleGroup().setSelectedButton(this);
                return true;
            }
        return false;
    }

    @Override
    public boolean isSelected()
    {
        return this.selectedProperty.getValue();
    }

    public void setToggleGroup(final GuiToggleGroup group)
    {
        this.toggleGroupProperty.setValue(group);
        group.addButton(this);
    }

    @Override
    public GuiToggleGroup getToggleGroup()
    {
        return this.toggleGroupProperty.getValue();
    }

    @Override
    public BaseProperty<Boolean> getSelectedProperty()
    {
        return this.selectedProperty;
    }

    public BaseProperty<GuiToggleGroup> getToggleGroupProperty()
    {
        return this.toggleGroupProperty;
    }
}