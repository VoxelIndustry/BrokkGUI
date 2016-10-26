package fr.ourten.brokkgui.control;

import fr.ourten.brokkgui.data.EAlignment;
import fr.ourten.brokkgui.paint.Color;
import fr.ourten.teabeans.value.BaseProperty;

public abstract class GuiLabeled extends GuiControl
{
    private final BaseProperty<Color>      textColorProperty;
    private final BaseProperty<EAlignment> textAlignmentProperty;
    private final BaseProperty<String>     textProperty;
    private final BaseProperty<Boolean>    wrapTextProperty;

    public GuiLabeled(final String text)
    {
        this.textColorProperty = new BaseProperty<>(Color.BLACK, "textColorProperty");
        this.textProperty = new BaseProperty<>(text, "textProperty");
        this.textAlignmentProperty = new BaseProperty<>(EAlignment.MIDDLE_CENTER, "textAlignmentProperty");
        this.wrapTextProperty = new BaseProperty<>(false, "wrapTextProperty");
    }

    public GuiLabeled()
    {
        this("");
    }

    public BaseProperty<EAlignment> getTextAlignmentProperty()
    {
        return this.textAlignmentProperty;
    }

    public BaseProperty<String> getTextProperty()
    {
        return this.textProperty;
    }

    public BaseProperty<Boolean> getWrapTextProperty()
    {
        return this.wrapTextProperty;
    }

    public BaseProperty<Color> getTextColorProperty()
    {
        return this.textColorProperty;
    }

    public EAlignment getTextAlignment()
    {
        return this.textAlignmentProperty.getValue();
    }

    public void setTextAlignment(final EAlignment alignment)
    {
        this.textAlignmentProperty.setValue(alignment);
    }

    public String getText()
    {
        return this.textProperty.getValue();
    }

    public void setText(final String text)
    {
        this.textProperty.setValue(text);
    }

    public boolean isWrapText()
    {
        return this.wrapTextProperty.getValue();
    }

    public void setWrapText(final boolean wrapText)
    {
        this.wrapTextProperty.setValue(wrapText);
    }

    public Color getTextColor()
    {
        return this.textColorProperty.getValue();
    }

    public void setTextColor(final Color textColor)
    {
        this.textColorProperty.setValue(textColor);
    }
}