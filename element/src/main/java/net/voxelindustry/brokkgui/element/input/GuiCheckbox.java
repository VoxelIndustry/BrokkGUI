package net.voxelindustry.brokkgui.element.input;

import net.voxelindustry.brokkgui.component.impl.BooleanFormFieldComponent;
import net.voxelindustry.brokkgui.control.GuiFather;
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

    public static class CheckboxButtonContent extends GuiFather
    {
        private final Rectangle box;
        private final Text      mark;

        public CheckboxButtonContent(GuiCheckbox parent, BooleanFormFieldComponent booleanFormFieldComponent)
        {
            transform().heightProperty().bindProperty(booleanFormFieldComponent.buttonSizeProperty());
            transform().widthProperty().bindProperty(transform().heightProperty());

            box = new Rectangle();
            mark = new Text("✔");

            box.get(StyleComponent.class).styleClass().add("box");
            mark.get(StyleComponent.class).styleClass().add("mark");

            addChild(mark);
            RelativeBindingHelper.bindToCenter(mark.transform(), transform());

            addChild(box);
            RelativeBindingHelper.bindToCenter(box.transform(), transform());

            box.transform().widthProperty().bindProperty(box.transform().heightProperty());
            box.transform().heightProperty().bindProperty(transform().heightProperty());

            mark.transform().widthProperty().bindProperty(mark.transform().heightProperty());
            mark.transform().heightProperty().bindProperty(box.transform().heightProperty().map(height -> height - 2));

            mark.visibleProperty().bindProperty(parent.getSelectedProperty());
        }

        @Override
        public String type()
        {
            return "checkbox-button";
        }

        public Rectangle getBox()
        {
            return box;
        }

        public Text getMark()
        {
            return mark;
        }
    }
}