package net.voxelindustry.brokkgui.wrapper.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemColorMap;
import net.voxelindustry.brokkgui.wrapper.IAccessibleItemColorMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements IAccessibleItemColorMap
{
    @Shadow
    private ItemColorMap itemColorMap;

    @Override
    public ItemColorMap getItemColorMap()
    {
        return this.itemColorMap;
    }
}
