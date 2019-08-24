package net.voxelindustry.brokkgui.style.tree;

import net.voxelindustry.brokkgui.style.selector.IStyleSelector;

import java.util.ArrayList;
import java.util.List;

public class StyleEntry
{
    private IStyleSelector  selector;
    private List<StyleRule> rules;

    public StyleEntry(IStyleSelector selector)
    {
        this.selector = selector;
        this.rules = new ArrayList<>();
    }

    public void mergeRules(List<StyleRule> rules)
    {
        rules.forEach(rule ->
        {
            if (this.rules.stream().noneMatch(rule2 -> rule2.getRuleIdentifier().equals(rule.getRuleIdentifier())))
                this.rule(rule);
        });
    }

    public StyleEntry rule(StyleRule rule)
    {
        if (!this.rules.contains(rule))
            this.rules.add(rule);
        return this;
    }

    public IStyleSelector getSelector()
    {
        return selector;
    }

    public List<StyleRule> getRules()
    {
        return rules;
    }

    @Override
    public String toString()
    {
        return "{selector=" + selector + ", rules=" + rules + '}';
    }
}
