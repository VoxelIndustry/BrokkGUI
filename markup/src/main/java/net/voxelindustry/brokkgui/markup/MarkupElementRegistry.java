package net.voxelindustry.brokkgui.markup;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.markup.definitions.MarkupElementDefinition;
import net.voxelindustry.brokkgui.markup.definitions.MarkupMetaElementDefinition;

import java.util.HashMap;
import java.util.Map;

public class MarkupElementRegistry
{
    private static final Map<String, MarkupElementDefinition<?>>     elementDefinitions     = new HashMap<>();
    private static final Map<String, MarkupMetaElementDefinition<?>> metaElementDefinitions = new HashMap<>();

    public static <T extends GuiElement> void registerElementDefinition(String name, MarkupElementDefinition<T> definition)
    {
        elementDefinitions.put(name, definition);
    }

    public static <T> void registerMetaElementDefinition(String name, MarkupMetaElementDefinition<T> definition)
    {
        metaElementDefinitions.put(name, definition);
    }

    @SuppressWarnings("unchecked")
    public static <T extends GuiElement> MarkupElementDefinition<T> getElementDefinition(String name)
    {
        return (MarkupElementDefinition<T>) elementDefinitions.get(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> MarkupMetaElementDefinition<T> getMetaElementDefinition(String name)
    {
        return (MarkupMetaElementDefinition<T>) metaElementDefinitions.get(name);
    }
}
