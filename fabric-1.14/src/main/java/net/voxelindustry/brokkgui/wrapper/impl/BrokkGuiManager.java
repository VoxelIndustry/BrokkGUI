package net.voxelindustry.brokkgui.wrapper.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.Screen;
import net.minecraft.container.Container;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.style.StylesheetManager;
import net.voxelindustry.brokkgui.wrapper.container.BrokkGuiContainer;

/**
 * @author Ourten 4 oct. 2016
 */
public class BrokkGuiManager
{
    public static Screen getBrokkGuiScreen(BrokkGuiScreen brokkGui)
    {
        return getBrokkGuiScreen(StylesheetManager.getInstance().DEFAULT_THEME, brokkGui);
    }

    public static Screen getBrokkGuiScreen(String modID, BrokkGuiScreen brokkGui)
    {
        return new GuiScreenImpl(modID, brokkGui);
    }

    public static ContainerScreen getBrokkGuiContainer(BrokkGuiContainer<? extends Container> brokkGui)
    {
        return getBrokkGuiContainer(StylesheetManager.getInstance().DEFAULT_THEME, brokkGui);
    }

    public static ContainerScreen getBrokkGuiContainer(String modID, BrokkGuiContainer<? extends Container> brokkGui)
    {
        return new GuiContainerImpl(modID, brokkGui);
    }

    public static void openBrokkGuiScreen(BrokkGuiScreen brokkGui)
    {
        openBrokkGuiScreen(StylesheetManager.getInstance().DEFAULT_THEME, brokkGui);
    }

    public static void openBrokkGuiScreen(String modID, BrokkGuiScreen brokkGui)
    {
        MinecraftClient.getInstance().openScreen(BrokkGuiManager.getBrokkGuiScreen(modID, brokkGui));
    }
}
