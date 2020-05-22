package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public class GuiAbsolutePane extends GuiPane
{
    @Override
    public void addChild(GuiElement node)
    {
        this.addChild(node, width() / 2 - node.width() / 2, height() / 2 - node.height() / 2);
    }

    public void addChild(GuiElement node, float posX, float posY)
    {
        super.addChild(node);

        RelativeBindingHelper.bindToPos(node.transform(), transform(), posX, posY);
    }

    public void setChildPos(GuiElement node, float posX, float posY)
    {
        if (!transform().hasChild(node.transform()))
            return;

        node.transform().xPosProperty().unbind();
        node.transform().yPosProperty().unbind();

        RelativeBindingHelper.bindToPos(node.transform(), transform(), posX, posY);
    }
}