package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.style.StyledElement;

public class GuiPane extends GuiElement implements StyledElement
{
    public GuiPane()
    {
        transform().bindChild(false);
    }

    @Override
    public String type()
    {
        return "pane";
    }

    @Override
    public void addChild(GuiElement node)
    {
        super.addChild(node);

        RelativeBindingHelper.bindToCenter(node.transform(), transform());
    }
}