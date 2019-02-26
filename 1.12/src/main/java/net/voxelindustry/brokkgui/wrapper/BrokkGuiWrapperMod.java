package net.voxelindustry.brokkgui.wrapper;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;

/**
 * @author Ourten 5 oct. 2016
 */
@Mod(modid = BrokkGuiWrapperMod.MODID, version = BrokkGuiWrapperMod.VERSION, name = BrokkGuiWrapperMod.MODNAME)
public class BrokkGuiWrapperMod
{
    public static final String MODID   = "brokkguiwrapper";
    public static final String MODNAME = "BrokkGui Wrapper";
    public static final String VERSION = "0.1.0";

    @EventHandler
    public void onPreInit(final FMLPreInitializationEvent event)
    {
        BrokkGuiPlatform.getInstance().setPlatformName("MC1.12.2");
        BrokkGuiPlatform.getInstance().setKeyboardUtil(new KeyboardUtil());
        BrokkGuiPlatform.getInstance().setMouseUtil(new MouseUtil());

        if (event.getSide().isClient())
        {
            BrokkGuiPlatform.getInstance().setGuiHelper(new GuiHelper());

            BrokkGuiTickSender tickSender = new BrokkGuiTickSender();
            MinecraftForge.EVENT_BUS.register(tickSender);
            BrokkGuiPlatform.getInstance().setTickSender(tickSender);
        }
    }
}