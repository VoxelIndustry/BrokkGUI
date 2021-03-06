package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.binding.Binding;
import fr.ourten.teabeans.binding.Expression;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.text.TextComponent;

public class LabelIconComponent extends GuiComponent
{
    private Expression<RectBox> paddingForIconBinding;

    private final Property<GuiElement> iconProperty = new Property<>();

    private final Property<RectSide> iconSideProperty    = new Property<>(RectSide.LEFT);
    private final Property<Float>    iconPaddingProperty = new Property<>(2F);

    private TextComponent textComponent;

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        if (!element.has(TextComponent.class))
            throw new GuiComponentException("Cannot attach LabelIconComponent to an element not having TextComponent!");

        if (!element.has(StyleComponent.class))
            throw new GuiComponentException("Cannot attach LabelIconComponent to an element not having StyleComponent!");

        textComponent = element().get(TextComponent.class);
        StyleComponent styleComponent = element().get(StyleComponent.class);

        // Bindings
        iconProperty().addChangeListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                transform().removeChild(oldValue.transform());
                if (newValue == null)
                    bindText(oldValue);
            }
            if (newValue != null)
            {
                styleComponent.styleClass().add("icon");
                bindTextWithIcon(oldValue, newValue);
                bindIcon(newValue);
            }
        });
        if (iconProperty().isPresent())
        {
            bindTextWithIcon(null, icon());
            bindIcon(icon());
        }
        else
            bindText(null);
    }

    private void bindText(GuiElement previousIcon)
    {
        if (paddingForIconBinding == null)
        {
            paddingForIconBinding = new Expression<>(() -> RectBox.EMPTY,
                    iconSideProperty(),
                    iconPaddingProperty());
            textComponent.addTextPaddingProperty(paddingForIconBinding);
            return;
        }

        paddingForIconBinding.setClosure(() -> RectBox.EMPTY);
        if (previousIcon != null)
        {
            paddingForIconBinding.unbind(previousIcon.transform().widthProperty());
            paddingForIconBinding.unbind(previousIcon.transform().heightProperty());
        }
    }

    private void bindTextWithIcon(GuiElement previousIcon, GuiElement icon)
    {
        if (paddingForIconBinding == null)
        {
            paddingForIconBinding = new Expression<>(this::getTextPaddingForIcon,
                    iconSideProperty(),
                    iconPaddingProperty(),
                    icon.transform().widthProperty(),
                    icon.transform().heightProperty());
            textComponent.addTextPaddingProperty(paddingForIconBinding);
            return;
        }

        if (previousIcon != null)
        {
            paddingForIconBinding.unbind(previousIcon.transform().widthProperty());
            paddingForIconBinding.unbind(previousIcon.transform().heightProperty());
        }

        paddingForIconBinding.bind(icon.transform().widthProperty());
        paddingForIconBinding.bind(icon.transform().heightProperty());

        paddingForIconBinding.setClosure(this::getTextPaddingForIcon);
    }


    private void bindIcon(GuiElement icon)
    {
        transform().addChild(icon.transform());

        icon.transform().xPosProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(iconSideProperty(),
                        transform().xPosProperty(),
                        transform().xTranslateProperty(),
                        textComponent.computedTextPaddingValue(),
                        paddingForIconBinding,
                        transform().widthProperty(),
                        icon.transform().widthProperty());
            }

            @Override
            public Float computeValue()
            {
                RectBox paddingForIcon = paddingForIconBinding.getValue();

                if (iconSide() == RectSide.LEFT)
                {
                    return transform().leftPos()
                            + textComponent.computedTextPadding().getLeft() - paddingForIcon.getLeft();
                }
                if (iconSide() == RectSide.RIGHT)
                    return transform().rightPos()
                            - textComponent.computedTextPadding().getRight() + paddingForIcon.getRight()
                            - icon.width();
                return transform().leftPos()
                        + transform().width() / 2 - icon.width() / 2
                        + textComponent.computedTextPadding().getLeft() - paddingForIcon.getLeft()
                        - textComponent.computedTextPadding().getRight() + paddingForIcon.getRight();
            }
        });

        icon.transform().yPosProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(iconSideProperty(),
                        transform().yPosProperty(),
                        transform().yTranslateProperty(),
                        textComponent.computedTextPaddingValue(),
                        paddingForIconBinding,
                        transform().heightProperty(),
                        icon.transform().heightProperty());
            }

            @Override
            public Float computeValue()
            {
                RectBox paddingForIcon = paddingForIconBinding.getValue();

                if (iconSide() == RectSide.UP)
                    return transform().topPos()
                            + textComponent.computedTextPadding().getTop() - paddingForIcon.getTop();
                if (iconSide() == RectSide.DOWN)
                    return transform().bottomPos()
                            - textComponent.computedTextPadding().getBottom() + paddingForIcon.getBottom()
                            - icon.height();
                return transform().topPos()
                        + transform().height() / 2 - icon.height() / 2
                        + textComponent.computedTextPadding().getTop() - paddingForIcon.getTop()
                        - textComponent.computedTextPadding().getBottom() + paddingForIcon.getBottom();
            }
        });
    }

    private RectBox getTextPaddingForIcon()
    {
        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;

        if (iconSide() == RectSide.LEFT)
            left += icon().width() + iconPadding();
        else if (iconSide() == RectSide.RIGHT)
            right += icon().width() + iconPadding();
        else if (iconSide() == RectSide.UP)
            top += icon().height() + iconPadding();
        else if (iconSide() == RectSide.DOWN)
            bottom += icon().height() + iconPadding();

        return RectBox.build()
                .left(left)
                .right(right)
                .top(top)
                .bottom(bottom)
                .create();
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    public Property<GuiElement> iconProperty()
    {
        return iconProperty;
    }

    public Property<RectSide> iconSideProperty()
    {
        return iconSideProperty;
    }

    public Property<Float> iconPaddingProperty()
    {
        return iconPaddingProperty;
    }

    ////////////////
    //   VALUES   //
    ////////////////

    public GuiElement icon()
    {
        return iconProperty().getValue();
    }

    public void icon(GuiElement icon)
    {
        iconProperty().setValue(icon);
    }

    public RectSide iconSide()
    {
        return iconSideProperty().getValue();
    }

    public void iconSide(RectSide iconSide)
    {
        iconSideProperty().setValue(iconSide);
    }

    public float iconPadding()
    {
        return iconPaddingProperty().getValue();
    }

    public void iconPadding(float iconPadding)
    {
        iconPaddingProperty().setValue(iconPadding);
    }
}
