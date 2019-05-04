package net.voxelindustry.brokkgui.util;

import net.voxelindustry.brokkgui.exp.component.GuiElement;

@FunctionalInterface
public interface MouseInBoundsChecker
{
    MouseInBoundsChecker DEFAULT = ((element, mouseX, mouseY) -> mouseX >
            element.transform().getLeftPos() &&
            mouseX < element.transform().getRightPos() &&
            mouseY > element.transform().getTopPos() &&
            mouseY < element.transform().getBottomPos());

    boolean test(GuiElement element, int mouseX, int mouseY);
}
