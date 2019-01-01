package net.voxelindustry.brokkgui.wrapper.impl;

import net.minecraft.client.renderer.Tessellator;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.internal.IBrokkGuiWrapper;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.wrapper.GuiHelper;
import net.voxelindustry.brokkgui.wrapper.GuiRenderer;

public class WorldScreenWrapper implements IBrokkGuiWrapper
{
    private int windowWidth, windowHeight;
    private final BrokkGuiScreen screen;
    private final GuiRenderer    renderer;

    public WorldScreenWrapper(BrokkGuiScreen screen, int windowWidth, int windowHeight)
    {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        this.screen = screen;
        this.renderer = new GuiRenderer(Tessellator.getInstance());

        this.screen.setWrapper(this);
    }

    @Override
    public int getScreenWidth()
    {
        return this.windowWidth;
    }

    @Override
    public int getScreenHeight()
    {
        return this.windowHeight;
    }

    @Override
    public void askOpen()
    {

    }

    @Override
    public void askClose()
    {

    }

    @Override
    public IGuiRenderer getRenderer()
    {
        return this.renderer;
    }

    @Override
    public String getThemeID()
    {
        return null;
    }

    public void render()
    {
        screen.render(0, 0, RenderTarget.MAIN,
                RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER,
                GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);
        screen.render(0, 0, RenderTarget.WINDOW,
                RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER,
                GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);
        screen.render(0, 0, RenderTarget.POPUP,
                RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER,
                GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);
        screen.renderLast(0, 0);
    }

    @Override
    public boolean allowDebug()
    {
        return false;
    }
}
