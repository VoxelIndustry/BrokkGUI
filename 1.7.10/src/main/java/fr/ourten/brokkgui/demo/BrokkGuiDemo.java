package fr.ourten.brokkgui.demo;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.ourten.brokkgui.wrapper.BrokkGuiManager;
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

    @EventHandler
    public void onPreInit(final FMLPreInitializationEvent event)
    {
        GameRegistry.registerItem(new Item()
        {
            @Override
            public boolean onItemUse(final ItemStack stack, final EntityPlayer player, final World w, final int x,
                    final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ)
            {
                if (w.isRemote)
                    Minecraft.getMinecraft().displayGuiScreen(BrokkGuiManager.getBrokkGuiScreen(new GuiDemo()));
                return true;
            }
        }.setCreativeTab(CreativeTabs.tabMisc), "ItemBrokkGuiTest");
    }
}