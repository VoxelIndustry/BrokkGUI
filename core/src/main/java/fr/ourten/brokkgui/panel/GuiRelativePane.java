package fr.ourten.brokkgui.panel;

import fr.ourten.brokkgui.component.GuiNode;
import fr.ourten.brokkgui.data.RelativeBindingHelper;

/**
 * @author Ourten 9 oct. 2016
 */
public class GuiRelativePane extends GuiPane
{
    @Override
    public void addChild(final GuiNode node)
    {
        this.addChild(node, .5f, .5f);
    }

    public void addChild(final GuiNode node, final float ratioX, final float ratioY)
    {
        this.getChildrensProperty().add(node);
        RelativeBindingHelper.bindToRelative(node, this, ratioX, ratioY);
    }

    public void setChildPos(final GuiNode node, final float ratioX, final float ratioY)
    {
        if (this.getChildrensProperty().contains(node))
        {
            node.getxPosProperty().unbind();
            node.getyPosProperty().unbind();

            RelativeBindingHelper.bindToRelative(node, this, ratioX, ratioY);
        }
    }
}