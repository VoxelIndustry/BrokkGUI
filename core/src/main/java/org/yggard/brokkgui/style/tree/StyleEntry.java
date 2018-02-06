package org.yggard.brokkgui.style.tree;

import java.util.HashSet;
import java.util.Set;

public class StyleEntry
{
    private IStyleSelector  selector;
    private Set<StyleRule> rules;

    public StyleEntry(IStyleSelector selector)
    {
        this.selector = selector;
        this.rules = new HashSet<>();
    }

    public void mergeRules(Set<StyleRule> rules)
    {
        rules.forEach(rule ->
        {
            if (this.rules.stream().noneMatch(rule2 -> rule2.getRuleIdentifier().equals(rule.getRuleIdentifier())))
                this.rules.add(rule);
        });
    }

    public StyleEntry rule(StyleRule rule)
    {
        this.rules.add(rule);
        return this;
    }

    public IStyleSelector getSelector()
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
