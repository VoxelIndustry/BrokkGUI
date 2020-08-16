package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.element.IGuiToggleable;

import java.util.Arrays;
import java.util.List;

public class GuiToggleGroup
{
    private final Property<IGuiToggleable>     selectedButtonProperty;
    private final ListProperty<IGuiToggleable> buttonListProperty;
    private       boolean                      allowNothing;

    public GuiToggleGroup()
    {
        selectedButtonProperty = new Property<>(null);
        buttonListProperty = new ListProperty<>(null);
    }

    public void setSelectedButton(IGuiToggleable button)
    {
        if (buttonListProperty.contains(button))
        {
            selectedButtonProperty.setValue(button);
            buttonListProperty.getValue()
                    .forEach(button2 -> button2.getSelectedProperty().setValue(button2 == button));
        }
        else if (allowNothing() && button == null)
        {
            selectedButtonProperty.setValue(null);
            buttonListProperty.getValue().forEach(button2 -> button2.getSelectedProperty().setValue(false));
        }
    }

    public IGuiToggleable getSelectedButton()
    {
        return getSelectedButtonProperty().getValue();
    }

    /**
     * @return an immutable list
     */
    public List<IGuiToggleable> getButtonList()
    {
        return getButtonListProperty().getValue();
    }

    public void addButton(IGuiToggleable button)
    {
        if (!getButtonListProperty().contains(button))
        {
            getButtonListProperty().add(button);
            button.setToggleGroup(this);
        }
    }

    public void addButtons(IGuiToggleable... buttons)
    {
        Arrays.asList(buttons).forEach(this::addButton);
    }

    public boolean allowNothing()
    {
        return allowNothing;
    }

    public void setAllowNothing(boolean allowNothing)
    {
        this.allowNothing = allowNothing;
    }

    public Property<IGuiToggleable> getSelectedButtonProperty()
    {
        return selectedButtonProperty;
    }

    public ListProperty<IGuiToggleable> getButtonListProperty()
    {
        return buttonListProperty;
    }
}