package net.voxelindustry.brokkgui.style.shorthand.paint;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.paint.border.BorderPaint;
import net.voxelindustry.brokkgui.paint.border.BorderPaintData;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.style.StyleProperty;
import net.voxelindustry.brokkgui.style.adapter.StyleTranslator;
import net.voxelindustry.brokkgui.style.shorthand.MultiStyleProperty;
import net.voxelindustry.brokkgui.style.specificity.StylePriority;
import net.voxelindustry.brokkgui.style.specificity.StyleSource;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

public class BorderPaintStyleProperty extends StyleProperty<BorderPaint> implements BorderPaint, MultiStyleProperty
{
    private final EnumMap<BorderPaintStyleProperties, StylePriority> priorityByProperty = new EnumMap<>(BorderPaintStyleProperties.class);

    public BorderPaintStyleProperty(String name)
    {
        super(BorderPaint.EMPTY, name, BorderPaint.class);
    }

    @Override
    public void setStyleRaw(String propertyName, StyleSource source, int specificity, String rawValue, @Nullable AtomicInteger consumedLength)
    {
        var subPropertyKey = BorderPaintStyleProperties.fromValue(propertyName);

        if (subPropertyKey == BorderPaintStyleProperties.color)
        {
            var color = StyleTranslator.getInstance().decode(rawValue, Color.class);

            if (!priorityByProperty.containsKey(BorderPaintStyleProperties.leftColor) ||
                    priorityByProperty.get(BorderPaintStyleProperties.leftColor).compareTo(source, specificity) < 0)
            {
                colorLeft(color);
                priorityByProperty.put(BorderPaintStyleProperties.leftColor, new StylePriority(source, specificity));
            }
            if (!priorityByProperty.containsKey(BorderPaintStyleProperties.rightColor) ||
                    priorityByProperty.get(BorderPaintStyleProperties.rightColor).compareTo(source, specificity) < 0)
            {
                colorRight(color);
                priorityByProperty.put(BorderPaintStyleProperties.rightColor, new StylePriority(source, specificity));
            }
            if (!priorityByProperty.containsKey(BorderPaintStyleProperties.topColor) ||
                    priorityByProperty.get(BorderPaintStyleProperties.topColor).compareTo(source, specificity) < 0)
            {
                colorTop(color);
                priorityByProperty.put(BorderPaintStyleProperties.topColor, new StylePriority(source, specificity));
            }
            if (!priorityByProperty.containsKey(BorderPaintStyleProperties.bottomColor) ||
                    priorityByProperty.get(BorderPaintStyleProperties.bottomColor).compareTo(source, specificity) < 0)
            {
                colorBottom(color);
                priorityByProperty.put(BorderPaintStyleProperties.bottomColor, new StylePriority(source, specificity));
            }

            internalSetStyle(source, specificity, value);
            return;
        }
        else if (priorityByProperty.containsKey(subPropertyKey) &&
                priorityByProperty.get(subPropertyKey).compareTo(source, specificity) > 0)
            return;

        switch (subPropertyKey)
        {
            case leftColor -> colorLeft(StyleTranslator.getInstance().decode(rawValue, Color.class));
            case rightColor -> colorRight(StyleTranslator.getInstance().decode(rawValue, Color.class));
            case topColor -> colorTop(StyleTranslator.getInstance().decode(rawValue, Color.class));
            case bottomColor -> colorBottom(StyleTranslator.getInstance().decode(rawValue, Color.class));
            case box -> box(StyleTranslator.getInstance().decode(rawValue, RectBox.class));

            case imageSource -> image(StyleTranslator.getInstance().decode(rawValue, Texture.class));
            case imageWidth -> imageWidth(StyleTranslator.getInstance().decode(rawValue, RectBox.class));
            case imageOutset -> imageOutset(StyleTranslator.getInstance().decode(rawValue, RectBox.class));
            case imageSlice -> imageSlice(StyleTranslator.getInstance().decode(rawValue, RectBox.class));
            case imageFill -> imageFill(StyleTranslator.getInstance().decode(rawValue, Boolean.class));
        }
        priorityByProperty.put(subPropertyKey, new StylePriority(source, specificity));
        internalSetStyle(source, specificity, value);
    }

    @Override
    public void setToDefault()
    {
        priorityByProperty.keySet().removeIf(subProperty ->
        {
            var priority = priorityByProperty.get(subProperty);
            if (priority.source() != StyleSource.AUTHOR && priority.source() != StyleSource.USER_AGENT)
                return false;

            switch (subProperty)
            {
                case leftColor -> colorLeft(BorderPaint.EMPTY.colorLeft());
                case rightColor -> colorRight(BorderPaint.EMPTY.colorRight());
                case topColor -> colorTop(BorderPaint.EMPTY.colorTop());
                case bottomColor -> colorBottom(BorderPaint.EMPTY.colorBottom());
                case imageSource -> image(BorderPaint.EMPTY.image());
                case imageWidth -> imageWidth(BorderPaint.EMPTY.imageWidth());
                case imageOutset -> imageOutset(BorderPaint.EMPTY.imageOutset());
                case imageSlice -> imageSlice(BorderPaint.EMPTY.imageSlice());
                case imageFill -> imageFill(BorderPaint.EMPTY.imageFill());
                case box -> box(BorderPaint.EMPTY.box());
            }
            return true;
        });
    }

    @Override
    public Color colorLeft()
    {
        return value.colorLeft();
    }

    @Override
    public Color colorRight()
    {
        return value.colorRight();
    }

    @Override
    public Color colorTop()
    {
        return value.colorTop();
    }

    @Override
    public Color colorBottom()
    {
        return value.colorBottom();
    }

    @Override
    public boolean isColorUniform()
    {
        return value.isColorUniform();
    }

    @Override
    public Texture image()
    {
        return value.image();
    }

    @Override
    public RectBox imageSlice()
    {
        return value.imageSlice();
    }

    @Override
    public RectBox imageWidth()
    {
        return value.imageWidth();
    }

    @Override
    public RectBox imageOutset()
    {
        return value.imageOutset();
    }

    @Override
    public boolean imageFill()
    {
        return value.imageFill();
    }

    @Override
    public void colorLeft(Color color)
    {
        checkRemoveEmpty();
        value.colorLeft(color);
        priorityByProperty.put(BorderPaintStyleProperties.leftColor, StylePriority.CODE);
        invalidate();
    }

    @Override
    public void colorRight(Color color)
    {
        checkRemoveEmpty();
        value.colorRight(color);
        priorityByProperty.put(BorderPaintStyleProperties.rightColor, StylePriority.CODE);
        invalidate();
    }

    @Override
    public void colorTop(Color color)
    {
        checkRemoveEmpty();
        value.colorTop(color);
        priorityByProperty.put(BorderPaintStyleProperties.topColor, StylePriority.CODE);
        invalidate();
    }

    @Override
    public void colorBottom(Color color)
    {
        checkRemoveEmpty();
        value.colorBottom(color);
        priorityByProperty.put(BorderPaintStyleProperties.bottomColor, StylePriority.CODE);
        invalidate();
    }

    @Override
    public void image(Texture texture)
    {
        checkRemoveEmpty();
        value.image(texture);
        priorityByProperty.put(BorderPaintStyleProperties.imageSource, StylePriority.CODE);
        invalidate();
    }

    @Override
    public void imageSlice(RectBox box)
    {
        checkRemoveEmpty();
        value.imageSlice(box);
        priorityByProperty.put(BorderPaintStyleProperties.imageSlice, StylePriority.CODE);
        invalidate();
    }

    @Override
    public void imageWidth(RectBox box)
    {
        checkRemoveEmpty();
        value.imageWidth(box);
        priorityByProperty.put(BorderPaintStyleProperties.imageWidth, StylePriority.CODE);
        invalidate();
    }

    @Override
    public void imageOutset(RectBox box)
    {
        checkRemoveEmpty();
        value.imageOutset(box);
        priorityByProperty.put(BorderPaintStyleProperties.imageOutset, StylePriority.CODE);
        invalidate();
    }

    @Override
    public void imageFill(boolean doFill)
    {
        checkRemoveEmpty();
        value.imageFill(doFill);
        priorityByProperty.put(BorderPaintStyleProperties.imageFill, StylePriority.CODE);
        invalidate();
    }

    @Override
    public RectBox box()
    {
        return value.box();
    }

    @Override
    public void box(RectBox box)
    {
        checkRemoveEmpty();
        value.box(box);
        priorityByProperty.put(BorderPaintStyleProperties.box, StylePriority.CODE);
        invalidate();
    }

    @Override
    public String[] names()
    {
        return BorderPaintStyleProperties.KEYS;
    }

    public boolean isEmpty()
    {
        return value == BorderPaint.EMPTY;
    }

    private void checkRemoveEmpty()
    {
        if (isEmpty())
            this.value = new BorderPaintData();
    }

    public enum BorderPaintStyleProperties
    {
        color("border-color"),
        leftColor("border-left-color"),
        rightColor("border-right-color"),
        topColor("border-top-color"),
        bottomColor("border-bottom-color"),
        imageSource("border-image-source"),
        imageWidth("border-image-width"),
        imageOutset("border-image-outset"),
        imageSlice("border-image-slice"),
        imageFill("border-image-fill"),
        box("border-box");

        private final String styleKey;

        BorderPaintStyleProperties(String styleKey)
        {
            this.styleKey = styleKey;
        }

        public String styleKey()
        {
            return styleKey;
        }

        public static BorderPaintStyleProperties fromValue(String value)
        {
            for (var property : values())
            {
                if (property.styleKey.equals(value))
                    return property;
            }
            throw new NoSuchElementException("No value present");
        }

        public static final String[] KEYS = Arrays.stream(BorderPaintStyleProperties.values()).map(BorderPaintStyleProperties::styleKey).toArray(String[]::new);
    }
}
