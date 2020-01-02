package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.Observable;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.layout.ILayoutBox;

public class Icon extends GuiComponent implements ILayoutBox
{
    private final BaseProperty<GuiElement> iconProperty;
    private final BaseProperty<RectSide>   iconSideProperty;

    // FIXME: Change to a RectBox padding to remove special casing with iconSide
    private final BaseProperty<Float> iconPaddingProperty;

    private final BaseProperty<RectBox> elementContentPaddingProperty;

    private boolean isLayoutDirty = false;

    private float layoutWidth;
    private float layoutHeight;
    private float layoutPosX;
    private float layoutPosY;

    public Icon()
    {
        this.iconProperty = new BaseProperty<>(null, "iconProperty");
        this.iconSideProperty = new BaseProperty<>(RectSide.LEFT, "iconSideProperty");
        this.iconPaddingProperty = new BaseProperty<>(2f, "iconPaddingProperty");

        this.elementContentPaddingProperty = new BaseProperty<>(RectBox.EMPTY, "elementContentPaddingProperty");

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

        ValueInvalidationListener dirtyLayoutListener = this::dirtyOnChange;
        iconProperty.addListener(dirtyLayoutListener);
        iconSideProperty.addListener(dirtyLayoutListener);
        iconPaddingProperty.addListener(dirtyLayoutListener);
        elementContentPaddingProperty.addListener(dirtyLayoutListener);
    }

    private void dirtyOnChange(Observable obs)
    {
        this.isLayoutDirty = true;
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

    public BaseProperty<RectBox> elementContentPaddingProperty()
    {
        return elementContentPaddingProperty;
    }

    public void elementContentPaddingProperty(ObservableValue<RectBox> elementContentPaddingProperty)
    {
        this.elementContentPaddingProperty.bind(elementContentPaddingProperty);
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

    public RectBox elementContentPadding()
    {
        return this.elementContentPaddingProperty.getValue();
    }

    public void elementContentPadding(RectBox elementContentPadding)
    {
        this.elementContentPaddingProperty().setValue(elementContentPadding);
    }

    private void bindIcon(GuiElement icon)
    {
        icon.transform().xPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(iconSideProperty(),
                        element().transform().xPosProperty(),
                        element().transform().xTranslateProperty(),
                        elementContentPaddingProperty,
                        element().transform().widthProperty(),
                        icon.transform().widthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (iconSide() == RectSide.LEFT)
                    return element().transform().leftPos()
                            + elementContentPadding().getLeft();
                if (iconSide() == RectSide.RIGHT)
                    return element().transform().rightPos()
                            - elementContentPadding().getRight()
                            - icon.width();
                return element().transform().leftPos()
                        + element().width() / 2 - icon.width() / 2
                        + elementContentPadding().getLeft();
            }
        });

        icon.transform().yPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(iconSideProperty(),
                        element().transform().yPosProperty(),
                        element().transform().yTranslateProperty(),
                        elementContentPaddingProperty,
                        element().transform().heightProperty(),
                        icon.transform().heightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (iconSide() == RectSide.UP)
                    return element().transform().topPos()
                            + elementContentPadding().getTop();
                if (iconSide() == RectSide.DOWN)
                    return element().transform().bottomPos()
                            - elementContentPadding().getBottom()
                            - icon.height();
                return element().transform().topPos()
                        + element().height() / 2 - icon.height() / 2
                        + elementContentPadding().getTop();
            }
        });
    }

    ////////////
    // LAYOUT //
    ////////////

    @Override
    public float minWidth()
    {
        return this.icon().width();
    }

    @Override
    public float minHeight()
    {
        return this.icon().height();
    }

    @Override
    public float prefWidth()
    {
        return this.icon().width();
    }

    @Override
    public float prefHeight()
    {
        return this.icon().height();
    }

    @Override
    public float maxWidth()
    {
        return this.icon().width();
    }

    @Override
    public float maxHeight()
    {
        return this.icon().height();
    }

    @Override
    public boolean isLayoutDirty()
    {
        return isLayoutDirty;
    }

    @Override
    public void layoutWidth(float layoutWidth)
    {
        this.layoutWidth = layoutWidth;
    }

    @Override
    public void layoutHeight(float layoutHeight)
    {
        this.layoutHeight = layoutHeight;
    }

    @Override
    public void layoutPosX(float layoutPosX)
    {
        this.layoutPosX = layoutPosX;
    }

    @Override
    public void layoutPosY(float layoutPosY)
    {
        this.layoutPosY = layoutPosY;
    }

    public float layoutWidth()
    {
        return this.layoutWidth;
    }

    public float layoutHeight()
    {
        return this.layoutHeight;
    }

    public float layoutPosX()
    {
        return this.layoutPosX;
    }

    public float layoutPosY()
    {
        return this.layoutPosY;
    }
}
