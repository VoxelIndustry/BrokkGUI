package net.voxelindustry.brokkgui.wrapper.mixin;

import net.minecraft.client.Mouse;
import net.voxelindustry.brokkgui.wrapper.IAccessibleMouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mouse.class)
public class MouseMixin implements IAccessibleMouse
{
    @Shadow
    private int field_1780;

    @Shadow
    private double field_1786;

    @Override
    public int getMouseButton()
    {
        return this.field_1780;
    }

    @Override
    public double getDWheel()
    {
        return this.field_1786;
    }
}
