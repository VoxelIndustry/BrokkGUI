package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.data.RectBox;

public class RectBoxTranslator implements IStyleDecoder<RectBox>, IStyleEncoder<RectBox>, IStyleValidator<RectBox>
{
    @Override
    public String encode(RectBox value, boolean prettyPrint)
    {
        if (prettyPrint)
            return "top: " + value.getTop() + " right: " + value.getRight() + " bottom: " + value.getBottom() + " " +
                    "left: " + value.getLeft();
        return value.getTop() + " " + value.getRight() + " " + value.getBottom() + " " + value.getLeft();
    }

    @Override
    public RectBox decode(String style)
    {
        float top = 0;
        float right = 0;
        float bottom = 0;
        float left = 0;

        String[] parts = style.split(" ");

        switch (parts.length)
        {
            case 1:
                top = right = bottom = left = StyleTranslator.getInstance().decode(parts[0], Float.class);
                break;
            case 2:
                top = bottom = StyleTranslator.getInstance().decode(parts[0], Float.class);
                right = left = StyleTranslator.getInstance().decode(parts[1], Float.class);
                break;
            case 3:
                top = StyleTranslator.getInstance().decode(parts[0], Float.class);
                right = left = StyleTranslator.getInstance().decode(parts[1], Float.class);
                bottom = StyleTranslator.getInstance().decode(parts[2], Float.class);
                break;
            case 4:
                top = StyleTranslator.getInstance().decode(parts[0], Float.class);
                right = StyleTranslator.getInstance().decode(parts[1], Float.class);
                bottom = StyleTranslator.getInstance().decode(parts[2], Float.class);
                left = StyleTranslator.getInstance().decode(parts[3], Float.class);
                break;
            default:
                throw new RuntimeException("Cannot parse a RectBox from more than 4 values! style=" + style);
        }
        return RectBox.build().top(top).right(right).bottom(bottom).left(left).create();
    }

    @Override
    public int validate(String style)
    {
        int totalLength = StyleTranslator.getInstance().validate(style, Float.class);

        if (totalLength == 0)
            return 0;

        String current = style.substring(totalLength);

        for (int index = 0; index < 3; index++)
        {
            String trimmed = current.trim();
            int length = StyleTranslator.getInstance().validate(trimmed, Float.class) + current.indexOf(trimmed);

            if (length == 0)
                break;

            totalLength += length;
            current = current.substring(length);
        }
        return totalLength;
    }
}
