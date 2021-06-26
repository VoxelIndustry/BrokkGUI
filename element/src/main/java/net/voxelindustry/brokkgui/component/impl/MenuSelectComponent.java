package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.event.ClickReleaseEvent;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.text.GuiOverflow;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.hermod.EventHandler;

import java.util.Objects;

public class MenuSelectComponent extends GuiComponent
{
    private final ListProperty<GuiElement> optionsElementProperty = new ListProperty<>();

    private final Property<String> selectedValueProperty = new Property<>("");

    private final EventHandler<ClickReleaseEvent> onOptionSelected = this::onOptionSelected;

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        optionsElementProperty.addChangeListener((ListValueChangeListener<? super GuiElement>) (observable, oldValue, newValue) ->
        {
            if (newValue != null)
                newValue.getEventDispatcher().addHandler(ClickReleaseEvent.TYPE, onOptionSelected);

            if (oldValue != null)
                oldValue.getEventDispatcher().removeHandler(ClickReleaseEvent.TYPE, onOptionSelected);
        });
    }

    private void onOptionSelected(ClickReleaseEvent event)
    {
        if (event.getSource() instanceof GuiElement source)
        {
            var value = getLabelFromElement(source);

            if (value != null)
                selectedValueProperty.setValue(value);
        }
    }

    private String getLabelFromElement(GuiElement element)
    {
        String value = null;
        if (element.has(MenuOptionComponent.class))
            value = element.get(MenuOptionComponent.class).optionsValueGetter().get();
        else if (element.has(TextComponent.class))
            value = element.get(TextComponent.class).text();
        return value;
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    public ListProperty<GuiElement> optionsElementProperty()
    {
        return optionsElementProperty;
    }

    public Property<String> selectedValueProperty()
    {
        return selectedValueProperty;
    }

    ////////////
    // VALUES //
    ////////////

    public String selectedValue()
    {
        return selectedValueProperty().getValue();
    }

    public void selectedValue(String selectedValue)
    {
        selectedValueProperty().setValue(selectedValue);
    }

    public GuiElement addOption(String option)
    {
        var label = new GuiLabel(option);
        label.expandToText(false);
        label.transform().overflow(GuiOverflow.HIDDEN);

        optionsElementProperty().add(label);

        return label;
    }

    public GuiElement addOption(GuiElement option)
    {
        optionsElementProperty().add(option);

        return option;
    }

    public GuiElement addSeparator()
    {
        var separator = new Rectangle();
        separator.get(StyleComponent.class).addStyleClass("menu-separator");
        separator.transform().widthRatio(1);

        optionsElementProperty().add(separator);

        return separator;
    }

    public GuiElement addSeparator(int index)
    {
        var separator = new Rectangle();
        separator.get(StyleComponent.class).addStyleClass("menu-separator");
        separator.transform().widthRatio(1);

        optionsElementProperty().set(index, separator);

        return separator;
    }

    public GuiElement addOption(String option, int index)
    {
        var label = new GuiLabel(option);
        label.expandToText(false);
        label.transform().overflow(GuiOverflow.HIDDEN);

        optionsElementProperty().set(index, label);

        return label;
    }

    public GuiElement addOption(GuiElement option, int index)
    {
        optionsElementProperty().set(index, option);

        return option;
    }

    public void removeOption(String option)
    {
        optionsElementProperty().removeIf(element -> Objects.equals(option, getLabelFromElement(element)));
    }

    public void removeOption(int index)
    {
        optionsElementProperty().remove(index);
    }

    public void removeOption(GuiElement option)
    {
        optionsElementProperty().remove(option);
    }
}
