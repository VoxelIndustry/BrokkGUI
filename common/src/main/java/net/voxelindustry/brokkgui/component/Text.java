package net.voxelindustry.brokkgui.component;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;

import javax.annotation.Nonnull;

public class Text extends GuiComponent
{
    private final BaseProperty<RectAlignment> textAlignmentProperty;
    private final BaseProperty<String>        textProperty;

    private final BaseProperty<String>  ellipsisProperty;
    private final BaseProperty<Boolean> expandToTextProperty;
    private final BaseProperty<RectBox> textPaddingProperty;

    private final BaseProperty<GuiElement> iconProperty;
    private final BaseProperty<RectSide>   iconSideProperty;
    private final BaseProperty<Float>      iconPaddingProperty;

    public Text()
    {
        this.textProperty = new BaseProperty<>("", "textProperty");
        this.textAlignmentProperty = new BaseProperty<>(RectAlignment.MIDDLE_CENTER, "textAlignmentProperty");
        this.ellipsisProperty = new BaseProperty<>("...", "ellipsisProperty");
        this.expandToTextProperty = new BaseProperty<>(true, "expandToTextProperty");
        this.textPaddingProperty = new BaseProperty<>(RectBox.EMPTY, "textPaddingProperty");

        this.iconProperty = new BaseProperty<>(null, "iconProperty");
        this.iconSideProperty = new BaseProperty<>(RectSide.LEFT, "iconSideProperty");
        this.iconPaddingProperty = new BaseProperty<>(2f, "iconPaddingProperty");

        this.bindSizeToText();
    }

    public BaseProperty<RectAlignment> getTextAlignmentProperty()
    {
        return this.textAlignmentProperty;
    }

    public BaseProperty<String> getTextProperty()
    {
        return this.textProperty;
    }

    public BaseProperty<String> getEllipsisProperty()
    {
        return this.ellipsisProperty;
    }

    public BaseProperty<Boolean> getExpandToTextProperty()
    {
        return this.expandToTextProperty;
    }

    public BaseProperty<RectBox> getTextPaddingProperty()
    {
        return textPaddingProperty;
    }

    public BaseProperty<GuiElement> getIconProperty()
    {
        return iconProperty;
    }

    public BaseProperty<RectSide> getIconSideProperty()
    {
        return iconSideProperty;
    }

    public BaseProperty<Float> getIconPaddingProperty()
    {
        return iconPaddingProperty;
    }

    public RectAlignment getTextAlignment()
    {
        return this.getTextAlignmentProperty().getValue();
    }

    public void setTextAlignment(final RectAlignment alignment)
    {
        this.getTextAlignmentProperty().setValue(alignment);
    }

    public String getText()
    {
        return this.getTextProperty().getValue();
    }

    public void setText(@Nonnull final String text)
    {
        this.getTextProperty().setValue(text);
    }

    public String getEllipsis()
    {
        return this.ellipsisProperty.getValue();
    }

    public void setEllipsis(final String ellipsis)
    {
        this.ellipsisProperty.setValue(ellipsis);
    }

    public RectBox getTextPadding()
    {
        return this.textPaddingProperty.getValue();
    }

    public void setTextPadding(RectBox textPadding)
    {
        this.textPaddingProperty.setValue(textPadding);
    }

    public GuiElement getIcon()
    {
        return this.iconProperty.getValue();
    }

    public void setIcon(GuiElement icon)
    {
        this.iconProperty.setValue(icon);
    }

    public RectSide getIconSide()
    {
        return this.iconSideProperty.getValue();
    }

    public void setIconSide(RectSide iconSide)
    {
        this.iconSideProperty.setValue(iconSide);
    }

    public float getIconPadding()
    {
        return this.iconPaddingProperty.getValue();
    }

    public void setIconPadding(float iconPadding)
    {
        this.iconPaddingProperty.setValue(iconPadding);
    }

    public boolean expandToText()
    {
        return this.expandToTextProperty.getValue();
    }

    public void setExpandToText(final boolean expandToText)
    {
        if (expandToText && !this.expandToText())
            this.bindSizeToText();
        else if (!expandToText && this.expandToText())
        {
            this.element().transform().widthProperty().unbind();
            this.element().transform().heightProperty().unbind();
        }
        this.expandToTextProperty.setValue(expandToText);
    }

    private void bindSizeToText()
    {
        this.element().transform().widthProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getTextProperty(),
                        getTextPaddingProperty(),
                        getIconProperty(),
                        getIconPaddingProperty(),
                        getIconSideProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getIconProperty().isPresent())
                {
                    if (getIconSide().isHorizontal())
                        return BrokkGuiPlatform.instance().guiHelper().getStringWidth(getText())
                                + getTextPadding().getLeft() + getTextPadding().getRight()
                                + getIcon().transform().width() + getIconPadding();
                    else
                        return Math.max(BrokkGuiPlatform.instance().guiHelper().getStringWidth(getText()),
                                getIcon().transform().width())
                                + getTextPadding().getLeft() + getTextPadding().getRight();
                }
                return BrokkGuiPlatform.instance().guiHelper().getStringWidth(getText())
                        + getTextPadding().getLeft() + getTextPadding().getRight();
            }
        });

        this.element().transform().heightProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getTextProperty(),
                        getTextPaddingProperty(),
                        getIconProperty(),
                        getIconPaddingProperty(),
                        getIconSideProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getIconProperty().isPresent())
                {
                    if (getIconSide().isVertical())
                        return BrokkGuiPlatform.instance().guiHelper().getStringHeight()
                                + getTextPadding().getTop() + getTextPadding().getBottom()
                                + getIcon().transform().height() + getIconPadding();
                    else
                        return Math.max(BrokkGuiPlatform.instance().guiHelper().getStringHeight(),
                                getIcon().transform().height())
                                + getTextPadding().getTop() + getTextPadding().getBottom();
                }
                return BrokkGuiPlatform.instance().guiHelper().getStringHeight()
                        + getTextPadding().getTop() + getTextPadding().getBottom();
            }
        });
    }
}
