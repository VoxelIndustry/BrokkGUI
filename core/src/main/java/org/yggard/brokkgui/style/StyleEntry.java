package org.yggard.brokkgui.style;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StyleEntry
{
    private StyleSelector selector;
    private Set<String>   rules;

    public StyleEntry(StyleSelector selector)
    {
        this.selector = selector;
        this.rules = new HashSet<>();
    }

    public StyleEntry rule(String rule)
    {
        this.rules.add(rule);
        return this;
    }

    public StyleSelector getSelector()
    {
        return selector;
    }

    public Set<String> getRules()
    {
        return rules;
    }

    @Override
    public String toString()
    {
        return "{selector=" + selector + ", rules=" + rules + '}';
    }
}
