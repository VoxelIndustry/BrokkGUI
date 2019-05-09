package net.voxelindustry.brokkgui.element;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.Text;
import net.voxelindustry.brokkgui.component.TextRenderer;

public class Label extends GuiElement
{
    public Label()
    {
        add(Text.class);
        add(TextRenderer.class);
    }

    @Override
    public String getType()
    {
        return "label";
    }
}
