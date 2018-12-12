package net.voxelindustry.brokkgui.wrapper.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.class_308;
import net.minecraft.client.gui.ContainerGui;
import net.minecraft.client.render.Tessellator;
import net.minecraft.container.ActionTypeSlot;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.wrapper.GuiHelper;
import net.voxelindustry.brokkgui.wrapper.GuiRenderer;
import net.voxelindustry.brokkgui.wrapper.container.BrokkGuiContainer;
import org.lwjgl.glfw.GLFW;

public class GuiContainerImpl extends ContainerGui implements IBrokkGuiImpl
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
        this.containerWidth = width;
        this.left = (this.width - this.containerWidth) / 2;
    }

    private void refreshContainerHeight(int newHeight)
    {
        this.height = newHeight;
        this.containerHeight = height;
        this.top = (this.height - this.containerHeight) / 2;
    }

    @Override
    public void onInitialized()
    {
        super.onInitialized();

        this.brokkgui.getScreenWidthProperty().setValue(this.width);
        this.brokkgui.getScreenHeightProperty().setValue(this.height);

        this.client.keyboard.enableRepeatEvents(true);
        this.brokkgui.initGui();
    }

    @Override
    public void onClosed()
    {
        super.onClosed();
        this.client.keyboard.enableRepeatEvents(false);

        this.brokkgui.onClose();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks)
    {
        this.drawBackground();
        super.draw(mouseX, mouseY, partialTicks);

        if (brokkgui.allowContainerHover(mouseX, mouseY))
            this.drawMousoverTooltip(mouseX, mouseY);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY)
    {
        this.brokkgui.render(mouseX, mouseY, RenderTarget.MAIN,
                RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER);
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY)
    {
        GlStateManager.translatef(-this.left, -this.top, 0);
        this.brokkgui.render(mouseX, mouseY, RenderTarget.MAIN, GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);

        // RenderHelper disableGUIStandardItemLighting
        class_308.method_1450();

        this.brokkgui.render(mouseX, mouseY, RenderTarget.WINDOW, RenderPass.BACKGROUND, RenderPass.MAIN,
                RenderPass.FOREGROUND, RenderPass.HOVER, GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);

        // RenderHelper disableGUIStandardItemLighting
        class_308.method_1450();

        this.brokkgui.render(mouseX, mouseY, RenderTarget.POPUP, RenderPass.BACKGROUND, RenderPass.MAIN,
                RenderPass.FOREGROUND, RenderPass.HOVER, GuiHelper.ITEM_MAIN, GuiHelper.ITEM_HOVER);

        this.brokkgui.renderLast(mouseX, mouseY);
        GlStateManager.translatef(this.left, this.top, 0);
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int key)
    {
        this.brokkgui.onClick((int) mouseX, (int) mouseY, key);

        return super.mouseClicked(mouseX, mouseY, key);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int clickedMouseButton, double timeSinceLastClick,
                                double unknown)
    {
        this.brokkgui.onClickDrag((int) mouseX, (int) mouseY, clickedMouseButton, (long) timeSinceLastClick);

        return super.mouseDragged(mouseX, mouseY, clickedMouseButton, timeSinceLastClick, unknown);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state)
    {
        this.brokkgui.onClickStop((int) mouseX, (int) mouseY, state);

        return super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public boolean mouseScrolled(double scrolled)
    {
        this.brokkgui.handleMouseScroll(scrolled);
        return super.mouseScrolled(scrolled);
    }

    @Override
    public boolean keyPressed(int c, int key, int var3)
    {
        if (key == GLFW.GLFW_KEY_ESCAPE || GuiFocusManager.getInstance().getFocusedNode() == null)
            return super.keyPressed(c, key, var3);
        this.brokkgui.onKeyTyped((char) c, key);
        return true;
    }

    @Override
    public void askClose()
    {
        if (this.client.player != null)
            this.client.player.closeGui();
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
    protected void onMouseClick(final Slot slot, final int slotID, final int button, final ActionTypeSlot flag)
    {
        super.onMouseClick(slot, slotID, button, flag);

        if (slot != null)
            this.brokkgui.slotClick(slot, button);
    }
}
