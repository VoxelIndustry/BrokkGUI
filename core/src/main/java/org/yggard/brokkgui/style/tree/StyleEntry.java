package org.yggard.brokkgui.style.tree;

import java.util.HashSet;
import java.util.Set;

public class StyleEntry
{
    private StyleSelector selector;
    private Set<StyleRule>   rules;

    public StyleEntry(StyleSelector selector)
    {
        this.selector = selector;
        this.rules = new HashSet<>();
    }

    public StyleEntry rule(StyleRule rule)
    {
        this.rules.add(rule);
        return this;
    }

    public StyleSelector getSelector()
    {
        return selector;
    }

    public Set<StyleRule> getRules()
    {
        return rules;
    }

    @Override
    public String toString()
    {
        return "{selector=" + selector + ", rules=" + rules + '}';
    }
}
