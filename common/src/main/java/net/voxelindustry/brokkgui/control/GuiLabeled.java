package net.voxelindustry.brokkgui.control;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectOffset;
import net.voxelindustry.brokkgui.data.RectSide;

import javax.annotation.Nonnull;

public abstract class GuiLabeled extends GuiElement
{
    private final BaseProperty<RectAlignment> textAlignmentProperty;
    private final BaseProperty<String>        textProperty;

    private final BaseProperty<String>     ellipsisProperty;
    private final BaseProperty<Boolean>    expandToTextProperty;
    private final BaseProperty<RectOffset> textPaddingProperty;

    private final BaseProperty<GuiNode>  iconProperty;
    private final BaseProperty<RectSide> iconSideProperty;
    private final BaseProperty<Float>    iconPaddingProperty;

    public GuiLabeled(String type, String text, GuiNode icon)
    {
        super(type);

        this.textProperty = new BaseProperty<>(text, "textProperty");
        this.textAlignmentProperty = new BaseProperty<>(RectAlignment.MIDDLE_CENTER, "textAlignmentProperty");
        this.ellipsisProperty = new BaseProperty<>("...", "ellipsisProperty");
        this.expandToTextProperty = new BaseProperty<>(true, "expandToTextProperty");
        this.textPaddingProperty = new BaseProperty<>(RectOffset.EMPTY, "textPaddingProperty");

        this.iconProperty = new BaseProperty<>(icon, "iconProperty");
        this.iconSideProperty = new BaseProperty<>(RectSide.LEFT, "iconSideProperty");
        this.iconPaddingProperty = new BaseProperty<>(2f, "iconPaddingProperty");

        this.bindSizeToText();
    }

    public GuiLabeled(String type, String text)
    {
        this(type, text, null);
    }

    public GuiLabeled(String type)
    {
        this(type, "", null);
    }

    @Override
    public void setWidth(final float width)
    {
        if (this.getWidthProperty().isBound() && this.expandToText())
            this.getWidthProperty().unbind();
        super.setWidth(width);
    }

    @Override
    public void setHeight(final float height)
    {
        if (this.getHeightProperty().isBound() && this.expandToText())
            this.getHeightProperty().unbind();
        super.setHeight(height);
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

    public BaseProperty<RectOffset> getTextPaddingProperty()
    {
        return textPaddingProperty;
    }

    public BaseProperty<GuiNode> getIconProperty()
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

    public RectOffset getTextPadding()
    {
        return this.textPaddingProperty.getValue();
    }

    public void setTextPadding(RectOffset textPadding)
    {
        this.textPaddingProperty.setValue(textPadding);
    }

    public GuiNode getIcon()
    {
        return this.iconProperty.getValue();
    }

    public void setIcon(GuiNode icon)
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
            this.getWidthProperty().unbind();
            this.getHeightProperty().unbind();
        }
        this.expandToTextProperty.setValue(expandToText);
    }

    private void bindSizeToText()
    {
        this.getWidthProperty().bind(new BaseBinding<Float>()
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
                                + getIcon().getWidth() + getIconPadding();
                    else
                        return Math.max(BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getText()),
                                getIcon().getWidth())
                                + getTextPadding().getLeft() + getTextPadding().getRight();
                }
                return BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getText())
                        + getTextPadding().getLeft() + getTextPadding().getRight();
            }
        });

        this.getHeightProperty().bind(new BaseBinding<Float>()
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
                                + getIcon().getHeight() + getIconPadding();
                    else
                        return Math.max(BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight(),
                                getIcon().getHeight())
                                + getTextPadding().getTop() + getTextPadding().getBottom();
                }
                return BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight()
                        + getTextPadding().getTop() + getTextPadding().getBottom();
            }
        });
    }
}