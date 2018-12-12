package net.voxelindustry.brokkgui.wrapper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Mouse;
import net.voxelindustry.brokkgui.internal.IMouseUtil;
import net.voxelindustry.brokkgui.wrapper.mixin.IAccessibleMouse;

/**
 * @author Ourten 9 oct. 2016
 */
public class MouseUtil implements IMouseUtil
{
    private Mouse mouse;

    public MouseUtil()
    {
        this.mouse = MinecraftClient.getInstance().mouse;
    }

    @Override
    public int getEventDWheel()
    {
        return (int) ((IAccessibleMouse) mouse).getDWheel();
    }

    @Override
    public int getEventButton()
    {
        return ((IAccessibleMouse) mouse).getMouseButton();
    }

    public int getMouseX()
    {
        if (MinecraftClient.getInstance().currentGui == null)
            // Mouse.getX
            return (int) this.mouse.method_1603();
        return (int) (mouse.method_1603() * MinecraftClient.getInstance().window.getScaledWidth() /
                MinecraftClient.getInstance().window.getWindowWidth());
    }

    public int getMouseY()
    {
        if (MinecraftClient.getInstance().currentGui == null)
            // Mouse.getY
            return (int) this.mouse.method_1604();

        // Mouse.getEventY
        return (int) (MinecraftClient.getInstance().window.getScaledHeight() - mouse.method_1604() *
                MinecraftClient.getInstance().window.getScaledHeight() / MinecraftClient.getInstance().window.getWindowHeight() - 1);
    }
}
