package net.voxelindustry.brokkgui.wrapper.impl;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.internal.IBrokkGuiWrapper;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.wrapper.GuiHelper;
import net.voxelindustry.brokkgui.wrapper.hud.HUDConfig;

public class HudWrapper implements IBrokkGuiWrapper
{
    private GlobalHudImpl  global;
    private BrokkGuiScreen screen;

    private HUDConfig config;

    public HudWrapper(GlobalHudImpl global, BrokkGuiScreen screen, HUDConfig config)
    {
        this.global = global;
        this.screen = screen;

        this.config = config;

        this.screen.setWrapper(this);

        this.screen.getScreenWidthProperty().setValue(global.getScreenWidth());
        this.screen.getScreenHeightProperty().setValue(global.getScreenHeight());
    }

    public boolean isOpenByDefault()
    {
        return config.isOpenByDefault();
    }

    public RenderGameOverlayEvent.ElementType getRenderType()
    {
        return config.getType();
    }

    public HUDConfig getConfig()
    {
        return config;
    }

    @Override
    public int getScreenWidth()
    {
        return this.global.getScreenWidth();
    }

    @Override
    public int getScreenHeight()
    {
        return this.global.getScreenHeight();
    }

    @Override
    public void askOpen()
    {
        this.global.askOpen(this);
    }

    @Override
    public void askClose()
    {
        this.global.askClose(this);
    }

    @Override
    public IGuiRenderer getRenderer()
    {
        return this.global.getRenderer();
    }

    @Override
    public String getThemeID()
    {
        return null;
    }

    public BrokkGuiScreen getScreen()
    {
        return screen;
    }

    @Override
    public boolean allowDebug()
    {
        return false;
    }

    public void render()
    {
        getScreen().render(0, 0, RenderTarget.MAIN,
                RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER,
                GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);
        getScreen().render(0, 0, RenderTarget.WINDOW,
                RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER,
                GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);
        getScreen().render(0, 0, RenderTarget.POPUP,
                RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER,
                GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);
        getScreen().renderLast(0, 0);
    }
}
