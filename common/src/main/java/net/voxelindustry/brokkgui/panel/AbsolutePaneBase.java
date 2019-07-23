package net.voxelindustry.brokkgui.panel;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public abstract class AbsolutePaneBase extends PaneBase
{
    @Override
    public void addChild(Transform transform)
    {
        this.addChild(transform, this.transform().width() / 2 - transform.width() / 2,
                this.transform().height() / 2 - transform.height() / 2);
    }

    public void addChild(Transform transform, float posX, float posY)
    {
        super.addChild(transform);

        RelativeBindingHelper.bindToPos(transform, this.transform(), posX, posY);
    }

    public void setChildPos(Transform transform, float posX, float posY)
    {
        if (transform().hasChild(transform))
            RelativeBindingHelper.bindToPos(transform, this.transform(), posX, posY);
    }

    public void addChild(GuiElement element, float posX, float posY)
    {
        this.addChild(element.transform(), posX, posY);
    }

    public void setChildPos(GuiElement element, float posX, float posY)
    {
        this.setChildPos(element.transform(), posX, posY);
    }
}