package org.yggard.brokkgui.panel;

import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.component.GuiTab;

@FunctionalInterface
public interface TabHeaderFactory
{
    GuiNode create(GuiTab tab, float maxWidth, float maxHeight);
}
