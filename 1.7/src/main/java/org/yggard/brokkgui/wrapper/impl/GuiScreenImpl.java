package org.yggard.brokkgui.wrapper.impl;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Keyboard;
import org.yggard.brokkgui.gui.BrokkGuiScreen;
import org.yggard.brokkgui.internal.IBrokkGuiImpl;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.wrapper.GuiRenderer;

public class GuiScreenImpl extends GuiScreen implements IBrokkGuiImpl
{
    private final BrokkGuiScreen brokkgui;
    private final String         modID;

    private final GuiRenderer renderer;

    public GuiScreenImpl(String modID, BrokkGuiScreen brokkgui)
    {
        this.brokkgui = brokkgui;
        this.modID = modID;
        this.renderer = new GuiRenderer(Tessellator.instance);
        this.brokkgui.setWrapper(this);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        this.brokkgui.getScreenWidthProperty().setValue(this.width);
        this.brokkgui.getScreenHeightProperty().setValue(this.height);

        Keyboard.enableRepeatEvents(true);
        this.brokkgui.initGui();
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);

        this.brokkgui.onClose();
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.brokkgui.render(mouseX, mouseY, EGuiRenderPass.MAIN);
        this.brokkgui.render(mouseX, mouseY, EGuiRenderPass.HOVER);
        this.brokkgui.render(mouseX, mouseY, EGuiRenderPass.SPECIAL);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int key)
    {
        super.mouseClicked(mouseX, mouseY, key);

        this.brokkgui.onClick(mouseX, mouseY, key);
    }

    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        this.brokkgui.handleMouseInput();
    }

    @Override
    public void keyTyped(final char c, final int key)
    {
        super.keyTyped(c, key);
        this.brokkgui.onKeyTyped(c, key);
    }

    @Override
    public void askClose()
    {
        this.mc.displayGuiScreen(null);
        this.mc.setIngameFocus();

        this.brokkgui.onClose();
    }

    @Override
    public void askOpen()
    {
        this.mc.displayGuiScreen(this);

        this.brokkgui.onOpen();
    }

    @Override
    public int getScreenWidth()
    {
        return this.width;
    }

    @Override
    public int getScreenHeight()
    {
        return this.height;
    }

    @Override
    public IGuiRenderer getRenderer()
    {
        return this.renderer;
    }

    @Override
    public String getThemeID()
    {
        return this.modID;
    }
}