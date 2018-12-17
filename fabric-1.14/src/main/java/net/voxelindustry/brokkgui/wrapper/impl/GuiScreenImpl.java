package net.voxelindustry.brokkgui.wrapper.impl;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.Tessellator;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.GuiFocusManager;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.wrapper.GuiHelper;
import net.voxelindustry.brokkgui.wrapper.GuiRenderer;
import org.lwjgl.glfw.GLFW;

public class GuiScreenImpl extends Gui implements IBrokkGuiImpl
{
    private final BrokkGuiScreen brokkgui;
    private final String         modID;

    private final GuiRenderer renderer;

    GuiScreenImpl(String modID, BrokkGuiScreen brokkgui)
    {
        this.brokkgui = brokkgui;
        this.modID = modID;
        this.renderer = new GuiRenderer(Tessellator.getInstance());
        this.brokkgui.setWrapper(this);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
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
        super.draw(mouseX, mouseY, partialTicks);

        this.brokkgui.render(mouseX, mouseY, RenderTarget.MAIN,
                RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER, GuiHelper.ITEM_MAIN,
                GuiHelper.ITEM_HOVER);
        this.brokkgui.render(mouseX, mouseY, RenderTarget.WINDOW,
                RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER, GuiHelper.ITEM_MAIN,
                GuiHelper.ITEM_HOVER);
        this.brokkgui.render(mouseX, mouseY, RenderTarget.POPUP,
                RenderPass.BACKGROUND, RenderPass.MAIN, RenderPass.FOREGROUND, RenderPass.HOVER, GuiHelper.ITEM_MAIN,
                GuiHelper.ITEM_HOVER);
        this.brokkgui.renderLast(mouseX, mouseY);
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
        System.out.println(scrolled+"  "+ BrokkGuiPlatform.getInstance().getMouseUtil().getEventDWheel());
        this.brokkgui.handleMouseScroll(scrolled*120);
        return super.mouseScrolled(scrolled);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modsField)
    {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || GuiFocusManager.getInstance().getFocusedNode() == null)
            return super.keyPressed(keyCode, scanCode, modsField);

        this.brokkgui.onKeyTyped((char) 0, keyCode);
        return true;
    }

    @Override
    public boolean charTyped(char typedChar, int modsField)
    {
        if (GuiFocusManager.getInstance().getFocusedNode() != null)
        {
            this.brokkgui.onKeyTyped(typedChar, -1);
            return true;
        }
        return false;
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
        this.client.openGui(this);

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

    public BrokkGuiScreen getGui()
    {
        return brokkgui;
    }
}
