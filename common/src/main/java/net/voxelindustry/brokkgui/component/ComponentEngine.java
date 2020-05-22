package net.voxelindustry.brokkgui.component;

import com.google.common.base.Predicates;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ComponentEngine
{
    private static ComponentEngine INSTANCE;

    public static ComponentEngine instance()
    {
        if (INSTANCE == null)
            INSTANCE = new ComponentEngine();
        return INSTANCE;
    }

    private final Map<Class<? extends GuiComponent>, Predicate<GuiElement>> componentInjector = new IdentityHashMap<>();

    private final Map<Class<? extends GuiComponent>, Class<? extends GuiComponent>> componentOverrides = new IdentityHashMap<>();

    private ComponentEngine()
    {

    }

    public void addComponentInject(Class<? extends GuiComponent> classToInject)
    {
        addComponentInject(classToInject, Predicates.alwaysTrue());
    }

    public void addComponentInject(Class<? extends GuiComponent> classToInject, Predicate<GuiElement> filter)
    {
        componentInjector.put(classToInject, filter);
    }

    public void addComponentOverride(Class<? extends GuiComponent> classToOverride, Class<? extends GuiComponent> classToInject)
    {
        componentOverrides.put(classToOverride, classToInject);
    }

    public void inject(GuiElement element)
    {
        componentInjector.forEach((componentClass, filter) ->
        {
            if (filter.test(element))
                element.add(componentClass);
        });
    }

    public GuiComponent override(GuiElement element, GuiComponent component)
    {
        GuiComponent instance = component;

        if (componentOverrides.containsKey(component.getClass()))
        {
            try
            {
                instance = componentOverrides.get(component.getClass()).newInstance();
            } catch (InstantiationException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        return instance;
    }

    public Class<? extends GuiComponent> override(GuiElement element, Class<? extends GuiComponent> componentClass)
    {
        return componentOverrides.getOrDefault(componentClass, componentClass);
    }
}
