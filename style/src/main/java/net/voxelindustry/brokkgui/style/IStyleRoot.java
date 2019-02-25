package net.voxelindustry.brokkgui.style;

import fr.ourten.teabeans.value.BaseListProperty;
import net.voxelindustry.brokkgui.style.tree.StyleList;

public interface IStyleRoot
{
    String getThemeID();

     BaseListProperty<String> getStylesheetsProperty();

    void setStyleTree(StyleList tree);
}
