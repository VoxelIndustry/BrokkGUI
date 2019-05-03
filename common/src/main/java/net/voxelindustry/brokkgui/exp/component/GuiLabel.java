package net.voxelindustry.brokkgui.exp.component;

public class GuiLabel extends GuiElement
{
    public GuiLabel()
    {
        add(Text.class);
        add(TextRenderer.class);
    }

    @Override
    protected String getType()
    {
        return "label";
    }
}
