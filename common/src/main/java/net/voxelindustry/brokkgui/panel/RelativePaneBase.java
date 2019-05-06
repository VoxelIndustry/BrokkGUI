package net.voxelindustry.brokkgui.panel;

import net.voxelindustry.brokkgui.component.Transform;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public class RelativePaneBase extends PaneBase
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
}