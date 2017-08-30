package org.yggard.brokkgui.style;

import org.yggard.brokkgui.data.tree.MappedTree;

public class StyleTree
{
    MappedTree<StyleEntry> internalTree;

    public StyleTree()
    {
        internalTree = new MappedTree<>();
    }

    public StyleTree agglomerate(StyleTree tree)
    {
        return new StyleTree();
    }
}
