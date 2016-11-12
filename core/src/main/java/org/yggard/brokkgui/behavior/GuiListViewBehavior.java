package org.yggard.brokkgui.behavior;

import org.yggard.brokkgui.element.GuiListView;

public class GuiListViewBehavior<T> extends GuiBehaviorBase<GuiListView<T>>
{
    public GuiListViewBehavior(final GuiListView<T> model)
    {
        super(model);
    }

    /**
     * Used to select cell after the skin forward the ClickEvent to the
     * behavior.
     *
     * @param cellIndex
     */
    public void selectCell(final int cellIndex)
    {
        this.getModel().setSelectedCellIndex(cellIndex);
    }
}