package net.voxelindustry.brokkgui.component;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.data.RectSide;

public class Icon extends GuiComponent
{
    private final BaseProperty<GuiElement> iconProperty;
    private final BaseProperty<RectSide>   iconSideProperty;
    private final BaseProperty<Float>      iconPaddingProperty;

    public Icon()
    {
        this.iconProperty = new BaseProperty<>(null, "iconProperty");
        this.iconSideProperty = new BaseProperty<>(RectSide.LEFT, "iconSideProperty");
        this.iconPaddingProperty = new BaseProperty<>(2f, "iconPaddingProperty");
    }

    public BaseProperty<GuiElement> iconProperty()
    {
        return iconProperty;
    }

    public BaseProperty<RectSide> iconSideProperty()
    {
        return iconSideProperty;
    }

    public BaseProperty<Float> iconPaddingProperty()
    {
        return iconPaddingProperty;
    }

    public GuiElement icon()
    {
        return this.iconProperty.getValue();
    }

    public void icon(GuiElement icon)
    {
        this.iconProperty.setValue(icon);
    }

    public RectSide iconSide()
    {
        return this.iconSideProperty.getValue();
    }

    public void iconSide(RectSide iconSide)
    {
        this.iconSideProperty.setValue(iconSide);
    }

    public float iconPadding()
    {
        return this.iconPaddingProperty.getValue();
    }

    public void iconPadding(float iconPadding)
    {
        this.iconPaddingProperty.setValue(iconPadding);
    }
}
