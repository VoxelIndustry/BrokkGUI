package net.voxelindustry.brokkgui.style;

import net.voxelindustry.brokkgui.style.tree.StyleList;
import net.voxelindustry.hermod.IEventEmitter;

import java.util.function.Supplier;

@Deprecated
public interface ICascadeStyleable extends IStyleable, IEventEmitter
{
    ICascadeStyleable getParent();

    void setParent(ICascadeStyleable styleable);

    void setStyleListSupplier(Supplier<StyleList> treeSupplier);
}
