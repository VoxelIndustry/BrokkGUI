package net.voxelindustry.brokkgui.window;

import fr.ourten.teabeans.property.IProperty;
import net.voxelindustry.brokkgui.event.MouseInputCode;
import net.voxelindustry.brokkgui.internal.IBrokkGuiImpl;
import net.voxelindustry.brokkgui.paint.RenderTarget;

/**
 * @author Ourten 31 oct. 2016
 */
public interface IGuiWindow extends IGuiSubWindow
{
    void onOpen();

    void onClose();

    void setWrapper(IBrokkGuiImpl wrapper);

    default int screenWidth()
    {
        return screenWidthProperty().getValue();
    }

    default int screenHeight()
    {
        return screenHeightProperty().getValue();
    }

    void screenWidth(int width);

    void screenHeight(int height);

    IProperty<Integer> screenWidthProperty();

    IProperty<Integer> screenHeightProperty();

    float windowWidthRatio();

    float windowHeightRatio();

    void render(float mouseX, float mouseY, RenderTarget target);

    void renderLast(float mouseX, float mouseY);

    void tick();

    void initGui();

    IBrokkGuiImpl getWrapper();

    boolean doesOccludePoint(float mouseX, float mouseY);

    ////////////
    // INPUTS //
    ////////////

    boolean onMouseMoved(float mouseX, float mouseY);

    boolean onKeyPressed(int scanCode, int keyCode);

    boolean onTextTyped(String text);

    boolean onKeyReleased(int scanCode, int keyCode);

    boolean onClick(float mouseX, float mouseY, MouseInputCode key);

    boolean onClickDrag(float mouseX, float mouseY, MouseInputCode clickedMouseButton);

    boolean onClickStop(float mouseX, float mouseY, MouseInputCode key);

    boolean onScroll(float mouseX, float mouseY, double xOffset, double yOffset);
}