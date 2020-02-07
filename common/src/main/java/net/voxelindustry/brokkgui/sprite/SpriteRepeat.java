package net.voxelindustry.brokkgui.sprite;

import static java.lang.Math.ceil;

public enum SpriteRepeat
{
    NONE,
    REPEAT_X,
    REPEAT_Y,
    REPEAT_BOTH;

    public int getRepeatCount(Texture texture, float width, float height)
    {
        switch (this)
        {
            case NONE:
                return 1;
            case REPEAT_X:
                return (int) ceil(width / texture.getPixelWidth());
            case REPEAT_Y:
                return (int) ceil(height / texture.getPixelHeight());
            case REPEAT_BOTH:
                return (int) (ceil(width / texture.getPixelWidth()) * ceil(height / texture.getPixelHeight()));
        }
        return 0;
    }
}
