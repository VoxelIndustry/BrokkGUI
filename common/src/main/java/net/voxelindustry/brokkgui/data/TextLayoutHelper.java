package net.voxelindustry.brokkgui.data;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.binding.Binding;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Icon;
import net.voxelindustry.brokkgui.component.impl.Text;

import javax.annotation.Nullable;

public class TextLayoutHelper
{
    public static Binding<Float> createXPosBinding(GuiElement element, Text text, Icon icon, Binding<String> ellipsedTextProperty)
    {
        return new BaseBinding<Float>()
        {
            {
                super.bind(text.textAlignmentProperty(),
                        element.transform().widthProperty(),
                        icon.iconProperty(),
                        icon.iconSideProperty(),
                        icon.iconPaddingProperty(),
                        text.textPaddingProperty(),
                        text.elementContentPaddingProperty(),
                        ellipsedTextProperty);
            }

            @Override
            public Float computeValue()
            {
                float iconWidth = 0;
                if (icon.iconProperty().isPresent() && icon.iconSide().isHorizontal())
                    iconWidth = icon.icon().width() + icon.iconPadding();

                if (text.textAlignment().isLeft())
                    return text.textPadding().getLeft() + text.elementContentPadding().getLeft()
                            + (icon.iconSide() == RectSide.LEFT ? iconWidth : 0);
                else if (text.textAlignment().isRight())
                    return element.width()
                            - text.textPadding().getRight() - text.elementContentPadding().getRight()
                            - BrokkGuiPlatform.instance().guiHelper().getStringWidth(ellipsedTextProperty.getValue())
                            - (icon.iconSide() == RectSide.RIGHT ? iconWidth : 0);
                else
                    return text.textPadding().getLeft() + text.elementContentPadding().getLeft()
                            + (icon.iconSide() == RectSide.LEFT ? iconWidth : 0)
                            + getAvailableTextWidth(element, text, icon) / 2
                            - BrokkGuiPlatform.instance().guiHelper().getStringWidth(ellipsedTextProperty.getValue()) / 2;
            }
        };
    }

    public static Binding<Float> createYPosBinding(GuiElement element, Text text, Icon icon)
    {
        return new BaseBinding<Float>()
        {
            {
                super.bind(text.textAlignmentProperty(),
                        element.transform().heightProperty(),
                        icon.iconProperty(),
                        icon.iconSideProperty(),
                        icon.iconPaddingProperty(),
                        text.textPaddingProperty(),
                        text.elementContentPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                float iconHeight = 0;
                if (icon.iconProperty().isPresent() && icon.iconSide().isVertical())
                    iconHeight = icon.icon().height() + icon.iconPadding();

                if (text.textAlignment().isUp())
                    return text.textPadding().getTop() + text.elementContentPadding().getTop()
                            + (icon.iconSide() == RectSide.UP ? iconHeight : 0);
                else if (text.textAlignment().isDown())
                    return element.height()
                            - text.textPadding().getBottom() - text.elementContentPadding().getBottom()
                            - BrokkGuiPlatform.instance().guiHelper().getStringHeight()
                            - (icon.iconSide() == RectSide.DOWN ? iconHeight : 0);
                else
                    return element.height() / 2
                            + (icon.iconSide() == RectSide.UP ? iconHeight :
                            (icon.iconSide() == RectSide.DOWN ? -iconHeight : 0)) / 2
                            - BrokkGuiPlatform.instance().guiHelper().getStringHeight() / 2
                            + text.textPadding().getTop() + text.elementContentPadding().getTop();
            }
        };
    }

    public static Binding<String> createEllipsedTextBinding(GuiElement element, Text text, Icon icon)
    {
        return new BaseBinding<String>()
        {
            {
                super.bind(text.textProperty(),
                        text.expandToTextProperty(),
                        element.transform().widthProperty(),
                        text.ellipsisProperty(),
                        text.textPaddingProperty(),
                        text.elementContentPaddingProperty(),
                        icon.iconPaddingProperty(),
                        icon.iconSideProperty(),
                        icon.iconProperty());
            }

            @Override
            public String computeValue()
            {
                if (!text.expandToText() && element.width() < getExpandedWidth(text, icon))
                {
                    String trimmed = BrokkGuiPlatform.instance().guiHelper().trimStringToPixelWidth(
                            text.text(), (int) (getAvailableTextWidth(element, text, icon)));

                    if (trimmed.length() < text.ellipsis().length())
                        return "";
                    trimmed = trimmed.substring(0, trimmed.length() - text.ellipsis().length());
                    return trimmed + text.ellipsis();
                }
                return text.text();
            }
        };
    }

    public static Binding<Float> createMinimalWidthBinding(Text text, Icon icon)
    {
        return new BaseBinding<Float>()
        {
            {
                super.bind(text.textProperty(),
                        text.textPaddingProperty(),
                        text.elementContentPaddingProperty(),
                        icon.iconProperty(),
                        icon.iconPaddingProperty(),
                        icon.iconSideProperty());
            }

            @Override
            public Float computeValue()
            {
                if (icon.iconProperty().isPresent())
                {
                    if (icon.iconSide().isHorizontal())
                        return BrokkGuiPlatform.instance().guiHelper().getStringWidth(text.text())
                                + text.textPadding().getLeft() + text.textPadding().getRight()
                                + text.elementContentPadding().getLeft() + text.elementContentPadding().getRight()
                                + icon.icon().width() + icon.iconPadding();
                    else
                        return Math.max(BrokkGuiPlatform.instance().guiHelper().getStringWidth(text.text()),
                                icon.icon().width())
                                + text.textPadding().getLeft() + text.textPadding().getRight()
                                + text.elementContentPadding().getLeft() + text.elementContentPadding().getRight();
                }
                return BrokkGuiPlatform.instance().guiHelper().getStringWidth(text.text())
                        + text.textPadding().getLeft() + text.textPadding().getRight()
                        + text.elementContentPadding().getLeft() + text.elementContentPadding().getRight();
            }
        };
    }

    public static Binding<Float> createMinimalHeightBinding(Text text, Icon icon)
    {
        return new BaseBinding<Float>()
        {
            {
                super.bind(text.textProperty(),
                        text.textPaddingProperty(),
                        text.elementContentPaddingProperty(),
                        icon.iconProperty(),
                        icon.iconPaddingProperty(),
                        icon.iconSideProperty());
            }

            @Override
            public Float computeValue()
            {
                if (icon.iconProperty().isPresent())
                {
                    if (icon.iconSide().isVertical())
                        return BrokkGuiPlatform.instance().guiHelper().getStringHeight()
                                + text.textPadding().getTop() + text.textPadding().getBottom()
                                + text.elementContentPadding().getTop() + text.elementContentPadding().getBottom()
                                + icon.icon().height() + icon.iconPadding();
                    else
                        return Math.max(BrokkGuiPlatform.instance().guiHelper().getStringHeight(),
                                icon.icon().height())
                                + text.textPadding().getTop() + text.textPadding().getBottom()
                                + text.elementContentPadding().getTop() + text.elementContentPadding().getBottom();
                }
                return BrokkGuiPlatform.instance().guiHelper().getStringHeight()
                        + text.textPadding().getTop() + text.textPadding().getBottom()
                        + text.elementContentPadding().getTop() + text.elementContentPadding().getBottom();
            }
        };
    }

    public static Binding<Float> createXPosBinding(GuiElement element, Text text, Binding<String> ellipsedTextProperty)
    {
        return new BaseBinding<Float>()
        {
            {
                super.bind(text.textAlignmentProperty(),
                        element.transform().widthProperty(),
                        text.textPaddingProperty(),
                        text.elementContentPaddingProperty(),
                        ellipsedTextProperty);
            }

            @Override
            public Float computeValue()
            {
                if (text.textAlignment().isLeft())
                    return text.textPadding().getLeft() + text.elementContentPadding().getLeft();
                else if (text.textAlignment().isRight())
                    return element.width()
                            - text.textPadding().getRight() - text.elementContentPadding().getRight()
                            - BrokkGuiPlatform.instance().guiHelper().getStringWidth(ellipsedTextProperty.getValue());
                else
                    return text.textPadding().getLeft() + text.elementContentPadding().getLeft()
                            + getAvailableTextWidth(element, text, null) / 2
                            - BrokkGuiPlatform.instance().guiHelper().getStringWidth(ellipsedTextProperty.getValue()) / 2;
            }
        };
    }

    public static Binding<Float> createYPosBinding(GuiElement element, Text text)
    {
        return new BaseBinding<Float>()
        {
            {
                super.bind(text.textAlignmentProperty(),
                        element.transform().heightProperty(),
                        text.textPaddingProperty(),
                        text.elementContentPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                if (text.textAlignment().isUp())
                    return text.textPadding().getTop() + text.elementContentPadding().getTop();
                else if (text.textAlignment().isDown())
                    return element.height()
                            - text.textPadding().getBottom() - text.elementContentPadding().getBottom()
                            - BrokkGuiPlatform.instance().guiHelper().getStringHeight();
                else
                    return element.height() / 2
                            - BrokkGuiPlatform.instance().guiHelper().getStringHeight() / 2
                            + text.textPadding().getTop() + text.elementContentPadding().getTop();
            }
        };
    }

    public static Binding<String> createEllipsedTextBinding(GuiElement element, Text text)
    {
        return new BaseBinding<String>()
        {
            {
                super.bind(text.textProperty(),
                        text.expandToTextProperty(),
                        element.transform().widthProperty(),
                        text.ellipsisProperty(),
                        text.textPaddingProperty(),
                        text.elementContentPaddingProperty());
            }

            @Override
            public String computeValue()
            {
                if (!text.expandToText() && element.width() < getExpandedWidth(text, null))
                {
                    String trimmed = BrokkGuiPlatform.instance().guiHelper().trimStringToPixelWidth(
                            text.text(), (int) (getAvailableTextWidth(element, text, null)));

                    if (trimmed.length() < text.ellipsis().length())
                        return "";
                    trimmed = trimmed.substring(0, trimmed.length() - text.ellipsis().length());
                    return trimmed + text.ellipsis();
                }
                return text.text();
            }
        };
    }

    public static Binding<Float> createMinimalWidthBinding(Text text)
    {
        return new BaseBinding<Float>()
        {
            {
                super.bind(text.textProperty(),
                        text.textPaddingProperty(),
                        text.elementContentPaddingProperty());
            }

            @Override
            public Float computeValue()
            {

                return BrokkGuiPlatform.instance().guiHelper().getStringWidth(text.text())
                        + text.textPadding().getLeft() + text.textPadding().getRight()
                        + text.elementContentPadding().getLeft() + text.elementContentPadding().getRight();
            }
        };
    }

    public static Binding<Float> createMinimalHeightBinding(Text text)
    {
        return new BaseBinding<Float>()
        {
            {
                super.bind(text.textProperty(),
                        text.textPaddingProperty(),
                        text.elementContentPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                return BrokkGuiPlatform.instance().guiHelper().getStringHeight()
                        + text.textPadding().getTop() + text.textPadding().getBottom()
                        + text.elementContentPadding().getTop() + text.elementContentPadding().getBottom();
            }
        };
    }

    private static float getExpandedWidth(Text text, @Nullable Icon icon)
    {
        if (icon != null && icon.iconProperty().isPresent())
        {
            if (icon.iconSide().isHorizontal())
                return BrokkGuiPlatform.instance().guiHelper().getStringWidth(text.text())
                        + text.textPadding().getLeft() + text.textPadding().getRight()
                        + icon.icon().width() + icon.iconPadding();
            else
                return Math.max(BrokkGuiPlatform.instance().guiHelper().getStringWidth(text.text()),
                        icon.icon().width())
                        + text.textPadding().getLeft() + text.textPadding().getRight();
        }
        return BrokkGuiPlatform.instance().guiHelper().getStringWidth(text.text())
                + text.textPadding().getLeft() + text.textPadding().getRight();
    }

    private static float getAvailableTextWidth(GuiElement element, Text text, @Nullable Icon icon)
    {
        if (icon != null && icon.iconProperty().isPresent() && icon.iconSide().isHorizontal())
        {
            return element.width()
                    - text.textPadding().getLeft() - text.textPadding().getRight()
                    - text.elementContentPadding().getLeft() - text.elementContentPadding().getRight()
                    - icon.icon().width() - icon.iconPadding();
        }
        return element.width() - text.textPadding().getLeft() - text.textPadding().getRight();
    }
}
