package net.voxelindustry.brokkgui.component;

import java.util.Collection;
import java.util.function.Consumer;

public interface ComponentHolder
{
    <T extends GuiComponent> T remove(Class<T> componentClass);

    <T extends GuiComponent> T get(Class<T> componentClass);

    <T extends GuiComponent> Collection<T> getAll(Class<T> componentClass);

    <T extends GuiComponent> boolean has(Class<T> componentClass);

    boolean has(GuiComponent component);

    <T extends GuiComponent> boolean ifHas(Class<T> componentClass, Consumer<T> action);
}
