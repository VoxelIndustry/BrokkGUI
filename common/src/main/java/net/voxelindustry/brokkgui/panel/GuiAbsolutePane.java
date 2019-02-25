package net.voxelindustry.brokkgui.panel;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public class GuiAbsolutePane extends GuiPane
{
    @Override
    public void addChild(final GuiNode node)
    {
        this.addChild(node, this.getWidth() / 2 - node.getWidth() / 2, this.getHeight() / 2 - node.getHeight() / 2);
    }

    public void addChild(final GuiNode node, final float posX, final float posY)
    {
        this.getChildrensProperty().add(node);
        node.setFather(this);

        node.getxPosProperty().unbind();
        node.getyPosProperty().unbind();

        RelativeBindingHelper.bindToPos(node, this, posX, posY);
    }

    public void setChildPos(final GuiNode node, final float posX, final float posY)
    {
        if (this.getChildrensProperty().contains(node))
        {
            node.getxPosProperty().unbind();
            node.getyPosProperty().unbind();

            RelativeBindingHelper.bindToPos(node, this, posX, posY);
        }
    }
}