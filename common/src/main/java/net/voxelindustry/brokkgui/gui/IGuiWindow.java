package net.voxelindustry.brokkgui.gui;

import fr.ourten.teabeans.value.IProperty;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;
import net.voxelindustry.brokkgui.panel.PaneBase;

/**
 * @author Ourten 31 oct. 2016
 */
public interface IGuiWindow extends IGuiSubWindow
{
    void onOpen();

    void onClose();

    void setWrapper(IBrokkGuiImpl wrapper);

    void screenWidth(int width);

    void screenHeight(int height);

    IProperty<Integer> screenWidthProperty();

    IProperty<Integer> screenHeightProperty();

    void render(int mouseX, int mouseY, RenderTarget target, RenderPass... pass);

    void renderLast(int mouseX, int mouseY);

    void tick();

    void initGui();

    IBrokkGuiImpl wrapper();

    PaneBase getMainPanel();

    boolean doesOccludePoint(int mouseX, int mouseY);

    ////////////
    // INPUTS //
    ////////////

    void onKeyPressed(int key);

    void onKeyTyped(char c, int key);

    void onKeyReleased(int key);

    void onClick(int mouseX, int mouseY, int key);

    void onClickDrag(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick);

    void onClickStop(int mouseX, int mouseY, int state);

    void handleMouseScroll(double scrolled);
}