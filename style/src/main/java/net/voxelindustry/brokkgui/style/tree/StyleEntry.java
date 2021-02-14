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
        rules = new ArrayList<>();
    }

    public void mergeRules(List<StyleRule> rules)
    {
        for (StyleRule rule : rules)
            addOrReplaceRule(rule);
    }

    private void addOrReplaceRule(StyleRule rule)
    {
        StyleRule previousRule = null;
        for (StyleRule candidate : rules)
        {
            if (candidate.getRuleIdentifier().equals(rule.getRuleIdentifier()))
                previousRule = candidate;
        }

        if (previousRule != null)
            rules.remove(previousRule);
        rule(rule);
    }

    public void rule(StyleRule rule)
    {
        if (!rules.contains(rule))
            rules.add(rule);
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
