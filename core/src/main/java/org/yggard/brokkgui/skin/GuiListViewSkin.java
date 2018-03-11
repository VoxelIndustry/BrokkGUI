package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.behavior.GuiListViewBehavior;
import org.yggard.brokkgui.element.GuiListView;

public class GuiListViewSkin<T> extends GuiScrollableSkin<GuiListView<T>, GuiListViewBehavior<T>>
{
    public GuiListViewSkin(final GuiListView<T> model, final GuiListViewBehavior<T> behaviour)
    {
        super(model, behaviour);
    }
}