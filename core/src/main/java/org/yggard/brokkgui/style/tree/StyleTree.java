package org.yggard.brokkgui.style.tree;

import org.yggard.brokkgui.data.tree.MappedTree;
import org.yggard.brokkgui.style.StyleHolder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    public StyleTree merge(StyleTree tree)
    {
        tree.getInternalTree().getNodeList().forEach(entry -> this.addEntry(entry.getSelector(), entry.getRules()));
        return this;
    }

    // TODO: Rules erasure and priority
    public void addEntry(StyleSelector selectors, Set<StyleRule> rules)
    {
        StyleEntry lastAdded = this.wildcard;
        Optional<StyleEntry> match = internalTree.getChildren(lastAdded).stream()
                .filter(styleEntry -> styleEntry.getSelector().getSelector().equals(selectors.getSelector())
                        && styleEntry.getSelector().getType().equals(selectors.getType())).findFirst();

        if (!match.isPresent())
        {
            StyleEntry newEntry = new StyleEntry(new StyleSelector().setSelector(selectors.getType(), selectors
                    .getSelector()));

            if (lastAdded != this.wildcard)
                newEntry.getSelector().setInheritedSpecificity(lastAdded.getSelector().getSpecificity());

            this.internalTree.add(lastAdded, newEntry);
            lastAdded = newEntry;
        }
        else
            lastAdded = match.get();

        lastAdded.mergeRules(rules);
    }

    public MappedTree<StyleEntry> getInternalTree()
    {
        return this.internalTree;
    }

    public StyleEntry getWildcard()
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

    public Set<StyleEntry> getEntries(StyleHolder styleHolder)
    {
        Set<StyleEntry> entries = new HashSet<>();

        entries.add(this.wildcard);
        this.internalTree.getChildren(this.wildcard).forEach(entry ->
        {
            if (entry.getSelector().match(styleHolder))
                entries.add(entry);
            if (styleHolder.getParent().isPresent()
                    && entry.getSelector().match(styleHolder.getParent().getValue().getStyle()))
            {
                this.internalTree.getChildren(entry).forEach(childEntry ->
                {
                    if (childEntry.getSelector().match(styleHolder))
                        entries.add(childEntry);
                });
            }
        });

        return entries;
    }
}
