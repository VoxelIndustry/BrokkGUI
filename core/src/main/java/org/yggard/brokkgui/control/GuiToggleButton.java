package org.yggard.brokkgui.control;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.behavior.GuiTogglableButtonBehavior;
import org.yggard.brokkgui.behavior.IGuiTogglable;
import org.yggard.brokkgui.skin.GuiButtonSkin;
import org.yggard.brokkgui.skin.GuiSkinBase;

public class GuiToggleButton extends GuiButtonBase implements IGuiTogglable
{
    private final BaseProperty<Boolean>        selectedProperty;
    private final BaseProperty<GuiToggleGroup> toggleGroupProperty;

    public GuiToggleButton(final String type, final String label)
    {
        super(type, label);

        this.selectedProperty = new BaseProperty<>(false, "selectedProperty");
        this.toggleGroupProperty = new BaseProperty<>(null, "toggleGroupProperty");

        this.selectedProperty.addListener((obs, oldValue, newValue) ->
        {
            if (newValue)
                this.getActivePseudoClass().add("active");
            else
                this.getActivePseudoClass().remove("active");
        });
    }

    public GuiToggleButton(String type)
    {
        this(type, "");
    }

    public GuiToggleButton()
    {
        this("button", "");
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiButtonSkin<>(this, new GuiTogglableButtonBehavior<>(this));
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

    @Override
    public boolean setSelected(boolean selected)
    {
        if (!selected && this.isSelected())
        {
            if (this.getToggleGroup() == null)
            {
                this.getSelectedProperty().setValue(false);
                return false;
            }
            if (this.getToggleGroup().getSelectedButton() == this && !this.getToggleGroup().allowNothing())
                return true;
            this.getToggleGroup().setSelectedButton(null);
            return false;
        }
        if (selected && !this.isSelected())
        {
            if (this.getToggleGroup() == null)
            {
                this.getSelectedProperty().setValue(true);
                return true;
            }
            else
            {
                if (this.getToggleGroup().getSelectedButton() == this)
                    return false;
                this.getToggleGroup().setSelectedButton(this);
                return true;
            }
        }
        return this.isSelected();
    }
}