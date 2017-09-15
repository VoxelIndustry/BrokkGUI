package org.yggard.brokkgui.style.tree;

import org.yggard.brokkgui.style.StyleHolder;

import java.util.*;

public class StyleList
{
    private List<StyleEntry> styleList;
    private StyleEntry       wildcard;

    public StyleList()
    {
        this.wildcard = new StyleEntry(new StyleSelector().addWildcard());
        this.styleList = new ArrayList<>();
        this.styleList.add(this.wildcard);
    }

    public void addEntry(StyleSelector selectors, Set<StyleRule> rules)
    {
        StyleEntry lastAdded;

        Optional<StyleEntry> match = this.styleList.stream().filter(styleEntry ->
                styleEntry.getSelector().match(selectors)).findFirst();

        if (!match.isPresent())
        {
            StyleEntry newEntry = new StyleEntry(selectors);
            this.styleList.add(newEntry);
            lastAdded = newEntry;
        }
        else
            lastAdded = match.get();
        lastAdded.mergeRules(rules);
    }

    public List<StyleEntry> getInternalStyleList()
    {
        return this.styleList;
    }

    public StyleEntry getWildcard()
    {
        return this.wildcard;
    }

    void clear()
    {
        this.styleList.clear();
        this.styleList.add(wildcard);
        this.wildcard.getRules().clear();
    }

    boolean isEmpty()
    {
        return this.styleList.size() == 1 && this.wildcard.getRules().isEmpty();
    }

    public Set<StyleEntry> getEntries(StyleHolder styleHolder)
    {
        Set<StyleEntry> entries = new HashSet<>();

        entries.add(this.wildcard);
        this.styleList.forEach(entry ->
        {
            if (entry.getSelector().match(styleHolder))
                entries.add(entry);
        });
        return entries;
    }
}
