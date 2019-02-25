package net.voxelindustry.brokkgui.style.tree;

import javax.annotation.Nonnull;

public class StyleRule
{
    private final String ruleIdentifier;
    private final String ruleValue;

    public StyleRule(@Nonnull String ruleIdentifier, @Nonnull String ruleValue)
    {
        this.ruleIdentifier = ruleIdentifier;
        this.ruleValue = ruleValue;
    }

    public String getRuleIdentifier()
    {
        return ruleIdentifier;
    }

    public String getRuleValue()
    {
        return ruleValue;
    }

    @Override
    public String toString()
    {
        return "{ruleIdentifier='" + ruleIdentifier + '\'' + ", ruleValue='" + ruleValue + '\'' + '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StyleRule styleRule = (StyleRule) o;

        return getRuleIdentifier().equals(styleRule.getRuleIdentifier()) && getRuleValue().equals(styleRule
                .getRuleValue());
    }

    @Override
    public int hashCode()
    {
        int result = getRuleIdentifier().hashCode();
        result = 31 * result + getRuleValue().hashCode();
        return result;
    }
}
