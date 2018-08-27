package org.yggard.brokkgui.wrapper;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.yggard.brokkgui.internal.IMouseUtil;

/**
 * @author Ourten 9 oct. 2016
 */
public class MouseUtil implements IMouseUtil
{
    @Override
    public int getEventDWheel()
    {
        return Mouse.getEventDWheel();
    }

    @Override
    public int getEventButton()
    {
        return Mouse.getEventButton();
    }

    public int getMouseX()
    {
        if (Minecraft.getMinecraft().currentScreen == null)
            return Mouse.getX();
        return Mouse.getX() * Minecraft.getMinecraft().currentScreen.width / Minecraft.getMinecraft().displayWidth;
    }

    public int getMouseY()
    {
        if (Minecraft.getMinecraft().currentScreen == null)
            return Mouse.getY();
        return Minecraft.getMinecraft().currentScreen.height - Mouse.getEventY() *
                Minecraft.getMinecraft().currentScreen.height / Minecraft.getMinecraft().displayHeight - 1;
    }
}