package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;

public class BackgroundRepeatStyleTranslator implements IStyleTranslator<SpriteRepeat>
{
    @Override
    public SpriteRepeat decode(String style)
    {
        switch (style)
        {
            case "none":
                return SpriteRepeat.NONE;
            case "repeat-x":
                return SpriteRepeat.REPEAT_X;
            case "repeat-y":
                return SpriteRepeat.REPEAT_Y;
            case "repeat":
                return SpriteRepeat.REPEAT_BOTH;
        }
        throw new IllegalArgumentException("Cannot decode BackgroundRepeat value of [" + style + "]");
    }

    @Override
    public String encode(SpriteRepeat value, boolean prettyPrint)
    {
        switch (value)
        {
            case NONE:
                return "none";
            case REPEAT_X:
                return "repeat-x";
            case REPEAT_Y:
                return "repeat-y";
            case REPEAT_BOTH:
                return "repeat";
        }
        throw new RuntimeException("Unknown value given to BackgroundRepeatStyleTranslator! " + value.name());
    }

    @Override
    public int validate(String style)
    {
        switch (style)
        {
            case "none":
                return 4;
            case "repeat-x":
            case "repeat-y":
                return 8;
            case "repeat":
                return 6;
        }
        return 0;
    }
}
