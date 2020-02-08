package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.sprite.RandomSpriteRotation;
import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.sprite.Texture;

import java.util.Objects;

public class ShapeSpriteRotationCache
{
    private Float lastWidth;
    private Float lastHeight;

    private Texture      lastTexture;
    private SpriteRepeat lastRepeat;

    private RandomSpriteRotation lastRandomSpriteRotation;

    public float getLastWidth()
    {
        return lastWidth;
    }

    public ShapeSpriteRotationCache setLastWidth(float lastWidth)
    {
        this.lastWidth = lastWidth;
        return this;
    }

    public float getLastHeight()
    {
        return lastHeight;
    }

    public ShapeSpriteRotationCache setLastHeight(float lastHeight)
    {
        this.lastHeight = lastHeight;
        return this;
    }

    public Texture getLastTexture()
    {
        return lastTexture;
    }

    public ShapeSpriteRotationCache setLastTexture(Texture lastTexture)
    {
        this.lastTexture = lastTexture;
        return this;
    }

    public SpriteRepeat getLastRepeat()
    {
        return lastRepeat;
    }

    public ShapeSpriteRotationCache setLastRepeat(SpriteRepeat lastRepeat)
    {
        this.lastRepeat = lastRepeat;
        return this;
    }

    public RandomSpriteRotation getLastRandomSpriteRotation()
    {
        return lastRandomSpriteRotation;
    }

    public ShapeSpriteRotationCache setLastRandomSpriteRotation(RandomSpriteRotation lastRandomSpriteRotation)
    {
        this.lastRandomSpriteRotation = lastRandomSpriteRotation;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShapeSpriteRotationCache that = (ShapeSpriteRotationCache) o;
        return Float.compare(that.lastWidth, lastWidth) == 0 &&
                Float.compare(that.lastHeight, lastHeight) == 0 &&
                Objects.equals(lastTexture, that.lastTexture) &&
                lastRepeat == that.lastRepeat &&
                Objects.equals(lastRandomSpriteRotation, that.lastRandomSpriteRotation);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(lastWidth, lastHeight, lastTexture, lastRepeat, lastRandomSpriteRotation);
    }

    @Override
    public String toString()
    {
        return "ShapeSpriteRotationCache{" +
                "lastWidth=" + lastWidth +
                ", lastHeight=" + lastHeight +
                ", lastTexture=" + lastTexture +
                ", lastRepeat=" + lastRepeat +
                ", randomSpriteRotation=" + lastRandomSpriteRotation +
                '}';
    }

    public boolean matches(RandomSpriteRotation spriteRotation, Texture texture, SpriteRepeat repeat, float width, float height)
    {
        return Objects.equals(this.lastRandomSpriteRotation, spriteRotation) &&
                Objects.equals(this.lastTexture, texture) &&
                Objects.equals(this.lastRepeat, repeat) &&
                Objects.equals(this.lastWidth, width) &&
                Objects.equals(this.lastHeight, height);
    }
}
