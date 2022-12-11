package net.voxelindustry.brokkgui.sprite;

import net.voxelindustry.brokkgui.data.StyleValue;

import static java.lang.Math.ceil;

public enum SpriteRepeat implements StyleValue
{
    NONE("none"),
    REPEAT_X("repeat-x"),
    REPEAT_Y("repeat-y"),
    REPEAT_BOTH("repeat");

    private final String value;

    SpriteRepeat(String value)
    {
        this.value = value;
    }

    @Override
    public String value()
    {
        return value;
    }

    public int getRepeatCount(Texture texture, float width, float height)
    {
        return switch (this)
                {
                    case NONE -> 1;
                    case REPEAT_X -> (int) ceil(width / texture.getPixelWidth());
                    case REPEAT_Y -> (int) ceil(height / texture.getPixelHeight());
                    case REPEAT_BOTH ->
                            (int) (ceil(width / texture.getPixelWidth()) * ceil(height / texture.getPixelHeight()));
                };
    }
}
