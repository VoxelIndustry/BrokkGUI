package org.yggard.brokkgui.paint;

import javax.annotation.Nonnull;

import fr.ourten.teabeans.value.BaseProperty;

/**
 * @author Ourten 15 déc. 2016
 */
public class TextStyle
{
    private final BaseProperty<Color>   textColorProperty;
    private final BaseProperty<Color>   shadowColorProperty;
    private final BaseProperty<Boolean> shadowProperty;

    public TextStyle(final Color textColor, final Color shadowColor, final boolean shadow)
    {
        this.textColorProperty = new BaseProperty<>(textColor, "textColorProperty");
        this.shadowColorProperty = new BaseProperty<>(shadowColor, "shadowColorProperty");
        this.shadowProperty = new BaseProperty<>(shadow, "shadowProperty");
    }

    public TextStyle(final @Nonnull Color textColor, final @Nonnull Color shadowColor)
    {
        this(textColor, shadowColor, true);
    }

    public TextStyle(final Color textColor)
    {
        this(textColor, null, false);
    }

    public BaseProperty<Color> getTextColorProperty()
    {
        return this.textColorProperty;
    }

    public BaseProperty<Color> getShadowColorProperty()
    {
        return this.shadowColorProperty;
    }

    public BaseProperty<Boolean> getShadowProperty()
    {
        return this.shadowProperty;
    }

    public Color getTextColor()
    {
        return this.getTextColorProperty().getValue();
    }

    public void setTextColor(final Color textColor)
    {
        this.getTextColorProperty().setValue(textColor);
    }

    public Color getShadowColor()
    {
        return this.getShadowColorProperty().getValue();
    }

    public void setShadowColor(final Color shadowColor)
    {
        this.getShadowColorProperty().setValue(shadowColor);
    }

    public boolean useShadow()
    {
        return this.getShadowProperty().getValue();
    }

    public void setShadow(final boolean useShadow)
    {
        this.getShadowProperty().setValue(useShadow);
    }
}