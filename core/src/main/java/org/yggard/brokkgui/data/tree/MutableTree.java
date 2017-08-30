package org.yggard.brokkgui.data.tree;

public interface MutableTree<N> extends Tree<N>
{
    boolean add(N parent, N node);

    boolean remove(N node, boolean cascade);

    default void clear()
    {
        this.getRoots().forEach(node -> this.remove(node, true));
    }
}
