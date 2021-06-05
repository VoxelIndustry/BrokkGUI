package net.voxelindustry.brokkgui.shape;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.brokkgui.text.TextSettings;

public class Text extends GuiElement
{
    private TextComponent textComponent;

    public Text(String text)
    {
        textComponent.text(text);
    }

    public Text()
    {
        this("");
    }

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        textComponent = provide(TextComponent.class);
    }

    @Override
    public String type()
    {
        return "text";
    }

    public Property<String> textProperty()
    {
        return textComponent.textProperty();
    }

    public String text()
    {
        return textComponent.text();
    }

    public void text(String text)
    {
        textComponent.text(text);
    }

    public TextSettings textSettings()
    {
        return textComponent.textSettings();
    }
}
