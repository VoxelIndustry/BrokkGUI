package fr.ourten.brokkgui.control;

import com.google.common.collect.ImmutableList;

import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;

public class GuiToggleGroup
{
    private final BaseProperty<GuiToggleButtonBase>     selectedButtonProperty;
    private final BaseListProperty<GuiToggleButtonBase> buttonListProperty;
    private boolean                                     allowNothing;

    public GuiToggleGroup()
    {
        this.selectedButtonProperty = new BaseProperty<>(null, "selectedButtonProperty");
        this.buttonListProperty = new BaseListProperty<>(null, "buttonListProperty");
    }

    public void setSelectedButton(final GuiToggleButtonBase button)
    {
        if (this.buttonListProperty.contains(button))
        {
            this.selectedButtonProperty.setValue(button);
            this.buttonListProperty.getValue()
                    .forEach(button2 -> button2.getSelectedProperty().setValue(button2 == button));
        }
    }

    public GuiToggleButtonBase getSelectedButton()
    {
        return this.selectedButtonProperty.getValue();
    }

    public ImmutableList<GuiToggleButtonBase> getButtonList()
    {
        return this.buttonListProperty.getValue();
    }

    public void addButton(final GuiToggleButtonBase button)
    {
        if (!this.buttonListProperty.contains(button))
            this.buttonListProperty.add(button);
    }

    public boolean allowNothing()
    {
        return this.allowNothing;
    }

    public void setAllowNothing(final boolean allowNothing)
    {
        this.allowNothing = allowNothing;
    }

    public BaseProperty<GuiToggleButtonBase> getSelectedButtonProperty()
    {
        return this.selectedButtonProperty;
    }

    public BaseListProperty<GuiToggleButtonBase> getButtonListProperty()
    {
        return this.buttonListProperty;
    }
}