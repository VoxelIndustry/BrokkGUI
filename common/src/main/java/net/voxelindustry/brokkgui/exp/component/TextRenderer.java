package net.voxelindustry.brokkgui.exp.component;

public class TextRenderer extends GuiComponent
{
    public TextRenderer()
    {

    }

    @Override
    void attach(GuiElement element)
    {
        super.attach(element);

        if (!element.has(Text.class))
            throw new GuiComponentException(
                    "TextRenderer must be added to a GuiElement already containing the component Text!");
    }
}
