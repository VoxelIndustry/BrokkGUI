package net.voxelindustry.brokkgui.element.menu;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.ButtonComponent;
import net.voxelindustry.brokkgui.component.impl.MenuDisplayListComponent;
import net.voxelindustry.brokkgui.component.impl.MenuOptionComponent;
import net.voxelindustry.brokkgui.component.impl.MenuSelectComponent;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.style.StyledElement;
import net.voxelindustry.brokkgui.text.GuiOverflow;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.brokkgui.text.TextLayoutComponent;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

public class GuiSelect extends GuiElement implements StyledElement
{
    private TextComponent            textComponent;
    private TextLayoutComponent      textLayoutComponent;
    private MenuSelectComponent      menuSelectComponent;
    private MenuDisplayListComponent menuDisplayListComponent;
    private ButtonComponent          buttonComponent;

    @Nullable
    private final GuiElement selectedValueElement;

    public GuiSelect()
    {
        this(null);
    }

    public GuiSelect(GuiElement selectedValueElement)
    {
        this.selectedValueElement = selectedValueElement;

        if (selectedValueElement == null)
        {
            textComponent = provide(TextComponent.class);
            textLayoutComponent = provide(TextLayoutComponent.class);

            textLayoutComponent.textOverflow(GuiOverflow.HIDDEN);
            textComponent.textAlignment(RectAlignment.LEFT_CENTER);

            textComponent.textProperty().bindProperty(menuSelectComponent.selectedValueObservable()
                    .combine(menuSelectComponent.promptTextProperty(),
                            (selectedValue, promptText) -> StringUtils.isEmpty(selectedValue) ? promptText : selectedValue));
        }
        else
        {
            transform().addChild(selectedValueElement.transform());
            RelativeBindingHelper.bindToPos(selectedValueElement.transform(), transform());

            var menuOptionComponent = selectedValueElement.get(MenuOptionComponent.class);
            menuSelectComponent.selectedValueObservable()
                    .addChangeListener(obs -> menuOptionComponent.optionsValueSetter().accept(menuSelectComponent.selectedValue()));
        }
    }

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        buttonComponent = provide(ButtonComponent.class);

        menuSelectComponent = provide(MenuSelectComponent.class);
        menuDisplayListComponent = provide(MenuDisplayListComponent.class);

        menuDisplayListComponent.displayList().verticalLayoutComponent().setChildrenElements(menuSelectComponent().optionsElementProperty());
    }

    @Override
    public String type()
    {
        return "select";
    }

    @Nullable
    public GuiElement selectedValueElement()
    {
        return selectedValueElement;
    }

    ////////////////
    // COMPONENTS //
    ////////////////

    public TextComponent textComponent()
    {
        return textComponent;
    }

    public TextLayoutComponent textLayoutComponent()
    {
        return textLayoutComponent;
    }

    public MenuSelectComponent menuSelectComponent()
    {
        return menuSelectComponent;
    }

    public MenuDisplayListComponent menuDisplayListComponent()
    {
        return menuDisplayListComponent;
    }

    public ButtonComponent buttonComponent()
    {
        return buttonComponent;
    }

    ////////////////
    // DELEGATES  //
    ////////////////

    public String selectedValue()
    {
        return menuSelectComponent.selectedValue();
    }

    public void selectedValue(String selectedValue)
    {
        menuSelectComponent.selectedValue(selectedValue);
    }

    public GuiElement addOption(String option)
    {
        return menuSelectComponent.addOption(option);
    }

    public GuiElement addOption(GuiElement option)
    {
        return menuSelectComponent.addOption(option);
    }

    public GuiElement addSeparator()
    {
        return menuSelectComponent.addSeparator();
    }

    public GuiElement addSeparator(int index)
    {
        return menuSelectComponent.addSeparator(index);
    }

    public GuiElement addOption(String option, int index)
    {
        return menuSelectComponent.addOption(option, index);
    }

    public GuiElement addOption(GuiElement option, int index)
    {
        return menuSelectComponent.addOption(option, index);
    }

    public void removeOption(String option)
    {
        menuSelectComponent.removeOption(option);
    }

    public void removeOption(int index)
    {
        menuSelectComponent.removeOption(index);
    }

    public void removeOption(GuiElement option)
    {
        menuSelectComponent.removeOption(option);
    }

    public String promptText()
    {
        return menuSelectComponent.promptText();
    }

    public void promptText(String promptText)
    {
        menuSelectComponent.promptText(promptText);
    }

    public boolean allowEmptySelection()
    {
        return menuSelectComponent.allowEmptySelection();
    }

    public void allowEmptySelection(boolean allowEmptySelection)
    {
        menuSelectComponent.allowEmptySelection(allowEmptySelection);
    }
}
