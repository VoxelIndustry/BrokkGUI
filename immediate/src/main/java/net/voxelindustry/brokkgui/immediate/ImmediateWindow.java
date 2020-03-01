package net.voxelindustry.brokkgui.immediate;

import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.immediate.style.BoxStyle;
import net.voxelindustry.brokkgui.immediate.style.ButtonStyle;
import net.voxelindustry.brokkgui.immediate.style.EmptyBoxStyle;
import net.voxelindustry.brokkgui.immediate.style.StyleType;
import net.voxelindustry.brokkgui.immediate.style.TextStyle;
import net.voxelindustry.brokkgui.paint.Color;

import java.util.EnumMap;

import static net.voxelindustry.brokkgui.immediate.style.StyleType.NORMAL;
import static net.voxelindustry.brokkgui.paint.Color.ALPHA;

public abstract class ImmediateWindow extends BaseImmediateWindow
{
    private EnumMap<StyleType, TextStyle>     textStyles     = new EnumMap<>(StyleType.class);
    private EnumMap<StyleType, BoxStyle>      boxStyles      = new EnumMap<>(StyleType.class);
    private EnumMap<StyleType, EmptyBoxStyle> emptyBoxStyles = new EnumMap<>(StyleType.class);
    private EnumMap<StyleType, ButtonStyle>   buttonStyles   = new EnumMap<>(StyleType.class);

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

    public void setEmptyBoxStyle(EmptyBoxStyle style)
    {
        setEmptyBoxStyle(style, NORMAL);
    }

    public void setEmptyBoxStyle(EmptyBoxStyle style, StyleType type)
    {
        this.emptyBoxStyles.put(type, style);
    }

    public void setButtonStyle(ButtonStyle style)
    {
        setButtonStyle(style, NORMAL);
    }

    public void setButtonStyle(ButtonStyle style, StyleType type)
    {
        this.buttonStyles.put(type, style);
    }

    public boolean text(String text, float x, float y)
    {
        return text(text, x, y, NORMAL);
    }

    public boolean text(String text, float x, float y, StyleType type)
    {
        TextStyle textStyle = textStyles.get(type);
        return text(text, x, y, textStyle.textColor, textStyle.shadowColor, textStyle.hoverTextColor, textStyle.hoverShadowColor);
    }

    public boolean text(String text, float x, float y, Color color)
    {
        return text(text, x, y, color, ALPHA, color, ALPHA);
    }

    public boolean text(String text, float x, float y, Color color, Color shadowColor, Color hoverColor, Color hoverShadowColor)
    {
        boolean isHovered = getMouseX() > x && getMouseY() > y && getMouseY() < y + getRenderer().getHelper().getStringHeightMultiLine(text) && getMouseX() < x + getRenderer().getHelper().getStringWidthMultiLine(text);

        if (!isHovered)
            getRenderer().getHelper().drawStringMultiline(text, x, y, 1, color, shadowColor);
        else
            getRenderer().getHelper().drawStringMultiline(text, x, y, 1, hoverColor, hoverShadowColor);

        return isHovered;
    }

    public boolean box(float x, float y, float width, float height)
    {
        return box(x, y, width, height, NORMAL);
    }

    public boolean box(float x, float y, float width, float height, StyleType type)
    {
        BoxStyle boxStyle = boxStyles.get(type);
        return box(x, y, width, height, boxStyle.boxColor, boxStyle.borderColor, boxStyle.borderThin, boxStyle.hoverBoxColor, boxStyle.hoverBorderColor);
    }

    public boolean box(float x, float y, float width, float height, Color color)
    {
        return box(x, y, width, height, color, ALPHA, 1, color, ALPHA);
    }

    public boolean box(float x, float y, float width, float height, Color color, Color borderColor, float borderThin, Color hoverColor, Color hoverBorderColor)
    {
        boolean isHovered = getMouseX() > x && getMouseY() > y && getMouseX() < x + width && getMouseY() < y + height;

        if (!isHovered)
        {
            if (borderColor.getAlpha() != 0 && borderThin > 0)
                getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, borderColor, borderThin);
            getRenderer().getHelper().drawColoredRect(getRenderer(), x + borderThin, y + borderThin, width - borderThin * 2, height - borderThin * 2, 1, color);
        }
        else
        {
            if (borderColor.getAlpha() != 0 && borderThin > 0)
                getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, hoverBorderColor, borderThin);
            getRenderer().getHelper().drawColoredRect(getRenderer(), x + borderThin, y + borderThin, width - borderThin * 2, height - borderThin * 2, 1, hoverColor);
        }
        return isHovered;
    }

    public boolean textBox(String text, float x, float y, StyleType type)
    {
        return textBox(text, x, y, type, RectBox.EMPTY);
    }

    public boolean textBox(String text, float x, float y, StyleType type, RectBox textPadding)
    {
        TextStyle textStyle = textStyles.get(type);
        BoxStyle boxStyle = boxStyles.get(type);

        return textBox(text,
                x,
                y,
                0,
                0,
                textStyle.textColor,
                textStyle.shadowColor,
                boxStyle.boxColor,
                boxStyle.borderColor,
                boxStyle.borderThin,
                textPadding,
                textStyle.hoverTextColor,
                textStyle.hoverShadowColor,
                boxStyle.hoverBoxColor,
                boxStyle.hoverBorderColor);
    }

    public boolean textBox(String text, float x, float y, float width, float height, Color textColor, Color boxColor)
    {
        return textBox(text, x, y, width, height, textColor, boxColor, RectBox.EMPTY);
    }

    public boolean textBox(String text, float x, float y, float width, float height, Color textColor, Color boxColor, RectBox textPadding)
    {
        return textBox(text, x, y, width, height, textColor, ALPHA, boxColor, ALPHA, 0, textPadding, textColor, ALPHA, boxColor, ALPHA);
    }

    public boolean textBox(String text,
                           float x,
                           float y,
                           float width,
                           float height,
                           Color textColor,
                           Color shadowColor,
                           Color boxColor,
                           Color borderColor,
                           float borderThin,
                           RectBox textPadding,
                           Color hoverTextColor,
                           Color hoverShadowColor,
                           Color hoverBoxColor,
                           Color hoverBorderColor)
    {
        if (width == 0)
            width = getRenderer().getHelper().getStringWidthMultiLine(text) + textPadding.getHorizontal() + borderThin;
        if (height == 0)
            height = getRenderer().getHelper().getStringHeightMultiLine(text) + textPadding.getVertical() + borderThin;

        boolean isHovered = box(x, y, width, height, boxColor, borderColor, borderThin, hoverBoxColor, hoverBorderColor);
        text(text, x + textPadding.getLeft() + borderThin, y + textPadding.getTop() + borderThin, textColor, shadowColor, hoverTextColor, hoverShadowColor);

        return isHovered;
    }

    public boolean emptyBox(float x, float y, float width, float height, StyleType type)
    {
        EmptyBoxStyle style = emptyBoxStyles.get(type);

        if (style == null)
        {
            BoxStyle boxStyle = boxStyles.get(type);
            return emptyBox(x, y, width, height, boxStyle.borderColor, boxStyle.hoverBorderColor, boxStyle.borderThin);
        }
        return emptyBox(x, y, width, height, style.borderColor, style.hoverBorderColor, style.borderThin);
    }

    public boolean emptyBox(float x, float y, float width, float height, Color borderColor, Color hoverBorderColor, float borderThin)
    {
        boolean isHovered = getMouseX() > x && getMouseY() < y && getMouseX() < x + width && getMouseY() < y + height;

        if (!isHovered)
        {
            getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, borderColor, borderThin);
        }
        else
        {
            getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, hoverBorderColor, borderThin);
        }
        return isHovered;
    }

    public InteractionResult button(String text, float x, float y, float width, float height, StyleType type)
    {
        return button(text, x, y, width, height, buttonStyles.get(type));
    }

    public InteractionResult button(String text, float x, float y, float width, float height, ButtonStyle style)
    {
        boolean isHovered = getMouseX() > x && getMouseY() < y && getMouseX() < x + width && getMouseY() < y + height;
        boolean isClicked = getLastClickX() > x && getLastClickY() < y && getLastClickX() > x + width && getLastClickY() > y + height;

        if (isClicked)
        {
            getRenderer().getHelper().drawColoredRect(getRenderer(), x, y, width, height, 1, style.clickBoxColor);
            getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, style.clickBorderColor, style.borderThin);

            getRenderer().getHelper().drawString(text, x + style.padding.getLeft(), y + style.padding.getTop(), 1, style.clickTextColor, style.clickShadowColor);

            return InteractionResult.CLICKED;
        }
        if (isHovered)
        {
            getRenderer().getHelper().drawColoredRect(getRenderer(), x, y, width, height, 1, style.hoverBoxColor);
            getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, style.hoverBorderColor, style.borderThin);

            getRenderer().getHelper().drawString(text, x + style.padding.getLeft(), y + style.padding.getTop(), 1, style.hoverTextColor, style.hoverShadowColor);

            return InteractionResult.HOVERED;
        }

        getRenderer().getHelper().drawColoredRect(getRenderer(), x, y, width, height, 1, style.boxColor);
        getRenderer().getHelper().drawColoredEmptyRect(getRenderer(), x, y, width, height, 1, style.borderColor, style.borderThin);

        getRenderer().getHelper().drawString(text, x + style.padding.getLeft(), y + style.padding.getTop(), 1, style.textColor, style.shadowColor);

        return InteractionResult.NONE;
    }
}
