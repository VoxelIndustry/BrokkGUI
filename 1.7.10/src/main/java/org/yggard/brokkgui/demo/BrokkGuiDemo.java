package org.yggard.brokkgui.demo;

import org.yggard.brokkgui.wrapper.BrokkGuiManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mod(modid = BrokkGuiDemo.MODID, version = BrokkGuiDemo.VERSION, name = BrokkGuiDemo.MODNAME)
public class BrokkGuiDemo
{
    public static final String MODID   = "brokkguidemo";
    public static final String MODNAME = "BrokkGui Demo";
    public static final String VERSION = "1.0";

    @Instance(BrokkGuiDemo.MODID)
    public static BrokkGuiDemo INSTANCE;

    @EventHandler
    public void onPreInit(final FMLPreInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new DemoGuiHandler());
        GameRegistry.registerItem(new Item()
        {
            @Override
            public boolean onItemUse(final ItemStack stack, final EntityPlayer player, final World w, final int x,
                    final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ)
            {
                if (w.isRemote && !player.isSneaking())
                    Minecraft.getMinecraft().displayGuiScreen(BrokkGuiManager.getBrokkGuiScreen(new GuiDemo()));
                else if (!w.isRemote && player.isSneaking())
                    player.openGui(BrokkGuiDemo.INSTANCE, 0, w, x, y, z);
                return true;
            }
        }.setCreativeTab(CreativeTabs.tabMisc), "ItemBrokkGuiTest");
    }
}