package net.voxelindustry.brokkgui.wrapper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;

public class BrokkGuiWrapperMod implements ClientModInitializer
{
    public static final String MODID   = "brokkguiwrapper";
    public static final String MODNAME = "BrokkGui Wrapper";
    public static final String VERSION = "0.1.0";


    @Override
    public void onInitializeClient()
    {
        BrokkGuiPlatform.getInstance().setPlatformName("FABRIC-1.14");
        BrokkGuiPlatform.getInstance().setKeyboardUtil(new KeyboardUtil());
        BrokkGuiPlatform.getInstance().setMouseUtil(new MouseUtil());

        BrokkGuiPlatform.getInstance().setGuiHelper(new GuiHelper());
    }
}
