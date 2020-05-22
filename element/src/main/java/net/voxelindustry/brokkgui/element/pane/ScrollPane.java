package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.behavior.GuiScrollableBehavior;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiScrollableBase;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.skin.GuiScrollableSkin;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

/**
 * @author Ourten 9 oct. 2016
 */
public class ScrollPane extends GuiScrollableBase
{
    private GuiElement contentNode;

    public ScrollPane(GuiElement node)
    {
        if (node != null)
            this.setChild(node);
    }

    public ScrollPane()
    {
        this(null);
    }

    @Override
    public String type()
    {
        return "scrollpane";
    }

    public void setChild(GuiElement node)
    {
        if (contentNode != null)
        {
            this.getTrueWidthProperty().unbind();
            this.getTrueHeightProperty().unbind();
            this.removeChild(contentNode);
        }

        this.contentNode = node;
        this.addChild(node);
        RelativeBindingHelper.bindToPos(node.transform(), transform(), this.getScrollXProperty(), this.getScrollYProperty());

        this.getTrueWidthProperty().bind(node.transform().widthProperty());
        this.getTrueHeightProperty().bind(node.transform().heightProperty());
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiScrollableSkin<>(this, new GuiScrollableBehavior<>(this));
    }
}