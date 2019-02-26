package net.voxelindustry.brokkgui.demo.category;

import net.voxelindustry.brokkgui.element.input.GuiTextfield;
import net.voxelindustry.brokkgui.element.input.GuiTextfieldComplete;
import net.voxelindustry.brokkgui.panel.GuiAbsolutePane;

import java.util.Arrays;

public class TextFieldDemo extends GuiAbsolutePane
{
    public TextFieldDemo()
    {
        GuiTextfield field = new GuiTextfield();
        field.setWidthRatio(1);
        field.setHeight(40);

        field.setStyle("border-color: black; border-width: 1;");
        this.addChild(field, 0, 0);

        GuiTextfieldComplete autocomplete = new GuiTextfieldComplete();
        autocomplete.setPromptText("Enter a color");
        autocomplete.setCharBeforeCompletion(1);
        autocomplete.setSuggestions(Arrays.asList("Red", "Blue", "Purple", "Magenta", "Yellow", "Pink", "Gray"));
        autocomplete.setWidthRatio(1);
        autocomplete.setHeight(40);
        autocomplete.setID("autocomplete");

        this.addChild(autocomplete, 0, 50);

        GuiTextfield expandField = new GuiTextfield();
        expandField.setHeight(20);
        expandField.setExpandToText(true);

        expandField.setStyle("border-color: black; border-width: 1;");
        this.addChild(expandField, 0, 120);
    }
}
