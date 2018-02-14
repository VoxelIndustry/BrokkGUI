package org.yggard.brokkgui.demo;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.yggard.brokkgui.wrapper.impl.BrokkGuiManager;

@Mod(modid = BrokkGuiDemo.MODID, version = BrokkGuiDemo.VERSION, name = BrokkGuiDemo.MODNAME)
public class BrokkGuiDemo
{
    public static final String MODID   = "brokkguidemo";
    public static final String MODNAME = "BrokkGui Demo";
    public static final String VERSION = "1.0";

    @Mod.Instance(BrokkGuiDemo.MODID)
    public static BrokkGuiDemo INSTANCE;

    @Mod.EventHandler
    public void onPreInit(final FMLPreInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new DemoGuiHandler());
        MinecraftForge.EVENT_BUS.register(this);

        GameRegistry.registerItem(new Item()
        {
            @Override
            public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
            {
                if (world.isRemote && !player.isSneaking())
                    Minecraft.getMinecraft().displayGuiScreen(BrokkGuiManager.getBrokkGuiScreen(new GuiDemo()));
                else if (!world.isRemote && player.isSneaking())
                    player.openGui(BrokkGuiDemo.INSTANCE, 0, world, (int) player.posX, (int) player.posY,
                            (int) player.posZ);
                return stack;
            }

        }.setCreativeTab(CreativeTabs.tabMisc).setUnlocalizedName("itembrokkguitest"), "itembrokkguitest");
    }
}