package net.voxelindustry.brokkgui.control;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.shape.TextComponent;

import javax.annotation.Nonnull;

public abstract class GuiLabeled extends GuiSkinedElement
{
    private final BaseProperty<String>  ellipsisProperty;
    private final BaseProperty<Boolean> expandToTextProperty;
    private final BaseProperty<RectBox> textPaddingProperty;

    private final BaseProperty<GuiElement> iconProperty;
    private final BaseProperty<RectSide>   iconSideProperty;
    private final BaseProperty<Float>      iconPaddingProperty;

    private TextComponent textComponent;

    private final String startingText;

    public GuiLabeled(String text, GuiElement icon)
    {
        startingText = text;

        ellipsisProperty = new BaseProperty<>("...", "ellipsisProperty");
        expandToTextProperty = new BaseProperty<>(true, "expandToTextProperty");
        textPaddingProperty = new BaseProperty<>(RectBox.EMPTY, "textPaddingProperty");

        iconProperty = new BaseProperty<>(icon, "iconProperty");
        iconSideProperty = new BaseProperty<>(RectSide.LEFT, "iconSideProperty");
        iconPaddingProperty = new BaseProperty<>(2f, "iconPaddingProperty");

        textComponent.text(startingText);

        bindSizeToText();
    }

    public GuiLabeled(String text)
    {
        this(text, null);
    }

    public GuiLabeled()
    {
        this("", null);
    }

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        textComponent = provide(TextComponent.class);
    }

    public BaseProperty<RectAlignment> getTextAlignmentProperty()
    {
        return textComponent().textAlignmentProperty();
    }

    public BaseProperty<String> getTextProperty()
    {
        return textComponent.textProperty();
    }

    public BaseProperty<String> getEllipsisProperty()
    {
        return ellipsisProperty;
    }

    public BaseProperty<Boolean> getExpandToTextProperty()
    {
        return expandToTextProperty;
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
        return getTextAlignmentProperty().getValue();
    }

    public void setTextAlignment(RectAlignment alignment)
    {
        getTextAlignmentProperty().setValue(alignment);
    }

    public String getText()
    {
        return textComponent.text();
    }

    public void setText(@Nonnull String text)
    {
        textComponent.text(text);
    }

    public String getEllipsis()
    {
        return ellipsisProperty.getValue();
    }

    public void setEllipsis(String ellipsis)
    {
        ellipsisProperty.setValue(ellipsis);
    }

    public RectBox getTextPadding()
    {
        return textPaddingProperty.getValue();
    }

    public void setTextPadding(RectBox textPadding)
    {
        textPaddingProperty.setValue(textPadding);
    }

    public GuiElement getIcon()
    {
        return iconProperty.getValue();
    }

    public void setIcon(GuiElement icon)
    {
        iconProperty.setValue(icon);
    }

    public RectSide getIconSide()
    {
        return iconSideProperty.getValue();
    }

    public void setIconSide(RectSide iconSide)
    {
        iconSideProperty.setValue(iconSide);
    }

    public float getIconPadding()
    {
        return iconPaddingProperty.getValue();
    }

    public void setIconPadding(float iconPadding)
    {
        iconPaddingProperty.setValue(iconPadding);
    }

    public boolean expandToText()
    {
        return expandToTextProperty.getValue();
    }

    public void setExpandToText(boolean expandToText)
    {
        if (expandToText && !expandToText())
            bindSizeToText();
        else if (!expandToText && expandToText())
        {
            transform().widthProperty().unbind();
            transform().heightProperty().unbind();
        }
        expandToTextProperty.setValue(expandToText);
    }

    private void bindSizeToText()
    {
        transform().widthProperty().bind(new BaseBinding<Float>()
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
                        return BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getText())
                                + getTextPadding().getLeft() + getTextPadding().getRight()
                                + getIcon().width() + getIconPadding();
                    else
                        return Math.max(BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getText()),
                                getIcon().height())
                                + getTextPadding().getLeft() + getTextPadding().getRight();
                }
                return BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getText())
                        + getTextPadding().getLeft() + getTextPadding().getRight();
            }
        });

        transform().heightProperty().bind(new BaseBinding<Float>()
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
                        return BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight()
                                + getTextPadding().getTop() + getTextPadding().getBottom()
                                + getIcon().height() + getIconPadding();
                    else
                        return Math.max(BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight(),
                                getIcon().height())
                                + getTextPadding().getTop() + getTextPadding().getBottom();
                }
                return BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight()
                        + getTextPadding().getTop() + getTextPadding().getBottom();
            }
        });
    }

    public TextComponent textComponent()
    {
        return textComponent;
    }
}