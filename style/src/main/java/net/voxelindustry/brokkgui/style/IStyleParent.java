package net.voxelindustry.brokkgui.style;

import java.util.List;

public interface IStyleParent
{
    List<StyleComponent> getChildStyles();

    int getChildCount();
}
