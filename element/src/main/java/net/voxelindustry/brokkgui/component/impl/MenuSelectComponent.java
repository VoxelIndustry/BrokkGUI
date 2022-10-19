package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.property.specific.BooleanProperty;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.event.ClickReleaseEvent;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.style.PseudoClassConstants;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.text.GuiOverflow;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.IEventEmitter;

import java.util.Objects;

public class MenuSelectComponent extends GuiComponent
{
    private final ListProperty<GuiElement> optionsElementProperty = new ListProperty<>();

    private final Property<String> selectedValueProperty       = new Property<>("");
    private final Property<String> promptTextProperty          = new Property<>("");
    private final BooleanProperty  allowEmptySelectionProperty = new BooleanProperty();
    private final BooleanProperty  selectActiveOptionProperty  = new BooleanProperty();

    private final EventHandler<ClickReleaseEvent> onOptionSelected = this::onOptionSelected;

    private GuiLabel emptySelectionOptionElement;

    private GuiElement selectedElement;
    private GuiElement activeElement;

    private MenuDisplayListComponent menuDisplayListComponent;

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
        selectOption(event.getSource());

        menuDisplayListComponent().displayList().removeFocus();
    }

    private void selectOption(IEventEmitter option)
    {
        if (!(option instanceof GuiElement source))
            return;

        if (source == emptySelectionOptionElement)
        {
            selectedValueProperty.setValue("");
            selectedElement = emptySelectionOptionElement;

            activateOption(selectedElement);
            return;
        }

        var value = getLabelFromElement(source);

        if (value != null)
        {
            selectedValueProperty.setValue(value);
            selectedElement = source;

            activateOption(selectedElement);
        }
    }

    private void activateOption(GuiElement element)
    {
        if (activeElement != null)
            activeElement.get(StyleComponent.class).removePseudoClass(PseudoClassConstants.ACTIVE);

        element.get(StyleComponent.class).addPseudoClass(PseudoClassConstants.ACTIVE);
        activeElement = element;
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

    private MenuDisplayListComponent menuDisplayListComponent()
    {
        if (menuDisplayListComponent == null)
            menuDisplayListComponent = element().get(MenuDisplayListComponent.class);
        return menuDisplayListComponent;
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    public ListProperty<GuiElement> optionsElementProperty()
    {
        return optionsElementProperty;
    }

    public ObservableValue<String> selectedValueObservable()
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

    public BooleanProperty selectActiveOptionProperty()
    {
        return selectActiveOptionProperty;
    }

    ////////////
    // VALUES //
    ////////////

    public String selectedValue()
    {
        return selectedValueObservable().getValue();
    }

    public void selectedValue(String selectedValue)
    {
        for (var element : optionsElementProperty())
        {
            var value = getLabelFromElement(element);

            if (Objects.equals(value, selectedValue))
                selectOption(element);
        }
    }

    public int ordinalOfSelected()
    {
        return optionsElementProperty().indexOf(selectedElement);
    }

    public void selectWithOrdinal(int ordinal)
    {
        selectOption(optionsElementProperty().get(ordinal));
    }

    public int ordinalOfActive()
    {
        return optionsElementProperty().indexOf(activeElement);
    }

    public void activateWithOrdinal(int ordinal)
    {
        if (activeElement != null)
            activeElement.get(StyleComponent.class).removePseudoClass(PseudoClassConstants.ACTIVE);

        var element = optionsElementProperty().get(ordinal);
        activateOption(element);

        if (selectActiveOption())
            selectOption(activeElement);
    }

    public GuiElement addOption(String option)
    {
        var label = new GuiLabel(option);
        label.expandToText(false);
        label.style().addStyleClass("menu-select-element");
        label.textAlignment(RectAlignment.LEFT_CENTER);
        label.transform().overflow(GuiOverflow.HIDDEN);
        label.transform().widthRatio(1);

        optionsElementProperty().add(label);

        return label;
    }

    public GuiElement addOption(GuiElement option)
    {
        optionsElementProperty().add(option);
        option.ifHas(StyleComponent.class, style -> style.addStyleClass("menu-select-element"));

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
        label.style().addStyleClass("menu-select-element");

        optionsElementProperty().add(index, label);

        return label;
    }

    public GuiElement addOption(GuiElement option, int index)
    {
        optionsElementProperty().add(index, option);
        option.ifHas(StyleComponent.class, style -> style.addStyleClass("menu-select-element"));

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

    public GuiElement getOption(int ordinal)
    {
        return optionsElementProperty().get(ordinal);
    }

    public int getOptionsCount()
    {
        return optionsElementProperty().size();
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

    public boolean selectActiveOption()
    {
        return selectActiveOptionProperty().get();
    }

    public void selectActiveOption(boolean selectActiveOption)
    {
        selectActiveOptionProperty().set(selectActiveOption);
    }
}
