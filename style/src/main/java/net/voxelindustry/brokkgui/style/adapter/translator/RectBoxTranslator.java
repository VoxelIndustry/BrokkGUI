package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;
import net.voxelindustry.brokkgui.style.adapter.StyleTranslator;

import java.util.concurrent.atomic.AtomicInteger;

public class RectBoxTranslator implements IStyleTranslator<RectBox>
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
    public RectBox decode(String style, AtomicInteger consumedLength)
    {
        float top;
        float right;
        float bottom;
        float left;

        String[] parts = style.split(" ", 4);

        switch (parts.length)
        {
            case 1 -> top = right = bottom = left = StyleTranslator.getInstance().decode(parts[0], Float.class);
            case 2 ->
            {
                top = bottom = StyleTranslator.getInstance().decode(parts[0], Float.class);
                right = left = StyleTranslator.getInstance().decode(parts[1], Float.class);
            }
            case 3 ->
            {
                top = StyleTranslator.getInstance().decode(parts[0], Float.class);
                right = left = StyleTranslator.getInstance().decode(parts[1], Float.class);
                bottom = StyleTranslator.getInstance().decode(parts[2], Float.class);
            }
            case 4 ->
            {
                top = StyleTranslator.getInstance().decode(parts[0], Float.class);
                right = StyleTranslator.getInstance().decode(parts[1], Float.class);
                bottom = StyleTranslator.getInstance().decode(parts[2], Float.class);
                left = StyleTranslator.getInstance().decode(parts[3], Float.class);
            }
            default ->
                    throw new UnsupportedOperationException("RectBox style translator does not support more than 4 elements. value=" + style);
        }
        return RectBox.build().top(top).right(right).bottom(bottom).left(left).create();
    }
}
