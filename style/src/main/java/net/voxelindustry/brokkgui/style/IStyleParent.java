package net.voxelindustry.brokkgui.style;

import java.util.List;

public interface IStyleParent
{
    List<StyleHolder> getChildStyles();

    int getChildCount();
}
