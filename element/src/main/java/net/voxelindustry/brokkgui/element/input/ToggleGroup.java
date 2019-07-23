package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.impl.Toggleable;

import java.util.Arrays;
import java.util.List;

public class ToggleGroup
{
    private final BaseProperty<Toggleable>     selectedButtonProperty;
    private final BaseListProperty<Toggleable> buttonListProperty;
    private       boolean                      allowNothing;

    public ToggleGroup()
    {
        this.selectedButtonProperty = new BaseProperty<>(null, "selectedButtonProperty");
        this.buttonListProperty = new BaseListProperty<>(null, "buttonListProperty");
    }

    public void selectedButton(Toggleable button)
    {
        if (this.buttonListProperty.contains(button))
        {
            this.selectedButtonProperty.setValue(button);
            this.buttonListProperty.getValue()
                    .forEach(button2 -> button2.selectedProperty().setValue(button2 == button));
        }
        else if (this.allowNothing() && button == null)
        {
            this.selectedButtonProperty.setValue(null);
            this.buttonListProperty.getValue().forEach(button2 -> button2.selectedProperty().setValue(false));
        }
    }

    public Toggleable selectedButton()
    {
        return this.selectedButtonProperty().getValue();
    }

    /**
     * @return an immutable list
     */
    public List<Toggleable> buttonList()
    {
        return this.buttonListProperty().getValue();
    }

    public void addButton(final Toggleable button)
    {
        if (!this.buttonListProperty().contains(button))
        {
            this.buttonListProperty().add(button);
            button.toggleGroup(this);
        }
    }

    public void addButtons(final Toggleable... buttons)
    {
        Arrays.asList(buttons).forEach(this::addButton);
    }

    public boolean allowNothing()
    {
        return this.allowNothing;
    }

    public void allowNothing(final boolean allowNothing)
    {
        this.allowNothing = allowNothing;
    }

    public BaseProperty<Toggleable> selectedButtonProperty()
    {
        return this.selectedButtonProperty;
    }

    public BaseListProperty<Toggleable> buttonListProperty()
    {
        return this.buttonListProperty;
    }
}
