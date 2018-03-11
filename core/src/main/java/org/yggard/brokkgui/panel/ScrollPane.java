package org.yggard.brokkgui.panel;

import org.yggard.brokkgui.behavior.GuiScrollableBehavior;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.control.GuiScrollableBase;
import org.yggard.brokkgui.data.RelativeBindingHelper;
import org.yggard.brokkgui.skin.GuiScrollableSkin;
import org.yggard.brokkgui.skin.GuiSkinBase;

/**
 * @author Ourten 9 oct. 2016
 */
public class ScrollPane extends GuiScrollableBase
{
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
        if (!this.getChildrens().isEmpty())
        {
            this.getChildrensProperty().get(0).getxPosProperty().unbind();
            this.getChildrensProperty().get(0).getyPosProperty().unbind();
            this.getChildrensProperty().get(0).setFather(null);
            this.getChildrensProperty().clear();

            this.getTrueWidthProperty().unbind();
            this.getTrueHeightProperty().unbind();
        }

        this.getChildrensProperty().add(node);

        RelativeBindingHelper.bindToPos(node, this, this.getScrollXProperty(), this.getScrollYProperty());

        node.setFather(this);
        this.getTrueWidthProperty().bind(node.getWidthProperty());
        this.getTrueHeightProperty().bind(node.getHeightProperty());
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiScrollableSkin<>(this, new GuiScrollableBehavior<>(this));
    }
}