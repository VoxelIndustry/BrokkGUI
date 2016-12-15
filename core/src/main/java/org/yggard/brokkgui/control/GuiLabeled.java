package org.yggard.brokkgui.control;

import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.TextStyle;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;

public abstract class GuiLabeled extends GuiControl
{
    private final BaseProperty<TextStyle>  textStyleProperty;
    private final BaseProperty<EAlignment> textAlignmentProperty;
    private final BaseProperty<String>     textProperty;

    private final BaseProperty<String>     ellipsisProperty;
    private final BaseProperty<Boolean>    expandToTextProperty;

    public GuiLabeled(final String text)
    {
        this.textStyleProperty = new BaseProperty<>(new TextStyle(Color.BLACK), "textStyleProperty");
        this.textProperty = new BaseProperty<>(text, "textProperty");
        this.textAlignmentProperty = new BaseProperty<>(EAlignment.MIDDLE_CENTER, "textAlignmentProperty");
        this.ellipsisProperty = new BaseProperty<>("...", "ellipsisProperty");
        this.expandToTextProperty = new BaseProperty<>(true, "expandToTextProperty");

        this.bindSizeToText();
    }

    public GuiLabeled()
    {
        this("");
    }

    @Override
    public void setWidth(final float width)
    {
        if (this.getWidthProperty().isBound())
            this.setExpandToText(false);
        super.setWidth(width);
    }

    @Override
    public void setHeight(final float height)
    {
        if (this.getHeightProperty().isBound())
            this.setExpandToText(false);
        super.setHeight(height);
    }

    public BaseProperty<EAlignment> getTextAlignmentProperty()
    {
        return this.textAlignmentProperty;
    }

    public BaseProperty<String> getTextProperty()
    {
        return this.textProperty;
    }

    public BaseProperty<TextStyle> getTextStyleProperty()
    {
        return this.textStyleProperty;
    }

    public BaseProperty<String> getEllipsisProperty()
    {
        return this.ellipsisProperty;
    }

    public EAlignment getTextAlignment()
    {
        return this.getTextAlignmentProperty().getValue();
    }

    public void setTextAlignment(final EAlignment alignment)
    {
        this.getTextAlignmentProperty().setValue(alignment);
    }

    public BaseProperty<Boolean> getExpandToTextProperty()
    {
        return this.expandToTextProperty;
    }

    public String getText()
    {
        return this.getTextProperty().getValue();
    }

    public void setText(final String text)
    {
        this.getTextProperty().setValue(text);
    }

    public TextStyle getTextStyle()
    {
        return this.getTextStyleProperty().getValue();
    }

    public void setTextStyle(final TextStyle textStyle)
    {
        this.getTextStyleProperty().setValue(textStyle);
    }

    public Color getTextColor()
    {
        return this.getTextStyle().getTextColor();
    }

    public void setTextColor(final Color textColor)
    {
        this.getTextStyle().setTextColor(textColor);
    }

    public String getEllipsis()
    {
        return this.ellipsisProperty.getValue();
    }

    public void setEllipsis(final String ellipsis)
    {
        this.ellipsisProperty.setValue(ellipsis);
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

    protected void bindSizeToText()
    {
        this.getWidthProperty().bind(BaseExpression.transform(this.getTextProperty(),
                text -> BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(text)));
        this.getHeightProperty().bind(BaseExpression.transform(this.getTextProperty(),
                text -> BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight()));
    }
}