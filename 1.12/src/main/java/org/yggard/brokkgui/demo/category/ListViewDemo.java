package org.yggard.brokkgui.demo.category;

import org.yggard.brokkgui.element.GuiButton;
import org.yggard.brokkgui.element.GuiLabel;
import org.yggard.brokkgui.element.GuiListView;
import org.yggard.brokkgui.panel.GuiRelativePane;

import java.util.Arrays;

public class ListViewDemo extends GuiRelativePane
{
    public ListViewDemo()
    {
        final GuiListView<String> labelList = new GuiListView<>();

        labelList.setWidth(75);
        labelList.setHeight(30);

        labelList.setCellHeight(20);
        labelList.setCellWidth(75);
        labelList.setStyle("-border-color: gray; -border-thin: 1;");

        labelList.setPlaceholder(new GuiLabel("I'm a placeholder"));

        labelList.setElements(Arrays.asList("One", "Two", "Three"));

        this.addChild(labelList, 0.25f, 0.5f);

        final GuiListView<GuiButton> buttonList = new GuiListView<>();

        buttonList.setSize(75, 30);

        buttonList.setCellHeight(20);
        buttonList.setCellWidth(75);

        GuiButton button1 = new GuiButton("HEY 1");
        GuiButton button2 = new GuiButton("HELLO");
        GuiButton button3 = new GuiButton("LALALA");
        buttonList.setElements(Arrays.asList(button1, button2, button3));
        buttonList.setCellYPadding(1);

        this.addChild(buttonList, 0.75f, 0.5f);
    }
}
