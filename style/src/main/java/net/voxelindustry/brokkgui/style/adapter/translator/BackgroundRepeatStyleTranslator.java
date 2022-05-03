package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.sprite.SpriteRepeat;
import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;

public class BackgroundRepeatStyleTranslator implements IStyleTranslator<SpriteRepeat>
{
    @Override
    public SpriteRepeat decode(String style)
    {
        return switch (style)
                {
                    case "none" -> SpriteRepeat.NONE;
                    case "repeat-x" -> SpriteRepeat.REPEAT_X;
                    case "repeat-y" -> SpriteRepeat.REPEAT_Y;
                    case "repeat" -> SpriteRepeat.REPEAT_BOTH;
                    default ->
                            throw new IllegalArgumentException("Cannot decode BackgroundRepeat value of [" + style + "]");
                };
    }

    @Override
    public String encode(SpriteRepeat value, boolean prettyPrint)
    {
        return switch (value)
                {
                    case NONE -> "none";
                    case REPEAT_X -> "repeat-x";
                    case REPEAT_Y -> "repeat-y";
                    case REPEAT_BOTH -> "repeat";
                };
    }

    @Override
    public int validate(String style)
    {
        return switch (style)
                {
                    case "none" -> 4;
                    case "repeat-x", "repeat-y" -> 8;
                    case "repeat" -> 6;
                    default -> 0;
                };
    }
}
