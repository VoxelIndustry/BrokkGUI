package org.yggard.brokkgui.demo;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.yggard.brokkgui.wrapper.impl.BrokkGuiManager;

@Mod(modid = BrokkGuiDemo.MODID, version = BrokkGuiDemo.VERSION, name = BrokkGuiDemo.MODNAME)
@Mod.EventBusSubscriber(modid = BrokkGuiDemo.MODID)
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
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new Item()
        {
            @Override
            public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player,
                                                            final EnumHand hand)
            {
                player.setActiveHand(hand);

                if (world.isRemote && !player.isSneaking())
                    Minecraft.getMinecraft().displayGuiScreen(BrokkGuiManager.getBrokkGuiScreen(new GuiDemo()));
                else if (!world.isRemote && player.isSneaking())
                    player.openGui(BrokkGuiDemo.INSTANCE, 0, world, (int) player.posX, (int) player.posY,
                            (int) player.posZ);
                return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
            }
        }.setCreativeTab(CreativeTabs.MISC).setRegistryName("itembrokkguitest"));
    }
}