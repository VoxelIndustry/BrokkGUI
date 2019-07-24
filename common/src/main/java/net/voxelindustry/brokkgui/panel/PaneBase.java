package net.voxelindustry.brokkgui.panel;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

import java.util.Objects;

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
        Objects.requireNonNull(element);

        this.addChild(element.transform());

        RelativeBindingHelper.bindToCenter(element.transform(), this.transform());
    }

    public void addChild(Transform transform)
    {
        Objects.requireNonNull(transform);

        transform().addChild(transform);
    }

    public void removeChild(Transform transform)
    {
        Objects.requireNonNull(transform);

        transform().removeChild(transform);
    }

    public void removeChild(GuiElement element)
    {
        Objects.requireNonNull(element);
        this.removeChild(element.transform());
    }

    public void clearChildren()
    {
        this.transform().clearChildren();
    }
}