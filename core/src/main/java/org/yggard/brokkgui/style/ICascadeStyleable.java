package org.yggard.brokkgui.style;

import org.yggard.brokkgui.style.tree.StyleList;

import java.util.function.Supplier;

public interface ICascadeStyleable extends IStyleable
{
    ICascadeStyleable getParent();

    void setStyleTree(Supplier<StyleList> treeSupplier);
}
