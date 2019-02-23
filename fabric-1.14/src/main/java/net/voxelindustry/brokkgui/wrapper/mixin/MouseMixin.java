package net.voxelindustry.brokkgui.wrapper.mixin;

import net.minecraft.client.Mouse;
import net.voxelindustry.brokkgui.wrapper.IAccessibleMouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mouse.class)
public class MouseMixin implements IAccessibleMouse
{
    @Shadow
    private int activeButton;

    @Shadow
    private double eventDeltaWheel;

    @Override
    public int getMouseButton()
    {
        return this.activeButton;
    }

    @Override
    public double getDWheel()
    {
        return this.eventDeltaWheel;
    }
}
