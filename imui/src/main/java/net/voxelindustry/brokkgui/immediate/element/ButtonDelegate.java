package net.voxelindustry.brokkgui.immediate.element;

import com.sun.webkit.dom.KeyboardEventImpl;
import net.voxelindustry.brokkgui.paint.Color;

public interface ButtonDelegate extends LabelDelegate, CommonLib {

    default boolean button(int x, int y, String label) {
        int width = (int)guiHelper().getStringWidth(label);
        int height = (int)guiHelper().getStringHeight();
        guiHelper().drawColoredRect(guiRenderer(), x, y, width+ 2, height + 2, 1, Color.WHITE);
        label(x + 1, y + 1, label,2);
        return mouseClickInArea(x,y, x+width, y +height, 0);
    }
}
