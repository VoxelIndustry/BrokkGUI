package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.property.specific.BooleanProperty;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectAlignment;
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

    private final Property<String> selectedValueProperty       = new Property<>("");
    private final Property<String> promptTextProperty          = new Property<>("");
    private final BooleanProperty  allowEmptySelectionProperty = new BooleanProperty();

    private GuiLabel emptySelectionOptionElement;

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

        allowEmptySelectionProperty().addChangeListener(obs ->
        {
            if (allowEmptySelection())
                addOption(emptySelectionOptionElement(), 0);
            else if (emptySelectionOptionElement != null)
                removeOption(emptySelectionOptionElement());
        });
    }

    private void onOptionSelected(ClickReleaseEvent event)
    {
        if (event.getSource() instanceof GuiElement source)
        {
            if (source == emptySelectionOptionElement)
            {
                selectedValueProperty.setValue("");
                return;
            }

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

    private GuiElement emptySelectionOptionElement()
    {
        if (emptySelectionOptionElement == null)
        {
            emptySelectionOptionElement = new GuiLabel();
            emptySelectionOptionElement.expandToText(false);
            emptySelectionOptionElement.transform().widthRatio(1);
            emptySelectionOptionElement.textAlignment(RectAlignment.LEFT_CENTER);
            emptySelectionOptionElement.transform().overflow(GuiOverflow.HIDDEN);

            emptySelectionOptionElement.textComponent().italic(true);

            emptySelectionOptionElement.textComponent().textProperty().bindProperty(promptTextProperty());
        }

        return emptySelectionOptionElement;
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

    public Property<String> promptTextProperty()
    {
        return promptTextProperty;
    }

    public BooleanProperty allowEmptySelectionProperty()
    {
        return allowEmptySelectionProperty;
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
        label.textAlignment(RectAlignment.LEFT_CENTER);
        label.transform().overflow(GuiOverflow.HIDDEN);
        label.transform().widthRatio(1);

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

        optionsElementProperty().add(index, label);

        return label;
    }

    public GuiElement addOption(GuiElement option, int index)
    {
        optionsElementProperty().add(index, option);

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

    public String promptText()
    {
        return promptTextProperty().getValue();
    }

    public void promptText(String promptText)
    {
        promptTextProperty().setValue(promptText);
    }

    public boolean allowEmptySelection()
    {
        return allowEmptySelectionProperty().get();
    }

    public void allowEmptySelection(boolean allowEmptySelection)
    {
        allowEmptySelectionProperty().set(allowEmptySelection);
    }
}
