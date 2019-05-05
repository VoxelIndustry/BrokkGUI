package net.voxelindustry.brokkgui.panel;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.Transform;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public class GuiPane extends GuiElement
{
    public GuiPane()
    {

    }

    @Override
    protected String getType()
    {
        return "pane";
    }

    public void addChild(GuiElement element)
    {
        this.addChild(element.transform());
    }

    public void addChild(Transform transform)
    {
        transform().addChild(transform);

        RelativeBindingHelper.bindToCenter(transform, this.transform());
    }

    public void removeChild(Transform transform)
    {
        transform().removeChild(transform);

        transform.xPosProperty().unbind();
        transform.yPosProperty().unbind();
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