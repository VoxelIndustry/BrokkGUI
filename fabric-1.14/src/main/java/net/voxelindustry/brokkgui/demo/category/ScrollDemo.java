package net.voxelindustry.brokkgui.demo.category;

import net.voxelindustry.brokkgui.panel.GuiRelativePane;
import net.voxelindustry.brokkgui.element.pane.ScrollPane;
import net.voxelindustry.brokkgui.shape.Rectangle;

public class ScrollDemo extends GuiRelativePane
{
    public ScrollDemo()
    {
        ScrollPane verticalScroll = new ScrollPane();
        verticalScroll.setWidthRatio(1);
        verticalScroll.setHeightRatio(0.5f);
        verticalScroll.setGripYWidth(12);
        verticalScroll.setGripYHeight(24);

        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(64);
        rectangle.setHeight(512);
        rectangle.setID("vertscroll-rect");

        verticalScroll.setChild(rectangle);

        this.addChild(verticalScroll, 0.5f, 0.25f);

        ScrollPane horizontalScroll = new ScrollPane();
        horizontalScroll.setWidthRatio(1);
        horizontalScroll.setHeightRatio(0.5f);
        horizontalScroll.setGripXWidth(24);
        horizontalScroll.setGripXHeight(12);

        Rectangle rectangle1 = new Rectangle();
        rectangle1.setWidth(512);
        rectangle1.setHeight(64);
        rectangle1.setID("horiscroll-rect");

        horizontalScroll.setChild(rectangle1);

        this.addChild(horizontalScroll, 0.5f, 0.75f);
    }
}
