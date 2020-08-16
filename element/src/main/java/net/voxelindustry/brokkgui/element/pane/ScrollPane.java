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
            setChild(node);
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
            getTrueWidthProperty().unbind();
            getTrueHeightProperty().unbind();
            removeChild(contentNode);
        }

        contentNode = node;
        addChild(node);
        RelativeBindingHelper.bindToPos(node.transform(), transform(), getScrollXProperty(), getScrollYProperty());

        getTrueWidthProperty().bindProperty(node.transform().widthProperty());
        getTrueHeightProperty().bindProperty(node.transform().heightProperty());
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiScrollableSkin<>(this, new GuiScrollableBehavior<>(this));
    }
}