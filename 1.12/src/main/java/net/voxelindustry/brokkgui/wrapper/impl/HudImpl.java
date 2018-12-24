package net.voxelindustry.brokkgui.wrapper.impl;

import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;

public class HudImpl implements IBrokkGuiImpl
{
    private GlobalHudImpl  global;
    private BrokkGuiScreen screen;
    private boolean        openByDefault;

    public HudImpl(GlobalHudImpl global, BrokkGuiScreen screen)
    {
        this.global = global;
        this.screen = screen;

        this.screen.setWrapper(this);

        this.screen.getScreenWidthProperty().setValue(global.getScreenWidth());
        this.screen.getScreenHeightProperty().setValue(global.getScreenHeight());
    }

    public boolean isOpenByDefault()
    {
        return openByDefault;
    }

    public void setOpenByDefault(boolean openByDefault)
    {
        this.openByDefault = openByDefault;
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
}
