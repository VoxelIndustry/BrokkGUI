package net.voxelindustry.brokkgui.style.tree;

import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.selector.IStyleSelector;
import net.voxelindustry.brokkgui.style.selector.StyleSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StyleList
{
    private List<StyleEntry> styleEntries;
    private StyleEntry       wildcard;

    public StyleList()
    {
        this.wildcard = new StyleEntry(new StyleSelector().addWildcard());
        this.styleEntries = new ArrayList<>();
        this.styleEntries.add(this.wildcard);
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

        Optional<StyleEntry> match = this.styleEntries.stream().filter(styleEntry ->
                styleEntry.getSelector().match(selectors)).findFirst();

        if (!match.isPresent())
        {
            StyleEntry newEntry = new StyleEntry(selectors);
            this.styleEntries.add(newEntry);
            lastAdded = newEntry;
        }
        else
            lastAdded = match.get();
        lastAdded.mergeRules(rules);
    }

    public List<StyleEntry> getInternalStyleList()
    {
        return this.styleEntries;
    }

    public StyleEntry getWildcard()
    {
        return this.wildcard;
    }

    void clear()
    {
        this.styleEntries.clear();
        this.styleEntries.add(wildcard);
        this.wildcard.getRules().clear();
    }

    boolean isEmpty()
    {
        return this.styleEntries.size() == 1 && this.wildcard.getRules().isEmpty();
    }

    public List<StyleEntry> getEntriesMatching(StyleComponent styleComponent)
    {
        List<StyleEntry> entries = new ArrayList<>();

        entries.add(this.wildcard);
        this.styleEntries.forEach(entry ->
        {
            if (entry.getSelector().match(styleComponent))
                entries.add(entry);
        });
        return entries;
    }
}
