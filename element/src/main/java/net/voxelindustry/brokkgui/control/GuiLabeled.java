package net.voxelindustry.brokkgui.control;

import fr.ourten.teabeans.binding.Binding;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.shape.TextComponent;

import javax.annotation.Nonnull;

public abstract class GuiLabeled extends GuiSkinedElement
{
    private final Property<String>     ellipsisProperty     = new Property<>("...");
    private final Property<Boolean>    expandToTextProperty = new Property<>(true);
    private final Property<GuiElement> iconProperty;
    private final Property<RectSide>   iconSideProperty     = new Property<>(RectSide.LEFT);
    private final Property<Float>      iconPaddingProperty  = new Property<>(2F);

    private TextComponent textComponent;

    private final String startingText;

    public GuiLabeled(String text, GuiElement icon)
    {
        startingText = text;
        iconProperty = new Property<>(icon);

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

    public Property<RectAlignment> getTextAlignmentProperty()
    {
        return textComponent().textAlignmentProperty();
    }

    public Property<String> getTextProperty()
    {
        return textComponent.textProperty();
    }

    public Property<String> getEllipsisProperty()
    {
        return ellipsisProperty;
    }

    public Property<Boolean> getExpandToTextProperty()
    {
        return expandToTextProperty;
    }

    public Property<RectBox> textPaddingProperty()
    {
        return textComponent().textPaddingProperty();
    }

    public Property<GuiElement> getIconProperty()
    {
        return iconProperty;
    }

    public Property<RectSide> getIconSideProperty()
    {
        return iconSideProperty;
    }

    public Property<Float> getIconPaddingProperty()
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

    public RectBox textPadding()
    {
        return textPaddingProperty().getValue();
    }

    public void setTextPadding(RectBox textPadding)
    {
        textPaddingProperty().setValue(textPadding);
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
        transform().widthProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(getTextProperty(),
                        textComponent().computedTextPaddingValue(),
                        getIconProperty(),
                        getIconPaddingProperty(),
                        getIconSideProperty());
            }

            @Override
            public Float computeValue()
            {
                textComponent().updateTextSettings();
                if (getIconProperty().isPresent())
                {
                    if (getIconSide().isHorizontal())
                        return BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth(getText(), textComponent().textSettings())
                                + textComponent().computedTextPadding().getHorizontal()
                                + getIcon().width() + getIconPadding();
                    else
                        return Math.max(BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth(getText(), textComponent().textSettings()),
                                getIcon().height())
                                + textComponent().computedTextPadding().getHorizontal();
                }
                return BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth(getText(), textComponent().textSettings())
                        + textComponent().computedTextPadding().getHorizontal();
            }
        });

        transform().heightProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(getTextProperty(),
                        textComponent().computedTextPaddingValue(),
                        getIconProperty(),
                        getIconPaddingProperty(),
                        getIconSideProperty());
            }

            @Override
            public Float computeValue()
            {
                textComponent().updateTextSettings();
                if (getIconProperty().isPresent())
                {
                    if (getIconSide().isVertical())
                        return BrokkGuiPlatform.getInstance().getTextHelper().getStringHeight(textComponent().textSettings())
                                + textComponent().computedTextPadding().getVertical()
                                + getIcon().height() + getIconPadding();
                    else
                        return Math.max(BrokkGuiPlatform.getInstance().getTextHelper().getStringHeight(textComponent().textSettings()),
                                getIcon().height())
                                + textComponent().computedTextPadding().getVertical();
                }
                return BrokkGuiPlatform.getInstance().getTextHelper().getStringHeight(textComponent().textSettings())
                        + textComponent().computedTextPadding().getVertical();
            }
        });
    }

    public TextComponent textComponent()
    {
        return textComponent;
    }
}