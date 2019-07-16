package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.panel.RelativePaneBase;
import net.voxelindustry.brokkgui.style.StyleHolder;

public class RelativePane extends RelativePaneBase
{
    public RelativePane()
    {
        this.add(StyleHolder.class);
    }

    public StyleHolder style()
    {
        return this.get(StyleHolder.class);
    }
}
