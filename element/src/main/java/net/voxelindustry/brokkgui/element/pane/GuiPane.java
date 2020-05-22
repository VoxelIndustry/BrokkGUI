package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public class GuiPane extends GuiFather
{
    public GuiPane()
    {
        this.transform().bindChild(false);
    }

    @Override
    public String type()
    {
        return "pane";
    }

    @Override
    public void addChild(final GuiElement node)
    {
        super.addChild(node);

        RelativeBindingHelper.bindToCenter(node.transform(), this.transform());
    }
}