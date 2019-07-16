package net.voxelindustry.brokkgui.style;

import net.voxelindustry.brokkgui.component.GuiElement;

public class DummyGuiElement extends GuiElement
{
    private String type;

    public DummyGuiElement(String type)
    {
        this.add(StyleHolder.class);
        this.type = type;
    }

    @Override
    public String type()
    {
        return this.type;
    }
}
