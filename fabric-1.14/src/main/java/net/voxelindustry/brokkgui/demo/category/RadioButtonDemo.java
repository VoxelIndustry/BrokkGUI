package net.voxelindustry.brokkgui.demo.category;

import net.voxelindustry.brokkgui.element.input.GuiToggleGroup;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.data.Rotation;
import net.voxelindustry.brokkgui.element.input.GuiCheckbox;
import net.voxelindustry.brokkgui.element.input.GuiRadioButton;
import net.voxelindustry.brokkgui.panel.GuiAbsolutePane;
import net.voxelindustry.brokkgui.shape.Rectangle;

public class RadioButtonDemo extends GuiAbsolutePane
{
    public RadioButtonDemo()
    {
        Rectangle icon = new Rectangle(8, 8);
        icon.setStyle("color: red;");

        GuiRadioButton radioButton = new GuiRadioButton("Right 1");
        radioButton.getLabel().setIcon(icon);
        radioButton.setHeight(10);
        radioButton.setRotation(Rotation.build().fromCenter().angle(90).create());
        radioButton.setScale(2);

        GuiRadioButton radioButton2 = new GuiRadioButton("Nothing to see here 2");
        radioButton2.setHeight(10);
        radioButton2.setButtonSide(RectSide.RIGHT);
        radioButton2.setStyle("border-color: blue; border-width: 1;");

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
