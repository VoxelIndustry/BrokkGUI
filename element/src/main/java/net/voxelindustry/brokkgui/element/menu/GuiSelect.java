package net.voxelindustry.brokkgui.element.menu;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.ButtonComponent;
import net.voxelindustry.brokkgui.component.impl.MenuDisplayListComponent;
import net.voxelindustry.brokkgui.component.impl.MenuSelectComponent;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.style.StyledElement;
import net.voxelindustry.brokkgui.text.GuiOverflow;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.brokkgui.text.TextLayoutComponent;

public class GuiSelect extends GuiElement implements StyledElement
{
    private TextComponent            textComponent;
    private TextLayoutComponent      textLayoutComponent;
    private MenuSelectComponent      menuSelectComponent;
    private MenuDisplayListComponent menuDisplayListComponent;
    private ButtonComponent          buttonComponent;

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        textComponent = provide(TextComponent.class);
        textLayoutComponent = provide(TextLayoutComponent.class);
        buttonComponent = provide(ButtonComponent.class);

        menuSelectComponent = provide(MenuSelectComponent.class);
        menuDisplayListComponent = provide(MenuDisplayListComponent.class);

        textLayoutComponent.textOverflow(GuiOverflow.HIDDEN);
        textComponent.textAlignment(RectAlignment.LEFT_CENTER);

        textComponent.textProperty().bindProperty(menuSelectComponent.selectedValueProperty());

        menuDisplayListComponent.displayList().verticalLayoutComponent().setChildrenElements(menuSelectComponent().optionsElementProperty());
        menuDisplayListComponent.displayList().style().parseInlineCSS("border-color: pink; border-width: 1;");

        // Close display list when an option has been selected
        menuSelectComponent.selectedValueProperty().addChangeListener(obs -> menuDisplayListComponent.displayList().removeFocus());
    }

    @Override
    public String type()
    {
        return "select";
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

    public MenuDisplayListComponent menuDropdownListComponent()
    {
        return menuDisplayListComponent;
    }

    public ButtonComponent buttonComponent()
    {
        return buttonComponent;
    }
}
