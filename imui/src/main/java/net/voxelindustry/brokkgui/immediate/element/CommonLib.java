package net.voxelindustry.brokkgui.immediate.element;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.internal.IGuiHelper;
import net.voxelindustry.brokkgui.internal.IMouseUtil;

public interface CommonLib {

    boolean isMouseClick(int key);

    default IGuiHelper guiHelper() {
        return BrokkGuiPlatform.instance().guiHelper();
    }

    default IMouseUtil mouseUtil() { return BrokkGuiPlatform.instance().mouseUtil(); }

    default boolean mouseInArea(int startX, int startY, int endX, int endY) {
        int xPos = mouseUtil().getMouseX();
        int yPos = mouseUtil().getMouseY();
        return xPos >= startX && xPos <= endX && yPos >= startY && yPos <= endY;
    }

    default boolean mouseClickInArea(int startX, int startY, int endX, int endY, int key) {
        return mouseInArea(startX, startY, endX, endY) && isMouseClick(key);
    }
}
