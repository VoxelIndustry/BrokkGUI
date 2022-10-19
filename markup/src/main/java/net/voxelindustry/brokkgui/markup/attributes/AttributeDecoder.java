package net.voxelindustry.brokkgui.markup.attributes;

import net.voxelindustry.brokkgui.component.GuiElement;

@FunctionalInterface
public interface AttributeDecoder
{
    void decode(String attributeValue, GuiElement element);
}
