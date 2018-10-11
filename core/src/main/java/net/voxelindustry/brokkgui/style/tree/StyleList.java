package net.voxelindustry.brokkgui.style.tree;

import net.voxelindustry.brokkgui.style.StyleHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public StyleList(StyleList original)
    {
        this();
        this.merge(original);
    }

    public StyleList merge(StyleList src)
    {
        src.getInternalStyleList().forEach(entry -> this.addEntry(entry.getSelector(), entry.getRules()));
        return this;
    }

    public void addEntry(IStyleSelector selectors, List<StyleRule> rules)
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

    public List<StyleEntry> getEntries(StyleHolder styleHolder)
    {
        List<StyleEntry> entries = new ArrayList<>();

        entries.add(this.wildcard);
        this.styleList.forEach(entry ->
        {
            if (entry.getSelector().match(styleHolder))
                entries.add(entry);
        });
        return entries;
    }
}
