package net.voxelindustry.brokkgui.demo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.voxelindustry.brokkgui.wrapper.impl.BrokkGuiManager;

/**
 * @author Ourten 31 oct. 2016
 */
public class DemoGuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
            final int y, final int z)
    {
        if (ID == 0)
            return new ContainerDemo(player.inventory);
        return null;
    }

    @Override
    public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x,
            final int y, final int z)
    {
        if (ID == 0)
            return BrokkGuiManager.getBrokkGuiContainer(new GuiContainerDemo(new ContainerDemo(player.inventory)));
        return null;
    }
}