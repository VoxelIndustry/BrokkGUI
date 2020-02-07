package net.voxelindustry.brokkgui.sprite;

import java.util.Map;
import java.util.Objects;

public class RandomSpriteRotation
{
    private Map<SpriteRotation, Float> chanceByRotation;

    public RandomSpriteRotation(Map<SpriteRotation, Float> chanceByRotation)
    {
        this.chanceByRotation = chanceByRotation;
    }

    public Map<SpriteRotation, Float> getChanceByRotation()
    {
        return chanceByRotation;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RandomSpriteRotation that = (RandomSpriteRotation) o;
        return Objects.equals(chanceByRotation, that.chanceByRotation);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(chanceByRotation);
    }

    @Override
    public String toString()
    {
        return "RandomSpriteRotation{" +
                "chanceByRotation=" + chanceByRotation +
                '}';
    }
}
