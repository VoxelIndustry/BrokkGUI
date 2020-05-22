package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public class GuiRelativePane extends GuiPane
{
    @Override
    public void addChild(GuiElement node)
    {
        this.addChild(node, .5f, .5f);
    }

    public void addChild(GuiElement node, float ratioX, float ratioY)
    {
        super.addChild(node);

        RelativeBindingHelper.bindToRelative(node.transform(), transform(), ratioX, ratioY);
    }

    public void setChildPos(GuiElement node, float ratioX, float ratioY)
    {
        if (!transform().hasChild(node.transform()))
            return;

        node.transform().xPosProperty().unbind();
        node.transform().yPosProperty().unbind();

        RelativeBindingHelper.bindToRelative(node.transform(), transform(), ratioX, ratioY);
    }
}