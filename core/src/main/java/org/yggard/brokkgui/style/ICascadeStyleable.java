package org.yggard.brokkgui.style;

import java.util.function.Supplier;

public interface ICascadeStyleable extends IStyleable
{
    ICascadeStyleable getParent();

    void setStyleTree(Supplier<StyleTree> treeSupplier);
}
