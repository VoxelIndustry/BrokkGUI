package org.yggard.brokkgui.control;

import java.util.Arrays;

import org.yggard.brokkgui.behavior.IGuiTogglable;

import com.google.common.collect.ImmutableList;

import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;

public class GuiToggleGroup
{
    private final BaseProperty<IGuiTogglable>     selectedButtonProperty;
    private final BaseListProperty<IGuiTogglable> buttonListProperty;
    private boolean                               allowNothing;

    public GuiToggleGroup()
    {
        this.selectedButtonProperty = new BaseProperty<>(null, "selectedButtonProperty");
        this.buttonListProperty = new BaseListProperty<>(null, "buttonListProperty");
    }

    public void setSelectedButton(final IGuiTogglable button)
    {
        if (this.buttonListProperty.contains(button))
        {
            this.selectedButtonProperty.setValue(button);
            this.buttonListProperty.getValue()
                    .forEach(button2 -> button2.getSelectedProperty().setValue(button2 == button));
        }
        else if (this.allowNothing() && button == null)
        {
            this.selectedButtonProperty.setValue(null);
            this.buttonListProperty.getValue().forEach(button2 -> button2.getSelectedProperty().setValue(false));
        }
    }

    public IGuiTogglable getSelectedButton()
    {
        return this.getSelectedButtonProperty().getValue();
    }

    public ImmutableList<IGuiTogglable> getButtonList()
    {
        return this.getButtonListProperty().getValue();
    }

    public void addButton(final GuiToggleButtonBase button)
    {
        if (!this.getButtonListProperty().contains(button))
        {
            this.getButtonListProperty().add(button);
            button.setToggleGroup(this);
        }
    }

    public void addButtons(final GuiToggleButtonBase... buttons)
    {
        Arrays.asList(buttons).forEach(this::addButton);
    }

    public boolean allowNothing()
    {
        return this.allowNothing;
    }

    public void setAllowNothing(final boolean allowNothing)
    {
        this.allowNothing = allowNothing;
    }

    public BaseProperty<IGuiTogglable> getSelectedButtonProperty()
    {
        return this.selectedButtonProperty;
    }

    public BaseListProperty<IGuiTogglable> getButtonListProperty()
    {
        return this.buttonListProperty;
    }
}