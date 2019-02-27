package net.voxelindustry.brokkgui.panel;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.policy.GuiOverflowPolicy;

public class GuiPane extends GuiFather
{
    private final GuiOverflowPolicy guiOverflowPolicy;

    public GuiPane()
    {
        super("pane");

        this.guiOverflowPolicy = GuiOverflowPolicy.NONE;
    }

    @Override
    public void addChild(final GuiNode node)
    {
        super.addChild(node);

        RelativeBindingHelper.bindToCenter(node, this);
    }

    @Override
    public void removeChild(final GuiNode node)
    {
        super.removeChild(node);

        node.getxPosProperty().unbind();
        node.getyPosProperty().unbind();
    }

    @Override
    public void clearChilds()
    {
        this.getChildrensProperty().getValue().forEach(node ->
        {
            node.setFather(null);
            node.getxPosProperty().unbind();
            node.getyPosProperty().unbind();
        });
        this.getChildrensProperty().clear();
    }

    @Override
    public GuiOverflowPolicy getGuiOverflowPolicy()
    {
        return this.guiOverflowPolicy;
    }
}