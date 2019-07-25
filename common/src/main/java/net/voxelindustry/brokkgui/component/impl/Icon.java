package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;

public class Icon extends GuiComponent
{
    private final BaseProperty<GuiElement> iconProperty;
    private final BaseProperty<RectSide>   iconSideProperty;
    private final BaseProperty<Float>      iconPaddingProperty;

    private final BaseProperty<RectBox> contentPaddingProperty;

    public Icon()
    {
        this.iconProperty = new BaseProperty<>(null, "iconProperty");
        this.iconSideProperty = new BaseProperty<>(RectSide.LEFT, "iconSideProperty");
        this.iconPaddingProperty = new BaseProperty<>(2f, "iconPaddingProperty");

        this.contentPaddingProperty = new BaseProperty<>(RectBox.EMPTY, "contentPaddingProperty");

        this.iconProperty.addListener((observable, oldValue, newValue) ->
        {
            if (oldValue != null)
                element().transform().removeChild(oldValue.transform());
            if (newValue != null)
            {
                element().transform().addChild(newValue.transform());
                this.bindIcon(newValue);
            }
        });
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

    public BaseProperty<RectBox> contentPaddingProperty()
    {
        return contentPaddingProperty;
    }

    public void contentPaddingProperty(BaseProperty<RectBox> contentPaddingProperty)
    {
        this.contentPaddingProperty.bind(contentPaddingProperty);
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

    public RectBox contentPadding()
    {
        return this.contentPaddingProperty.getValue();
    }

    private void bindIcon(GuiElement icon)
    {
        icon.transform().xPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(iconSideProperty(),
                        element().transform().xPosProperty(),
                        element().transform().xTranslateProperty(),
                        contentPaddingProperty,
                        element().transform().widthProperty(),
                        icon.transform().widthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (iconSide() == RectSide.LEFT)
                    return element().transform().leftPos()
                            + contentPadding().getLeft();
                if (iconSide() == RectSide.RIGHT)
                    return element().transform().rightPos()
                            - contentPadding().getRight()
                            - icon.width();
                return element().transform().leftPos()
                        + element().width() / 2 - icon.width() / 2
                        + contentPadding().getLeft();
            }
        });

        icon.transform().yPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(iconSideProperty(),
                        element().transform().yPosProperty(),
                        element().transform().yTranslateProperty(),
                        contentPaddingProperty,
                        element().transform().heightProperty(),
                        icon.transform().heightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (iconSide() == RectSide.UP)
                    return element().transform().topPos()
                            + contentPadding().getTop();
                if (iconSide() == RectSide.DOWN)
                    return element().transform().bottomPos()
                            - contentPadding().getBottom()
                            - icon.height();
                return element().transform().topPos()
                        + element().height() / 2 - icon.height() / 2
                        + contentPadding().getTop();
            }
        });
    }
}
