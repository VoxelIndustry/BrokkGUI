package net.voxelindustry.brokkgui.skin;

import net.voxelindustry.brokkgui.behavior.GuiListViewBehavior;
import net.voxelindustry.brokkgui.element.GuiListView;

public class GuiListViewSkin<T> extends GuiScrollableSkin<GuiListView<T>, GuiListViewBehavior<T>>
{
    public GuiListViewSkin(final GuiListView<T> model, final GuiListViewBehavior<T> behaviour)
    {
        super(model, behaviour);
    }
}