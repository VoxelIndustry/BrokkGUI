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
        return this.getTextAlignmentProperty().getValue();
    }

    public void setTextAlignment(final EAlignment alignment)
    {
        this.getTextAlignmentProperty().setValue(alignment);
    }

    public String getText()
    {
        return this.getTextProperty().getValue();
    }

    public void setText(final String text)
    {
        this.getTextProperty().setValue(text);
    }

    public boolean isWrapText()
    {
        return this.getWrapTextProperty().getValue();
    }

    public void setWrapText(final boolean wrapText)
    {
        this.getWrapTextProperty().setValue(wrapText);
    }

    public Color getTextColor()
    {
        return this.getTextColorProperty().getValue();
    }

    public void setTextColor(final Color textColor)
    {
        this.getTextColorProperty().setValue(textColor);
    }
}