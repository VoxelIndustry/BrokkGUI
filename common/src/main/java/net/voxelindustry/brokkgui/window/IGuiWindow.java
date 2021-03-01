package net.voxelindustry.brokkgui.window;

import fr.ourten.teabeans.property.IProperty;
import net.voxelindustry.brokkgui.event.MouseInputCode;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.RenderTarget;

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

    boolean doesOccludePoint(int mouseX, int mouseY);

    ////////////
    // INPUTS //
    ////////////

    void onMouseMoved(int mouseX, int mouseY);

    void onKeyPressed(int key);

    void onTextTyped(String text);

    void onKeyReleased(int key);

    void onClick(int mouseX, int mouseY, MouseInputCode key);

    void onClickDrag(int mouseX, int mouseY, MouseInputCode clickedMouseButton, double dragX, double dragY);

    void onClickStop(int mouseX, int mouseY, MouseInputCode key);

    void handleMouseScroll(double scrolled);
}