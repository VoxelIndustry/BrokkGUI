package net.voxelindustry.brokkgui.element.input;

import net.voxelindustry.brokkgui.component.impl.BooleanFormFieldComponent;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.shape.Text;
import net.voxelindustry.brokkgui.style.StyleComponent;

public class GuiCheckbox extends GuiRadioButton
{
    public GuiCheckbox(String text)
    {
        super(text);

        buttonNode(new CheckboxButtonContent(this, booleanFormFieldComponent()));
    }

    public GuiCheckbox()
    {
        this("");
    }

    @Override
    public String type()
    {
        return "checkbox";
    }

    public static class CheckboxButtonContent extends Rectangle
    {
        private final Text mark;

        public CheckboxButtonContent(GuiCheckbox parent, BooleanFormFieldComponent booleanFormFieldComponent)
        {
            transform().heightProperty().bindProperty(booleanFormFieldComponent.buttonSizeProperty());
            transform().widthProperty().bindProperty(transform().heightProperty());

            mark = new Text("✔");

            get(StyleComponent.class).styleClass().add("box");
            mark.get(StyleComponent.class).styleClass().add("mark");

            transform().addChild(mark.transform());
            RelativeBindingHelper.bindToCenter(mark.transform(), transform());

            mark.transform().widthProperty().bindProperty(mark.transform().heightProperty());
            mark.transform().heightProperty().bindProperty(transform().heightProperty().map(height -> height - 2));

            mark.visibleProperty().bindProperty(parent.getSelectedProperty());
        }

        @Override
        public String type()
        {
            return "checkbox-button";
        }

        public Text getMark()
        {
            return mark;
        }
    }
}