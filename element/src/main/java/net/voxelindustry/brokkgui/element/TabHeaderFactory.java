package net.voxelindustry.brokkgui.element;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.element.pane.GuiTab;

@FunctionalInterface
public interface TabHeaderFactory
{
    GuiNode create(GuiTab tab, float maxWidth, float maxHeight);
}
