package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;
import org.apache.commons.lang3.ArrayUtils;

public class RectAlignmentStyleTranslator implements IStyleTranslator<RectAlignment>
{
    private final String[] allowedValues = new String[]{
            "left",
            "left center",
            "right",
            "right center",
            "center",
            "center center",
            "left up",
            "right up",
            "up",
            "left down",
            "right down",
            "down",
            "center down",
            "center up"};

    @Override
    public RectAlignment decode(String style)
    {
        switch (style.toLowerCase())
        {
            case "left":
            case "left center":
                return RectAlignment.LEFT_CENTER;
            case "right":
            case "right center":
                return RectAlignment.RIGHT_CENTER;
            case "center":
            case "center center":
                return RectAlignment.MIDDLE_CENTER;
            case "left up":
                return RectAlignment.LEFT_UP;
            case "right up":
                return RectAlignment.RIGHT_UP;
            case "left down":
                return RectAlignment.LEFT_DOWN;
            case "right down":
                return RectAlignment.RIGHT_DOWN;
            case "down":
            case "center down":
                return RectAlignment.MIDDLE_DOWN;
            case "up":
            case "center up":
                return RectAlignment.MIDDLE_UP;
        }
        return RectAlignment.MIDDLE_CENTER;
    }

    @Override
    public String encode(RectAlignment value, boolean prettyPrint)
    {
        switch (value)
        {
            case LEFT_CENTER:
                return "left";
            case RIGHT_CENTER:
                return "right";
            case MIDDLE_CENTER:
                return "center";
            case LEFT_UP:
                return "left up";
            case RIGHT_UP:
                return "right up";
            case MIDDLE_UP:
                return "up";
            case LEFT_DOWN:
                return "left down";
            case MIDDLE_DOWN:
                return "down";
            case RIGHT_DOWN:
                return "right down";
        }
        return "none";
    }

    @Override
    public int validate(String style)
    {
        if (ArrayUtils.contains(allowedValues, style.toLowerCase()))
            return style.length();
        return 0;
    }
}
