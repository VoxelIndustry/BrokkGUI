package org.yggard.brokkgui.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class GuiRenderItemHelper
{
    private       RenderItem itemRender;
    private final Minecraft  mc;

    GuiRenderItemHelper()
    {
        this.mc = Minecraft.getMinecraft();
        this.itemRender = RenderItem.getInstance();
    }

    public void renderItemStack(ItemStack stack, int x, int y, float zLevel)
    {
        float previousZLevel = this.itemRender.zLevel;
        this.itemRender.zLevel = zLevel;

        this.itemRender.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, stack, x, y);

        this.itemRender.zLevel = previousZLevel;
    }
}
