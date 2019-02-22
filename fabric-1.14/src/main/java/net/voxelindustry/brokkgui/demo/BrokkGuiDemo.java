package net.voxelindustry.brokkgui.demo;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.wrapper.impl.BrokkGuiManager;

public class BrokkGuiDemo implements ModInitializer
{
    public static final String MODID   = "brokkguidemo";
    public static final String MODNAME = "BrokkGui Demo";
    public static final String VERSION = "1.0";

    @Override
    public void onInitialize()
    {
        BrokkGuiPlatform.getInstance().enableRenderDebug(true);

        Registry.register(Registry.ITEM, "brokkguidemo:itembrokkguitest",
                new Item(new Item.Settings().stackSize(1).itemGroup(ItemGroup.MISC))
                {
                    @Override
                    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
                    {
                        player.setCurrentHand(hand);

                        if (world.isClient() && !player.isSneaking())
                            MinecraftClient.getInstance().openScreen(BrokkGuiManager.getBrokkGuiScreen(new GuiDemo()));
                        // TODO : Add container opening

                        return new TypedActionResult<>(ActionResult.PASS, player.getStackInHand(hand));
                    }
                });
    }
}
