package net.voxelindustry.brokkgui.demo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

/**
 * @author Ourten 31 oct. 2016
 */
public class ContainerDemo extends Container
{
    public ContainerDemo(final InventoryPlayer player)
    {
        int l;
        for (l = 0; l < 3; ++l)
            for (int i1 = 0; i1 < 9; ++i1)
                this.addSlotToContainer(new Slot(player, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
        for (l = 0; l < 9; ++l)
            this.addSlotToContainer(new Slot(player, l, 8 + l * 18, 142));
    }

    @Override
    public boolean canInteractWith(final EntityPlayer player)
    {
        return true;
    }
}