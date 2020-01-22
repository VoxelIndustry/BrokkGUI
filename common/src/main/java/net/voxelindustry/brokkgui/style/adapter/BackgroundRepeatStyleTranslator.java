package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.paint.BackgroundRepeat;

public class BackgroundRepeatStyleTranslator implements IStyleDecoder<BackgroundRepeat>, IStyleEncoder<BackgroundRepeat>, IStyleValidator<BackgroundRepeat>
{
    @Override
    public BackgroundRepeat decode(String style)
    {
        switch (style)
        {
            case "none":
                return BackgroundRepeat.NONE;
            case "repeat-x":
                return BackgroundRepeat.REPEAT_X;
            case "repeat-y":
                return BackgroundRepeat.REPEAT_Y;
            case "repeat":
                return BackgroundRepeat.REPEAT_BOTH;
        }
        throw new IllegalArgumentException("Cannot decode BackgroundRepeat value of [" + style + "]");
    }

    @Override
    public String encode(BackgroundRepeat value, boolean prettyPrint)
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
