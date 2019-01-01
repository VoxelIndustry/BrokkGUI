package net.voxelindustry.brokkgui.wrapper.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ContainerGui;
import net.minecraft.client.gui.Gui;
import net.minecraft.container.Container;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.style.StylesheetManager;
import net.voxelindustry.brokkgui.wrapper.container.BrokkGuiContainer;

/**
 * @author Ourten 4 oct. 2016
 */
public class BrokkGuiManager
{
    public static Gui getBrokkGuiScreen(BrokkGuiScreen brokkGui)
    {
        return getBrokkGuiScreen(StylesheetManager.getInstance().DEFAULT_THEME, brokkGui);
    }

    public static Gui getBrokkGuiScreen(String modID, BrokkGuiScreen brokkGui)
    {
        return new GuiScreenWrapper(modID, brokkGui);
    }

    public static ContainerGui getBrokkGuiContainer(BrokkGuiContainer<? extends Container> brokkGui)
    {
        return getBrokkGuiContainer(StylesheetManager.getInstance().DEFAULT_THEME, brokkGui);
    }

    public static ContainerGui getBrokkGuiContainer(String modID, BrokkGuiContainer<? extends Container> brokkGui)
    {
        return new GuiContainerWrapper(modID, brokkGui);
    }

    public static void openBrokkGuiScreen(BrokkGuiScreen brokkGui)
    {
        openBrokkGuiScreen(StylesheetManager.getInstance().DEFAULT_THEME, brokkGui);
    }

    public static void openBrokkGuiScreen(String modID, BrokkGuiScreen brokkGui)
    {
        MinecraftClient.getInstance().openGui(BrokkGuiManager.getBrokkGuiScreen(modID, brokkGui));
    }
}
