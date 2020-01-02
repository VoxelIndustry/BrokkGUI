package net.voxelindustry.brokkgui.gui;

import fr.ourten.teabeans.value.IProperty;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.panel.GuiPane;

/**
 * @author Ourten 31 oct. 2016
 */
public interface IGuiWindow extends IGuiSubWindow
{
    void onOpen();

    void onClose();

    void setWrapper(IBrokkGuiImpl wrapper);

    void setScreenWidth(int width);

    void setScreenHeight(int height);

    IProperty<Integer> getScreenWidthProperty();

    IProperty<Integer> getScreenHeightProperty();

    void render(int mouseX, int mouseY, RenderTarget target, RenderPass... pass);

    void renderLast(int mouseX, int mouseY);

    void tick();

    void initGui();

    IBrokkGuiImpl getWrapper();

    GuiPane getMainPanel();

    boolean doesOccludePoint(int mouseX, int mouseY);

    ////////////
    // INPUTS //
    ////////////

    void onKeyPressed(int key);

    void onKeyTyped(char c, int key);

    void onKeyReleased(int key);

    void onClick(int mouseX, int mouseY, int key);

    void onClickDrag(int mouseX, int mouseY, int clickedMouseButton, double dragX, double dragY);

    void onClickStop(int mouseX, int mouseY, int state);

    void handleMouseScroll(double scrolled);
}