package org.yggard.brokkgui.style;

import org.yggard.brokkgui.data.tree.MappedTree;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StyleTree
{
    private MappedTree<StyleEntry> internalTree;
    private StyleEntry             wildcard;

    public StyleTree()
    {
        internalTree = new MappedTree<>();

        this.wildcard = new StyleEntry(new StyleSelector().addWildcard());
        this.internalTree.add(null, this.wildcard);
    }

    public StyleTree agglomerate(StyleTree tree)
    {
        return new StyleTree();
    }

    public void addEntry(StyleSelector selectors, List<String> rules)
    {
        StyleEntry lastAdded = this.wildcard;
        for (Map.Entry<StyleSelector.StyleSelectorType, List<String>> entry : selectors.getSelectors().entrySet())
        {
            for (String selector : entry.getValue())
            {
                Optional<StyleEntry> match = internalTree.getChildren(lastAdded).stream().filter(styleEntry ->
                        styleEntry.getSelector().getSelectors().containsKey(entry.getKey()) && styleEntry.getSelector()
                                .getSelectors().get(entry.getKey()).contains(selector)).findFirst();

                if (!match.isPresent())
                {
                    StyleEntry newEntry = new StyleEntry(new StyleSelector().addSelector(entry.getKey(), selector));

                    StyleEntry previous = lastAdded;
                    while (previous != this.wildcard)
                    {
                        newEntry.getSelector().getSelectors().putAll(previous.getSelector().getSelectors());
                        previous = this.internalTree.getParent(previous);
                    }

                    this.internalTree.add(lastAdded, newEntry);
                    lastAdded = newEntry;
                }
                else
                    lastAdded = match.get();
            }
        }

        lastAdded.getRules().addAll(rules);
    }

    MappedTree<StyleEntry> getInternalTree()
    {
        return this.internalTree;
    }

    StyleEntry getWildcard()
    {
        return this.wildcard;
    }

    void clear()
    {
        this.internalTree.clear();
        this.internalTree.add(null, wildcard);
        this.wildcard.getRules().clear();
    }

    boolean isEmpty()
    {
        return this.internalTree.size() == 1 && this.wildcard.getRules().isEmpty();
    }
}
