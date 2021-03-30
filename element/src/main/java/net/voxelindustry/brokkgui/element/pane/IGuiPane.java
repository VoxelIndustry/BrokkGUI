package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.GuiElement;

public interface IGuiPane<T extends PaneChildPlacer>
{
    T addChild(GuiElement child);

    T setChildPosition(GuiElement child);
}
