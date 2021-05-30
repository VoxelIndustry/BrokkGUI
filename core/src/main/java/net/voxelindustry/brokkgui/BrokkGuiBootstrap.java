package net.voxelindustry.brokkgui;

import net.voxelindustry.brokkgui.markup.ElementMarkupSetup;
import net.voxelindustry.brokkgui.style.StyleEngine;

public class BrokkGuiBootstrap
{
    public static void bootstrap()
    {
        BrokkGuiPlatform.getInstance();
        StyleEngine.getInstance().start();
        ElementMarkupSetup.setup();
    }
}
