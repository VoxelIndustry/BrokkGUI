package net.voxelindustry.brokkgui.shape;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiElement;

public class Text extends GuiElement
{
    private final String        startingText;
    private       TextComponent textComponent;

    public Text(String text)
    {
        startingText = text;
    }

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        textComponent = provide(TextComponent.class);
        textComponent.text(startingText);
    }

    @Override
    public String type()
    {
        return "text";
    }

    public BaseProperty<String> textProperty()
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
}
