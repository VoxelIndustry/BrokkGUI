package net.voxelindustry.brokkgui.element.input;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiButtonBase;

public class GuiButton extends GuiButtonBase
{
    public GuiButton(String text, GuiElement icon)
    {
        super(text, icon);
    }

    public GuiButton(String text)
    {
        this(text, null);
    }

    public GuiButton()
    {
        this("");
    }

    @Override
    public String type()
    {
        return "button";
    }
}