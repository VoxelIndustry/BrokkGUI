package net.voxelindustry.brokkgui.panel;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.Transform;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public class PaneBase extends GuiElement
{
    public PaneBase()
    {
        transform().bindChild(false);
    }

    @Override
    public String type()
    {
        return "pane";
    }

    public void addChild(GuiElement element)
    {
        this.addChild(element.transform());

        RelativeBindingHelper.bindToCenter(element.transform(), this.transform());
    }

    public void addChild(Transform transform)
    {
        transform().addChild(transform);
    }

    public void removeChild(Transform transform)
    {
        transform().removeChild(transform);
    }

    public void removeChild(GuiElement element)
    {
        this.removeChild(element.transform());
    }

    public void clearChildren()
    {
        this.transform().clearChildren();
    }
}