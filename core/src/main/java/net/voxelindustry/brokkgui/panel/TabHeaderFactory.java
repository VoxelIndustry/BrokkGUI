package net.voxelindustry.brokkgui.panel;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.component.GuiTab;

@FunctionalInterface
public interface TabHeaderFactory
{
    GuiNode create(GuiTab tab, float maxWidth, float maxHeight);
}
