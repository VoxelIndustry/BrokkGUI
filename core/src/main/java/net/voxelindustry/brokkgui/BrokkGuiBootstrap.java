package net.voxelindustry.brokkgui;

import net.voxelindustry.brokkgui.style.StyleEngine;

public class BrokkGuiBootstrap
{
    private static BrokkGuiBootstrap INSTANCE;

    public static BrokkGuiBootstrap getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new BrokkGuiBootstrap();
        return INSTANCE;
    }

    private BrokkGuiBootstrap()
    {
        BrokkGuiPlatform.getInstance();
        StyleEngine.getInstance().start();
    }
}
