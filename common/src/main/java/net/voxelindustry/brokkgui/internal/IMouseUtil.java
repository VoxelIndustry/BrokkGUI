package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.window.IGuiWindow;

/**
 * @author Ourten 9 oct. 2016
 */
public interface IMouseUtil
{
    float getMouseX(IGuiWindow window);

    float getMouseY(IGuiWindow window);

    float getMouseX(IBrokkGuiImpl window);

    float getMouseY(IBrokkGuiImpl window);
}