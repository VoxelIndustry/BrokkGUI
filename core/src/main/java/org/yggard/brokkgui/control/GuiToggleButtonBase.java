package org.yggard.brokkgui.control;

import org.yggard.brokkgui.behavior.IGuiTogglable;

import fr.ourten.teabeans.value.BaseProperty;

public abstract class GuiToggleButtonBase extends GuiButtonBase implements IGuiTogglable
{
    private final BaseProperty<Boolean>        selectedProperty;
    private final BaseProperty<GuiToggleGroup> toggleGroupProperty;

    public GuiToggleButtonBase(final String label)
    {
        super(label);

        this.selectedProperty = new BaseProperty<>(false, "selectedProperty");
        this.toggleGroupProperty = new BaseProperty<>(null, "toggleGroupProperty");
    }

    public GuiToggleButtonBase()
    {
        this("");
    }

    public void setToggleGroup(final GuiToggleGroup group)
    {
        this.getToggleGroupProperty().setValue(group);
        group.addButton(this);
    }

    @Override
    public GuiToggleGroup getToggleGroup()
    {
        return this.getToggleGroupProperty().getValue();
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