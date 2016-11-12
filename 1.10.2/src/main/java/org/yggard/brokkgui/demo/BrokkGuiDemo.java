package org.yggard.brokkgui.demo;

import org.yggard.brokkgui.wrapper.BrokkGuiManager;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
            public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
                    EnumHand hand)
            {
                playerIn.setActiveHand(hand);

                if (worldIn.isRemote && !playerIn.isSneaking())
                    Minecraft.getMinecraft().displayGuiScreen(BrokkGuiManager.getBrokkGuiScreen(new GuiDemo()));
                else if (!worldIn.isRemote && playerIn.isSneaking())
                    playerIn.openGui(BrokkGuiDemo.INSTANCE, 0, worldIn, (int) playerIn.posX, (int) playerIn.posY,
                            (int) playerIn.posZ);
                return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
            }
        }.setCreativeTab(CreativeTabs.MISC), "ItemBrokkGuiTest");
    }
}