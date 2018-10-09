package org.yggard.brokkgui.demo.category;

import org.yggard.brokkgui.control.GuiToggleGroup;
import org.yggard.brokkgui.data.RectSide;
import org.yggard.brokkgui.element.GuiCheckbox;
import org.yggard.brokkgui.element.GuiRadioButton;
import org.yggard.brokkgui.panel.GuiAbsolutePane;
import org.yggard.brokkgui.shape.Rectangle;

public class RadioButtonDemo extends GuiAbsolutePane
{
    public RadioButtonDemo()
    {
        Rectangle icon = new Rectangle(8, 8);
        icon.setStyle("color: red;");

        GuiRadioButton radioButton = new GuiRadioButton("Right 1");
        radioButton.getLabel().setIcon(icon);
        radioButton.setHeight(10);


        GuiRadioButton radioButton2 = new GuiRadioButton("Nothing to see here 2");
        radioButton2.setHeight(10);
        radioButton2.setButtonSide(RectSide.RIGHT);
        radioButton2.setStyle("border-color: blue; border-thin: 1;");

        GuiCheckbox checkbox = new GuiCheckbox("Left 3");
        checkbox.setHeight(10);
        checkbox.setButtonSide(RectSide.LEFT);

        final GuiToggleGroup toggleGroup = new GuiToggleGroup();
        toggleGroup.setAllowNothing(true);

        toggleGroup.addButtons(radioButton, radioButton2, checkbox);

        this.addChild(radioButton, 0, 0);
        this.addChild(radioButton2, 0, 15);
        this.addChild(checkbox, 0, 30);
    }
}
