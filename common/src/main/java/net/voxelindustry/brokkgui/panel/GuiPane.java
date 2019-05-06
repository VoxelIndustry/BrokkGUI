package net.voxelindustry.brokkgui.panel;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.Transform;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public class GuiPane extends GuiElement
{
    public GuiPane()
    {
        transform().bindChild(false);
    }

    @Override
    protected String getType()
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