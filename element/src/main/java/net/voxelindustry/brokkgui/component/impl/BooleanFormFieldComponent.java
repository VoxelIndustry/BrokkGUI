package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.binding.Binding;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.shape.TextComponent;

public class BooleanFormFieldComponent extends GuiComponent
{
    private final Property<RectSide>   buttonSideProperty = new Property<>(RectSide.LEFT);
    private final Property<GuiElement> buttonNodeProperty = new Property<>(null);
    private final Property<Float>      buttonSizeProperty = new Property<>(10F);

    public BooleanFormFieldComponent()
    {
        buttonNodeProperty.addListener((obs, oldValue, newValue) ->
        {
            if (newValue != null && !transform().hasChild(newValue.transform()))
            {
                boolean alreadyBindChild = transform().bindChild();
                if (alreadyBindChild)
                    transform().bindChild(false);

                transform().addChild(newValue.transform());

                newValue.transform().xPosProperty().bindProperty(new Binding<Float>()
                {
                    {
                        super.bind(transform().xTranslateProperty(),
                                transform().xPosProperty(),
                                transform().widthProperty(),
                                buttonSideProperty,
                                newValue.transform().widthProperty());
                    }

                    @Override
                    public Float computeValue()
                    {
                        switch (buttonSide())
                        {
                            case UP:
                            case DOWN:
                                return transform().leftPos() + transform().width() / 2 - newValue.transform().width() / 2;
                            case LEFT:
                                return transform().leftPos();
                            case RIGHT:
                                return transform().rightPos() - newValue.transform().width();
                        }
                        return 0F;
                    }
                });

                newValue.transform().yPosProperty().bindProperty(new Binding<Float>()
                {
                    {
                        super.bind(transform().yTranslateProperty(),
                                transform().yPosProperty(),
                                transform().heightProperty(),
                                buttonSideProperty,
                                newValue.transform().heightProperty());
                    }

                    @Override
                    public Float computeValue()
                    {
                        switch (buttonSide())
                        {
                            case UP:
                                return transform().topPos();
                            case DOWN:
                                return transform().bottomPos() - newValue.transform().height();
                            case LEFT:
                            case RIGHT:
                                return transform().topPos() + transform().height() / 2 - newValue.transform().height() / 2;
                        }
                        return 0F;
                    }
                });

                if (alreadyBindChild)
                    transform().bindChild(true);
            }
            if (oldValue != null && transform().hasChild(oldValue.transform()))
                transform().removeChild(oldValue.transform());
        });
    }

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        if (!element.has(ToggleButtonComponent.class))
            throw new GuiComponentException("Cannot attach RadioButtonComponent to an element not having ToggleButtonComponent!");

        if (!element.has(TextComponent.class) && !element.has(TextComponentStyle.class))
            throw new GuiComponentException("Cannot attach RadioButtonComponent to an element not having TextComponent!");

        element.get(TextComponent.class).addTextPaddingProperty(buttonSideProperty.combine(buttonSizeProperty, RectBox::fromSide));
    }

    public Property<RectSide> buttonSideProperty()
    {
        return buttonSideProperty;
    }

    public Property<GuiElement> buttonNodeProperty()
    {
        return buttonNodeProperty;
    }

    public Property<Float> buttonSizeProperty()
    {
        return buttonSizeProperty;
    }

    public RectSide buttonSide()
    {
        return buttonSideProperty().getValue();
    }

    public void buttonSide(RectSide buttonSide)
    {
        buttonSideProperty().setValue(buttonSide);
    }

    public GuiElement buttonNode()
    {
        return buttonNodeProperty().getValue();
    }

    public void buttonNode(GuiElement buttonNode)
    {
        buttonNodeProperty().setValue(buttonNode);
    }

    public float buttonSize()
    {
        return buttonSizeProperty().getValue();
    }

    public void buttonSize(float buttonSize)
    {
        buttonSizeProperty().setValue(buttonSize);
    }
}
