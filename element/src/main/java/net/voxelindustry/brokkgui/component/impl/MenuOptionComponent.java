package net.voxelindustry.brokkgui.component.impl;

import net.voxelindustry.brokkgui.component.GuiComponent;

import java.util.function.Supplier;

/**
 * Lightweight component intended to be added on elements lacking a TextComponent that needs to be added to a MenuSelectComponent.
 * When adding the component to your element you have to specify the Supplier returning the String value of the option.
 * <pre>{@code
 *  var element = new ElementWithoutTextComponent();
 *
 *  element.provide(new MenuOptionComponent(element::getExampleText));
 * }</pre>
 */
public class MenuOptionComponent extends GuiComponent
{
    private final Supplier<String> optionsValueGetter;

    public MenuOptionComponent(Supplier<String> optionsValueGetter)
    {
        this.optionsValueGetter = optionsValueGetter;
    }

    public Supplier<String> optionsValueGetter()
    {
        return optionsValueGetter;
    }
}
