package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.GuiElement;

@FunctionalInterface
public interface DynamicAttributeResolver
{
    boolean resolve(String attribute, String value, GuiElement element);
}
