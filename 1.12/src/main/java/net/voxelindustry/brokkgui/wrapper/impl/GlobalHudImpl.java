package net.voxelindustry.brokkgui.wrapper.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.wrapper.GuiHelper;
import net.voxelindustry.brokkgui.wrapper.GuiRenderer;

import java.util.ArrayList;
import java.util.List;

public class GlobalHudImpl
{
    private final Minecraft   mc;
    private final GuiRenderer renderer;

    private final List<HudImpl> openHUDs, closedHUDs;

    public GlobalHudImpl(Minecraft mc)
    {
        this.mc = mc;
        this.renderer = new GuiRenderer(Tessellator.getInstance());

        this.openHUDs = new ArrayList<>();
        this.closedHUDs = new ArrayList<>();
    }

    public int getScreenWidth()
    {
        return new ScaledResolution(mc).getScaledWidth();
    }

    public int getScreenHeight()
    {
        return new ScaledResolution(mc).getScaledHeight();
    }

    public IGuiRenderer getRenderer()
    {
        return this.renderer;
    }

    public void askOpen(HudImpl impl)
    {

    }

    public void askClose(HudImpl impl)
    {

    }

    public void attach(HudImpl impl)
    {
        if (impl.isOpenByDefault())
            this.openHUDs.add(impl);
        else
            this.closedHUDs.add(impl);
    }

    public void render()
    {
        this.openHUDs.forEach(hud ->
        {
            hud.getScreen().getScreenWidthProperty().setValue(this.getScreenWidth());
            hud.getScreen().getScreenHeightProperty().setValue(this.getScreenHeight());

            hud.getScreen().render(0, 0, RenderTarget.MAIN,
                    RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER,
                    GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);
            hud.getScreen().render(0, 0, RenderTarget.WINDOW,
                    RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER,
                    GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);
            hud.getScreen().render(0, 0, RenderTarget.POPUP,
                    RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER,
                    GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);
            hud.getScreen().renderLast(0, 0);
        });
    }
}
