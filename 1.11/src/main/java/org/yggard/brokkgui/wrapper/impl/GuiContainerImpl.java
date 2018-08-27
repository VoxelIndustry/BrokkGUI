package org.yggard.brokkgui.wrapper.impl;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Keyboard;
import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.GuiFocusManager;
import org.yggard.brokkgui.internal.IBrokkGuiImpl;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.internal.PopupHandler;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.wrapper.GuiHelper;
import org.yggard.brokkgui.wrapper.GuiRenderer;
import org.yggard.brokkgui.wrapper.container.BrokkGuiContainer;

import java.io.IOException;

public class GuiContainerImpl extends GuiContainer implements IBrokkGuiImpl
{
    private final BrokkGuiContainer<? extends Container> brokkgui;
    private       String                                 modID;

    private final GuiRenderer renderer;

    GuiContainerImpl(String modID, BrokkGuiContainer<? extends Container> brokkGui)
    {
        super(brokkGui.getContainer());
        this.brokkgui = brokkGui;
        this.modID = modID;
        this.renderer = new GuiRenderer(Tessellator.getInstance());
        this.brokkgui.setWrapper(this);

        brokkGui.getWidthProperty().addListener((obs, oldValue, newValue) ->
                refreshContainerWidth(newValue.intValue()));
        brokkGui.getHeightProperty().addListener((obs, oldValue, newValue) ->
                refreshContainerHeight(newValue.intValue()));

        refreshContainerWidth((int) brokkGui.getWidth());
        refreshContainerHeight((int) brokkGui.getHeight());
    }

    private void refreshContainerWidth(int newWidth)
    {
        this.width = newWidth;
        this.xSize = width;
        this.guiLeft = (this.width - this.xSize) / 2;
    }

    private void refreshContainerHeight(int newHeight)
    {
        this.height = newHeight;
        this.ySize = height;
        this.guiTop = (this.height - this.ySize) / 2;
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

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);

        this.brokkgui.onClose();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
    {
        this.brokkgui.render(mouseX, mouseY, RenderPass.BACKGROUND);
        this.brokkgui.render(mouseX, mouseY, RenderPass.MAIN);
        this.brokkgui.render(mouseX, mouseY, RenderPass.FOREGROUND);
        this.brokkgui.render(mouseX, mouseY, RenderPass.HOVER);

        PopupHandler.getInstance().renderPopupInPass(this.renderer, RenderPass.BACKGROUND, mouseX, mouseY);
        PopupHandler.getInstance().renderPopupInPass(this.renderer, RenderPass.MAIN, mouseX, mouseY);
        PopupHandler.getInstance().renderPopupInPass(this.renderer, RenderPass.FOREGROUND, mouseX, mouseY);
        PopupHandler.getInstance().renderPopupInPass(this.renderer, RenderPass.HOVER, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GlStateManager.translate(-this.guiLeft, -this.guiTop, 0);
        this.brokkgui.render(mouseX, mouseY, GuiHelper.ITEM_MAIN);
        this.brokkgui.render(mouseX, mouseY, GuiHelper.ITEM_HOVER);

        PopupHandler.getInstance().renderPopupInPass(this.renderer, GuiHelper.ITEM_MAIN, mouseX, mouseY);
        PopupHandler.getInstance().renderPopupInPass(this.renderer, GuiHelper.ITEM_HOVER, mouseX, mouseY);
        GlStateManager.translate(this.guiLeft, this.guiTop, 0);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int key) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, key);

        this.brokkgui.onClick(mouseX, mouseY, key);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.brokkgui.handleMouseInput();
    }

    @Override
    public void keyTyped(final char c, final int key) throws IOException
    {
        if (key == BrokkGuiPlatform.getInstance().getKeyboardUtil().getKeyCode("ESCAPE") ||
                GuiFocusManager.getInstance().getFocusedNode() == null)
            super.keyTyped(c, key);
        this.brokkgui.onKeyTyped(c, key);
    }

    @Override
    public void askClose()
    {
        if (this.mc.player != null)
            this.inventorySlots.onContainerClosed(this.mc.player);
        this.mc.displayGuiScreen(null);
        this.mc.setIngameFocus();

        this.brokkgui.onClose();
    }

    @Override
    public void askOpen()
    {
        // TODO : Container opening sync

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

    @Override
    protected void handleMouseClick(final Slot slot, final int slotID, final int button, final ClickType flag)
    {
        super.handleMouseClick(slot, slotID, button, flag);

        if (slot != null)
            this.brokkgui.slotClick(slot, button);
    }
}