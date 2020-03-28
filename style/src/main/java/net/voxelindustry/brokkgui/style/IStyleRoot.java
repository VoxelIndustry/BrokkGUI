package net.voxelindustry.brokkgui.style;

import net.voxelindustry.brokkgui.style.tree.StyleList;

import java.util.List;

public interface IStyleRoot
{
    String getThemeID();

     List<String> getStylesheets();

    void setStyleList(StyleList list);
}
