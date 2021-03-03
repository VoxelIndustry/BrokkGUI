package net.voxelindustry.brokkgui.element;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiLabeled;

public class GuiLabel extends GuiLabeled
{
    public GuiLabel(String text, GuiElement icon)
    {
        super(text, icon);
    }

    public GuiLabel(String text)
    {
        this(text, null);
    }

    public GuiLabel()
    {
        this("");
    }

    @Override
    public String type()
    {
        return "label";
    }
}