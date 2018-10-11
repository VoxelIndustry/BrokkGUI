package net.voxelindustry.brokkgui.panel;

import net.voxelindustry.brokkgui.behavior.GuiScrollableBehavior;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.control.GuiScrollableBase;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.skin.GuiScrollableSkin;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

/**
 * @author Ourten 9 oct. 2016
 */
public class ScrollPane extends GuiScrollableBase
{
    private GuiNode contentNode;

    public ScrollPane(final GuiNode node)
    {
        super("scrollpane");

        if (node != null)
            this.setChild(node);
    }

    public ScrollPane()
    {
        this(null);
    }

    public void setChild(final GuiNode node)
    {
        if(contentNode != null)
        {
            this.getTrueWidthProperty().unbind();
            this.getTrueHeightProperty().unbind();
            this.removeChild(contentNode);
        }

        this.contentNode = node;
        this.addChild(node);
        RelativeBindingHelper.bindToPos(node, this, this.getScrollXProperty(), this.getScrollYProperty());

        this.getTrueWidthProperty().bind(node.getWidthProperty());
        this.getTrueHeightProperty().bind(node.getHeightProperty());
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiScrollableSkin<>(this, new GuiScrollableBehavior<>(this));
    }
}