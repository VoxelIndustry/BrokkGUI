package org.yggard.brokkgui.style;

import org.yggard.brokkgui.style.tree.StyleTree;

import java.util.function.Supplier;

public interface ICascadeStyleable extends IStyleable
{
    ICascadeStyleable getParent();

    void setStyleTree(Supplier<StyleTree> treeSupplier);
}
