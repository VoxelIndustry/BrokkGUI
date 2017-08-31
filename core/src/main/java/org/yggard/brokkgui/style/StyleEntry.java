package org.yggard.brokkgui.style;

import java.util.ArrayList;
import java.util.List;

public class StyleEntry
{
    private StyleSelector selector;
    private List<String>  rules;

    public StyleEntry(StyleSelector selector)
    {
        this.selector = selector;
        this.rules = new ArrayList<>();
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

    public List<String> getRules()
    {
        return rules;
    }

    @Override
    public String toString()
    {
        return "{selector=" + selector + ", rules=" + rules + '}';
    }
}
