package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.text.GuiOverflow;

public class ScrollPane extends GuiFather
{
    public ScrollPane()
    {
        transform().overflow(GuiOverflow.SCROLL);
    }

    @Override
    public String type()
    {
        return "scrollpane";
    }
}