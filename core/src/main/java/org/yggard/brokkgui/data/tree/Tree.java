package org.yggard.brokkgui.data.tree;

import java.util.List;

public interface Tree<N>
{
    List<N> getRoots();

    N getParent(N node);

    List<N> getChildren(N node);

    int size();

    default boolean isEmpty()
    {
        return this.getRoots().isEmpty();
    }
}
