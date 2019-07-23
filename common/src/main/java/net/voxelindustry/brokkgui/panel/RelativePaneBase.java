package net.voxelindustry.brokkgui.panel;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public abstract class RelativePaneBase extends PaneBase
{
    @Override
    public void addChild(Transform node)
    {
        this.addChild(node, .5f, .5f);
    }

    public void addChild(Transform transform, float ratioX, float ratioY)
    {
        super.addChild(transform);

        RelativeBindingHelper.bindToRelative(transform, this.transform(), ratioX, ratioY);
    }

    public void setChildPos(Transform transform, float ratioX, float ratioY)
    {
        if (transform().hasChild(transform))
            RelativeBindingHelper.bindToRelative(transform, this.transform(), ratioX, ratioY);
    }

    public void addChild(GuiElement element, float ratioX, float ratioY)
    {
        this.addChild(element.transform(), ratioX, ratioY);
    }

    public void setChildPos(GuiElement element, float ratioX, float ratioY)
    {
        this.setChildPos(element.transform(), ratioX, ratioY);
    }
}