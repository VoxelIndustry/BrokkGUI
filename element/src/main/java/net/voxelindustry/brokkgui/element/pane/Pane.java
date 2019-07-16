package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.panel.PaneBase;
import net.voxelindustry.brokkgui.style.StyleHolder;

public class Pane extends PaneBase
{
    public Pane()
    {
        this.add(StyleHolder.class);
    }

    public StyleHolder style()
    {
        return this.get(StyleHolder.class);
    }
}
