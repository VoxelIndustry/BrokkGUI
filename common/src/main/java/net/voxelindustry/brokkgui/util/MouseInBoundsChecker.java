package net.voxelindustry.brokkgui.util;

import net.voxelindustry.brokkgui.component.GuiElement;

@FunctionalInterface
public interface MouseInBoundsChecker
{
    MouseInBoundsChecker DEFAULT = ((element, mouseX, mouseY) -> mouseX >
            element.transform().leftPos() &&
            mouseX < element.transform().rightPos() &&
            mouseY > element.transform().topPos() &&
            mouseY < element.transform().bottomPos());

    boolean test(GuiElement element, float mouseX, float mouseY);
}
