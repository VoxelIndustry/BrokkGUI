package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.BooleanFormFieldComponent;
import net.voxelindustry.brokkgui.component.impl.ToggleButtonComponent;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.style.StyleComponent;

public class GuiRadioButton extends GuiToggleButton
{
    private BooleanFormFieldComponent booleanFormFieldComponent;

    public GuiRadioButton(String text)
    {
        super(text);

        booleanFormFieldComponent.buttonNodeProperty().setValue(new RadioButtonContent(toggleButtonComponent(), booleanFormFieldComponent));
    }

    public GuiRadioButton()
    {
        this("");
    }

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        booleanFormFieldComponent = provide(BooleanFormFieldComponent.class);
        booleanFormFieldComponent.buttonSize(10);
    }

    public BooleanFormFieldComponent booleanFormFieldComponent()
    {
        return booleanFormFieldComponent;
    }

    public Property<RectSide> buttonSideProperty()
    {
        return booleanFormFieldComponent.buttonSideProperty();
    }

    public Property<GuiElement> buttonNodeProperty()
    {
        return booleanFormFieldComponent.buttonNodeProperty();
    }

    public Property<Float> buttonSizeProperty()
    {
        return booleanFormFieldComponent.buttonSizeProperty();
    }

    public RectSide buttonSide()
    {
        return booleanFormFieldComponent.buttonSide();
    }

    public void buttonSide(RectSide buttonSide)
    {
        booleanFormFieldComponent.buttonSide(buttonSide);
    }

    public GuiElement buttonNode()
    {
        return booleanFormFieldComponent.buttonNode();
    }

    public void buttonNode(GuiElement buttonNode)
    {
        booleanFormFieldComponent.buttonNode(buttonNode);
    }

    public float buttonSize()
    {
        return booleanFormFieldComponent.buttonSize();
    }

    public void buttonSize(float buttonSize)
    {
        booleanFormFieldComponent.buttonSize(buttonSize);
    }

    @Override
    public String type()
    {
        return "radio-button";
    }

    public static class RadioButtonContent extends Rectangle
    {
        private final Rectangle mark;

        public RadioButtonContent(ToggleButtonComponent toggleButtonComponent, BooleanFormFieldComponent booleanFormFieldComponent)
        {
            transform().heightProperty().bindProperty(booleanFormFieldComponent.buttonSizeProperty());
            transform().widthProperty().bindProperty(transform().heightProperty());

            mark = new Rectangle();

            get(StyleComponent.class).styleClass().add("box");
            mark.get(StyleComponent.class).styleClass().add("mark");

            transform().addChild(mark.transform());
            RelativeBindingHelper.bindToCenter(mark.transform(), transform());

            mark.transform().widthProperty().bindProperty(mark.transform().heightProperty());
            mark.transform().heightProperty().bindProperty(transform().heightProperty().map(height -> height - 4));

            mark.visibleProperty().bindProperty(toggleButtonComponent.getSelectedProperty());
        }

        public GuiElement getMark()
        {
            return mark;
        }

        @Override
        public String type()
        {
            return "radio-button-box";
        }
    }
}