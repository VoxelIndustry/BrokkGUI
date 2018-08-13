package org.yggard.brokkgui.demo.category;

import org.yggard.brokkgui.panel.GuiAbsolutePane;
import org.yggard.brokkgui.panel.ScrollPane;
import org.yggard.brokkgui.shape.Rectangle;

public class ScrollDemo extends GuiAbsolutePane
{
    public ScrollDemo()
    {
        ScrollPane verticalScroll = new ScrollPane();
        verticalScroll.setWidthRatio(1);
        verticalScroll.setHeightRatio(0.5f);

        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(64);
        rectangle.setHeight(512);
        rectangle.setID("vertscroll-rect");

        verticalScroll.setChild(rectangle);

        this.addChild(verticalScroll, 0, 0);
    }
}
