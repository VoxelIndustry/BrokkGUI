package net.voxelindustry.brokkgui.immediate;

import net.voxelindustry.brokkgui.component.GuiElement;

public class ImmediateGuiElement extends GuiElement
{
    public static final ImmediateGuiElement INSTANCE = new ImmediateGuiElement();

    private ImmediateGuiElement()
    {
        
    }

    @Override
    public String type()
    {
        return "immediate-dummy";
    }
}
