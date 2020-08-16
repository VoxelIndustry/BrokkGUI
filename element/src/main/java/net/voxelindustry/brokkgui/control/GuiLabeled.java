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
    private final Property<String>  ellipsisProperty;
    private final Property<Boolean> expandToTextProperty;
    private final Property<RectBox> textPaddingProperty;

    private final Property<GuiElement> iconProperty;
    private final Property<RectSide>   iconSideProperty;
    private final Property<Float>      iconPaddingProperty;

    private TextComponent textComponent;

    private final String startingText;

    public GuiLabeled(String text, GuiElement icon)
    {
        startingText = text;

        ellipsisProperty = new Property<>("...");
        expandToTextProperty = new Property<>(true);
        textPaddingProperty = new Property<>(RectBox.EMPTY);

        iconProperty = new Property<>(icon);
        iconSideProperty = new Property<>(RectSide.LEFT);
        iconPaddingProperty = new Property<>(2f);

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

    public Property<RectBox> getTextPaddingProperty()
    {
        return textPaddingProperty;
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
        transform().widthProperty().bindProperty(new Binding<Float>()
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

        transform().heightProperty().bindProperty(new Binding<Float>()
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