package net.voxelindustry.brokkgui.style.shorthand.paint;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.paint.outline.OutlinePaint;
import net.voxelindustry.brokkgui.paint.outline.OutlinePaintData;
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

public class OutlinePaintStyleProperty extends StyleProperty<OutlinePaint> implements OutlinePaint, MultiStyleProperty
{
    private final EnumMap<OutlinePaintStyleProperties, StylePriority> priorityByProperty = new EnumMap<>(OutlinePaintStyleProperties.class);

    public OutlinePaintStyleProperty(String name)
    {
        super(OutlinePaint.EMPTY, name, OutlinePaint.class);
    }

    @Override
    public void setStyleRaw(String propertyName, StyleSource source, int specificity, String rawValue, @Nullable AtomicInteger consumedLength)
    {
        var subPropertyKey = OutlinePaintStyleProperties.fromValue(propertyName);

        if (subPropertyKey == OutlinePaintStyleProperties.color)
        {
            var color = StyleTranslator.getInstance().decode(rawValue, Color.class);

            if (!priorityByProperty.containsKey(OutlinePaintStyleProperties.leftColor) ||
                    priorityByProperty.get(OutlinePaintStyleProperties.leftColor).compareTo(source, specificity) < 0)
            {
                colorLeft(color);
                priorityByProperty.put(OutlinePaintStyleProperties.leftColor, new StylePriority(source, specificity));
            }
            if (!priorityByProperty.containsKey(OutlinePaintStyleProperties.rightColor) ||
                    priorityByProperty.get(OutlinePaintStyleProperties.rightColor).compareTo(source, specificity) < 0)
            {
                colorRight(color);
                priorityByProperty.put(OutlinePaintStyleProperties.rightColor, new StylePriority(source, specificity));
            }
            if (!priorityByProperty.containsKey(OutlinePaintStyleProperties.topColor) ||
                    priorityByProperty.get(OutlinePaintStyleProperties.topColor).compareTo(source, specificity) < 0)
            {
                colorTop(color);
                priorityByProperty.put(OutlinePaintStyleProperties.topColor, new StylePriority(source, specificity));
            }
            if (!priorityByProperty.containsKey(OutlinePaintStyleProperties.bottomColor) ||
                    priorityByProperty.get(OutlinePaintStyleProperties.bottomColor).compareTo(source, specificity) < 0)
            {
                colorBottom(color);
                priorityByProperty.put(OutlinePaintStyleProperties.bottomColor, new StylePriority(source, specificity));
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
                case leftColor -> colorLeft(OutlinePaint.EMPTY.colorLeft());
                case rightColor -> colorRight(OutlinePaint.EMPTY.colorRight());
                case topColor -> colorTop(OutlinePaint.EMPTY.colorTop());
                case bottomColor -> colorBottom(OutlinePaint.EMPTY.colorBottom());
                case box -> box(OutlinePaint.EMPTY.box());
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
    public void colorLeft(Color color)
    {
        checkRemoveEmpty();
        value.colorLeft(color);
        priorityByProperty.put(OutlinePaintStyleProperties.leftColor, StylePriority.CODE);
        invalidate();
    }

    @Override
    public void colorRight(Color color)
    {
        checkRemoveEmpty();
        value.colorRight(color);
        priorityByProperty.put(OutlinePaintStyleProperties.rightColor, StylePriority.CODE);
        invalidate();
    }

    @Override
    public void colorTop(Color color)
    {
        checkRemoveEmpty();
        value.colorTop(color);
        priorityByProperty.put(OutlinePaintStyleProperties.topColor, StylePriority.CODE);
        invalidate();
    }

    @Override
    public void colorBottom(Color color)
    {
        checkRemoveEmpty();
        value.colorBottom(color);
        priorityByProperty.put(OutlinePaintStyleProperties.bottomColor, StylePriority.CODE);
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
        priorityByProperty.put(OutlinePaintStyleProperties.box, StylePriority.CODE);
        invalidate();
    }

    @Override
    public String[] names()
    {
        return OutlinePaintStyleProperties.KEYS;
    }

    public boolean isEmpty()
    {
        return value == OutlinePaint.EMPTY;
    }

    private void checkRemoveEmpty()
    {
        if (isEmpty())
            this.value = new OutlinePaintData();
    }


    public enum OutlinePaintStyleProperties
    {
        color("outline-color"),
        leftColor("outline-left-color"),
        rightColor("outline-right-color"),
        topColor("outline-top-color"),
        bottomColor("outline-bottom-color"),
        box("outline-box");

        private final String styleKey;

        OutlinePaintStyleProperties(String styleKey)
        {
            this.styleKey = styleKey;
        }

        public String styleKey()
        {
            return styleKey;
        }

        public static OutlinePaintStyleProperties fromValue(String value)
        {
            for (var property : values())
            {
                if (property.styleKey.equals(value))
                    return property;
            }
            throw new NoSuchElementException("No value present");
        }

        public static final String[] KEYS = Arrays.stream(OutlinePaintStyleProperties.values()).map(OutlinePaintStyleProperties::styleKey).toArray(String[]::new);
    }
}
