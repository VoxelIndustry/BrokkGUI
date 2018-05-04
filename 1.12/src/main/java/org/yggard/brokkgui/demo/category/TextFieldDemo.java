package org.yggard.brokkgui.demo.category;

import org.yggard.brokkgui.element.GuiTextfield;
import org.yggard.brokkgui.element.GuiTextfieldComplete;
import org.yggard.brokkgui.panel.GuiAbsolutePane;

import java.util.Arrays;

public class TextFieldDemo extends GuiAbsolutePane
{
    public TextFieldDemo()
    {
        GuiTextfield field = new GuiTextfield();
        field.setWidthRatio(1);
        field.setHeight(40);

        field.setStyle("-border-color: black; -border-thin: 1;");
        this.addChild(field, 0, 0);

        GuiTextfieldComplete autocomplete = new GuiTextfieldComplete();
        autocomplete.setPromptText("Enter a color");
        autocomplete.setCharBeforeCompletion(1);
        autocomplete.setSuggestions(Arrays.asList("Red", "Blue", "Purple", "Magenta", "Yellow", "Pink", "Gray"));
        autocomplete.setWidthRatio(1);
        autocomplete.setHeight(40);
        autocomplete.setID("autocomplete");

        this.addChild(autocomplete, 0, 50);
    }
}
