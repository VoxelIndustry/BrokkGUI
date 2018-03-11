package org.yggard.brokkgui.demo.category;

import org.yggard.brokkgui.element.GuiLabel;
import org.yggard.brokkgui.element.GuiListView;
import org.yggard.brokkgui.panel.GuiRelativePane;

import java.util.Arrays;

public class ListViewDemo extends GuiRelativePane
{
    public ListViewDemo()
    {
        final GuiListView<String> listView = new GuiListView<>();

        listView.setWidth(100);
        listView.setHeight(30);

        listView.setCellHeight(20);
        listView.setCellWidth(100);
        listView.setStyle("-border-color: gray; -border-thin: 1;");

        listView.setPlaceholder(new GuiLabel("I'm a placeholder"));

        listView.setElements(Arrays.asList("One", "Two", "Three"));

        this.addChild(listView, 0.5f, 0.5f);
    }
}
