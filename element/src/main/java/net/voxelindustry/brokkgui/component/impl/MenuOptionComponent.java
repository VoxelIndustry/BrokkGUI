package net.voxelindustry.brokkgui.component.impl;

import net.voxelindustry.brokkgui.component.GuiComponent;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Lightweight component intended to be added on elements lacking a TextComponent that needs to be added to a MenuSelectComponent.
 * When adding the component to your element you have to specify the Supplier returning the String value of the option.
 * <pre>{@code
 *  var element = new ElementWithoutTextComponent();
 *
 *  element.provide(new MenuOptionComponent("TEXT"));
 *  element.provide(new MenuOptionComponent(element::getExampleText));
 *  element.provide(new MenuOptionComponent(element::getExampleText, element::setExampleText));
 * }</pre>
 */
public class MenuOptionComponent extends GuiComponent
{
    private final Supplier<String> optionsValueGetter;
    @Nullable
    private final Consumer<String> optionsValueSetter;

    public MenuOptionComponent(String staticValue)
    {
        this(() -> staticValue, null);
    }

    public MenuOptionComponent(Supplier<String> optionsValueGetter)
    {
        this(optionsValueGetter, null);
    }

    public MenuOptionComponent(String staticValue, Consumer<String> optionsValueSetter)
    {
        this(() -> staticValue, optionsValueSetter);
    }

    public MenuOptionComponent(Supplier<String> optionsValueGetter, Consumer<String> optionsValueSetter)
    {
        this.optionsValueGetter = optionsValueGetter;
        this.optionsValueSetter = optionsValueSetter;
    }

    public Supplier<String> optionsValueGetter()
    {
        return optionsValueGetter;
    }

    @Nullable
    public Consumer<String> optionsValueSetter()
    {
        return optionsValueSetter;
    }
}
