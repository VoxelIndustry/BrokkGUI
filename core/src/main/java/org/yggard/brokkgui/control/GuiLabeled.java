package org.yggard.brokkgui.control;

import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.paint.Color;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;

public abstract class GuiLabeled extends GuiControl
{
    private final BaseProperty<Color>      textColorProperty;
    private final BaseProperty<EAlignment> textAlignmentProperty;
    private final BaseProperty<String>     textProperty;

    private final BaseProperty<String>     ellipsisProperty;
    private final BaseProperty<Boolean>    expandToTextProperty;

    public GuiLabeled(final String text)
    {
        this.textColorProperty = new BaseProperty<>(Color.BLACK, "textColorProperty");
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

    public BaseProperty<Color> getTextColorProperty()
    {
        return this.textColorProperty;
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

    public Color getTextColor()
    {
        return this.getTextColorProperty().getValue();
    }

    public void setTextColor(final Color textColor)
    {
        this.getTextColorProperty().setValue(textColor);
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
        this.expandToTextProperty.setValue(expandToText);
    }

    private void bindSizeToText()
    {
        this.getWidthProperty().bind(new BaseExpression<>(() ->
        {
            return BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(this.getText());
        }, this.textProperty));

        this.getHeightProperty().bind(new BaseExpression<>(() ->
        {
            return BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight();
        }));
    }
}