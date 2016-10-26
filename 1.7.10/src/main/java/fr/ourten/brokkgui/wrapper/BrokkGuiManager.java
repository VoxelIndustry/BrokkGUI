package fr.ourten.brokkgui.wrapper;

import fr.ourten.brokkgui.gui.BrokkGuiScreen;
import fr.ourten.brokkgui.wrapper.container.BrokkGuiContainer;
import fr.ourten.brokkgui.wrapper.impl.GuiContainerImpl;
import fr.ourten.brokkgui.wrapper.impl.GuiScreenImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

/**
 * @author Ourten 4 oct. 2016
 */
public class BrokkGuiManager
{
    public static final GuiScreen getBrokkGuiScreen(final BrokkGuiScreen brokkGui)
    {
        return new GuiScreenImpl(brokkGui);
    }

    public static final GuiContainer getBrokkGuiContainer(final BrokkGuiContainer<? extends Container> brokkGui)
    {
        return new GuiContainerImpl(brokkGui);
    }

    public static final void openBrokkGuiScreen(final BrokkGuiScreen brokkGui)
    {
        Minecraft.getMinecraft().displayGuiScreen(BrokkGuiManager.getBrokkGuiScreen(brokkGui));
    }
}