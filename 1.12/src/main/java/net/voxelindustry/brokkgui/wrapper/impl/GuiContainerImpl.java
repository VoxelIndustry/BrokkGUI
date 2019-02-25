package net.voxelindustry.brokkgui.wrapper.impl;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.wrapper.GuiHelper;
import net.voxelindustry.brokkgui.wrapper.GuiRenderer;
import net.voxelindustry.brokkgui.wrapper.container.BrokkGuiContainer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

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

        if (brokkgui.allowContainerHover(mouseX, mouseY))
            this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);

        this.brokkgui.onClose();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        this.brokkgui.render(mouseX, mouseY, RenderTarget.MAIN,
                RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GlStateManager.translate(-this.guiLeft, -this.guiTop, 0);
        this.brokkgui.render(mouseX, mouseY, RenderTarget.MAIN, GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);
        RenderHelper.disableStandardItemLighting();
        this.brokkgui.render(mouseX, mouseY, RenderTarget.WINDOW, RenderPass.BACKGROUND, RenderPass.MAIN,
                RenderPass.FOREGROUND, RenderPass.HOVER, GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);
        RenderHelper.disableStandardItemLighting();
        this.brokkgui.render(mouseX, mouseY, RenderTarget.POPUP, RenderPass.BACKGROUND, RenderPass.MAIN,
                RenderPass.FOREGROUND, RenderPass.HOVER, GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);

        this.brokkgui.renderLast(mouseX, mouseY);
        GlStateManager.translate(this.guiLeft, this.guiTop, 0);

        brokkgui.tick();
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
        this.brokkgui.handleMouseScroll(Mouse.getEventDWheel());
    }

    @Override
    public void handleKeyboardInput() throws IOException
    {
        super.handleKeyboardInput();

        char c0 = Keyboard.getEventCharacter();

        if (Keyboard.getEventKey() == 0 && c0 >= ' ' || !Keyboard.getEventKeyState())
            this.brokkgui.onKeyReleased(Keyboard.getEventKey());
    }

    @Override
    public void keyTyped(final char c, final int key) throws IOException
    {
        if (key == BrokkGuiPlatform.getInstance().getKeyboardUtil().getKeyCode("ESCAPE") ||
                GuiFocusManager.getInstance().getFocusedNode() == null)
            super.keyTyped(c, key);
        this.brokkgui.onKeyTyped(c, key);
        this.brokkgui.onKeyPressed(key);
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
    public float getGuiRelativePosX()
    {
        return (int) (this.width / (1 / brokkgui.getxRelativePos()) - brokkgui.getWidth() / 2);
    }

    @Override
    public float getGuiRelativePosY()
    {
        return (int) (this.height / (1 / brokkgui.getyRelativePos()) - brokkgui.getHeight() / 2);
    }

    @Override
    protected void handleMouseClick(final Slot slot, final int slotID, final int button, final ClickType flag)
    {
        super.handleMouseClick(slot, slotID, button, flag);

        if (slot != null)
            this.brokkgui.slotClick(slot, button);
    }
}
