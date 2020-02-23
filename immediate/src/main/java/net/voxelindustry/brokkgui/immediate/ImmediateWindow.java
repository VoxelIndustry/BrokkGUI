package net.voxelindustry.brokkgui.immediate;

import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.immediate.style.BoxStyle;
import net.voxelindustry.brokkgui.immediate.style.StyleType;
import net.voxelindustry.brokkgui.immediate.style.TextStyle;
import net.voxelindustry.brokkgui.paint.Color;

import java.util.EnumMap;

import static net.voxelindustry.brokkgui.immediate.style.StyleType.NORMAL;

public abstract class ImmediateWindow extends BaseImmediateWindow
{
    private EnumMap<StyleType, TextStyle> textStyles = new EnumMap<>(StyleType.class);
    private EnumMap<StyleType, BoxStyle>  boxStyles  = new EnumMap<>(StyleType.class);

    public void setTextStyle(TextStyle style)
    {
        setTextStyle(style, NORMAL);
    }

    public void setTextStyle(TextStyle style, StyleType type)
    {
        this.textStyles.put(type, style);
    }

    public void setBoxStyle(BoxStyle style)
    {
        setBoxStyle(style, NORMAL);
    }

    public void setBoxStyle(BoxStyle style, StyleType type)
    {
        this.boxStyles.put(type, style);
    }

    public void text(String text, float x, float y)
    {
        text(text, x, y, NORMAL);
    }

    public void text(String text, float x, float y, StyleType type)
    {
        TextStyle textStyle = textStyles.get(type);
        text(text, x, y, textStyle.textColor, textStyle.shadowColor);
    }

    public void text(String text, float x, float y, Color color)
    {
        text(text, x, y, color, Color.ALPHA);
    }

    public void text(String text, float x, float y, Color color, Color shadowColor)
    {
        getRenderer().getHelper().drawString(text, x, y, 1, color, shadowColor);
    }

    public void box(float x, float y, float width, float height)
    {
        box(x, y, width, height, NORMAL);
    }

    public void box(float x, float y, float width, float height, StyleType type)
    {
        BoxStyle boxStyle = boxStyles.get(type);
        box(x, y, width, height, boxStyle.boxColor, boxStyle.borderColor, boxStyle.borderThin);
    }

    public void box(float x, float y, float width, float height, Color color)
    {
        box(x, y, width, height, color, Color.ALPHA, 1);
    }

    public void box(float x, float y, float width, float height, Color color, Color borderColor, float borderThin)
    {
        if (borderColor.getAlpha() != 0 && borderThin > 0)
            getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, borderColor, borderThin);
        getRenderer().getHelper().drawColoredRect(getRenderer(), x + borderThin, y + borderThin, width - borderThin * 2, height - borderThin * 2, 1, color);
    }

    public void textBox(String text, float x, float y, StyleType type)
    {
        textBox(text, x, y, type, RectBox.EMPTY);
    }

    public void textBox(String text, float x, float y, StyleType type, RectBox textPadding)
    {
        TextStyle textStyle = textStyles.get(type);
        BoxStyle boxStyle = boxStyles.get(type);
        textBox(text, x, y, 0, 0, textStyle.textColor, textStyle.shadowColor, boxStyle.boxColor, boxStyle.borderColor, boxStyle.borderThin, textPadding);
    }

    public void textBox(String text, float x, float y, float width, float height, Color textColor, Color boxColor)
    {
        textBox(text, x, y, width, height, textColor, boxColor, RectBox.EMPTY);
    }

    public void textBox(String text, float x, float y, float width, float height, Color textColor, Color boxColor, RectBox textPadding)
    {
        textBox(text, x, y, width, height, textColor, Color.ALPHA, boxColor, Color.ALPHA, 0, textPadding);
    }

    public void textBox(String text, float x, float y, float width, float height, Color textColor, Color shadowColor, Color boxColor, Color borderColor, float borderThin, RectBox textPadding)
    {
        if (width == 0)
            width = getRenderer().getHelper().getStringWidth(text) + textPadding.getHorizontal();
        if (height == 0)
            height = getRenderer().getHelper().getStringHeight() + textPadding.getVertical();

        box(x, y, width, height, boxColor, borderColor, borderThin);
        text(text, x + textPadding.getLeft(), y + textPadding.getTop(), textColor, shadowColor);
    }
}
