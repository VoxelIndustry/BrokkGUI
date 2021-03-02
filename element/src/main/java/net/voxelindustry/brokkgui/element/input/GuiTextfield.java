package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.impl.TextInputComponent;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.brokkgui.text.TextLayoutComponent;
import net.voxelindustry.brokkgui.text.TextOverflow;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Ourten 2 oct. 2016
 */
public class GuiTextfield extends GuiFather
{
    private final Property<GuiLabel> promptTextLabelProperty           = new Property<>(null);
    private final Property<Boolean>  promptTextAlwaysDisplayedProperty = new Property<>(false);

    private TextComponent       textComponent;
    private TextLayoutComponent textLayoutComponent;
    private TextInputComponent  textInputComponent;

    public GuiTextfield(String text)
    {
        textComponent.text(text);

        setFocusable(true);

        promptTextLabelProperty().addListener(((observable, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                removeChild(oldValue);
                oldValue.visibleProperty().unbind();
                oldValue.style().removeStyleClass("prompt");
            }
            if (newValue != null)
            {
                newValue.visibleProperty().bindProperty(promptTextAlwaysDisplayedProperty
                        .combine(textProperty(), (alwaysDisplay, textContent) -> alwaysDisplay || !StringUtils.isEmpty(textContent)));
                newValue.style().addStyleClass("prompt");
                addChild(newValue);
            }
        }));
    }

    public GuiTextfield()
    {
        this("");
    }

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        textComponent = provide(TextComponent.class);
        textLayoutComponent = provide(TextLayoutComponent.class);
        textInputComponent = provide(TextInputComponent.class);

        textComponent.textPadding(new RectBox(5));
        textLayoutComponent.textOverflow(TextOverflow.MASK);

        textComponent.textAlignment(RectAlignment.LEFT_CENTER);
    }

    @Override
    public String type()
    {
        return "textfield";
    }

    public Property<Boolean> getPromptTextAlwaysDisplayedProperty()
    {
        return promptTextAlwaysDisplayedProperty;
    }

    public Property<GuiLabel> promptTextLabelProperty()
    {
        return promptTextLabelProperty;
    }

    public boolean isPromptTextAlwaysDisplayed()
    {
        return getPromptTextAlwaysDisplayedProperty().getValue();
    }

    public void setPromptTextAlwaysDisplayed(boolean always)
    {
        getPromptTextAlwaysDisplayedProperty().setValue(always);
    }

    public GuiLabel promptTextLabel()
    {
        if (!promptTextLabelProperty.isPresent())
            promptTextLabelProperty.setValue(new GuiLabel());
        return promptTextLabelProperty().getValue();
    }

    public void promptTextLabel(GuiLabel label)
    {
        promptTextLabelProperty().setValue(label);
    }

    public String promptText()
    {
        return promptTextLabel().getText();
    }

    public void promptText(String promptText)
    {
        promptTextLabel().setText(promptText);
    }

    ////////////////
    // COMPONENTS //
    ////////////////

    public TextComponent textComponent()
    {
        return textComponent;
    }

    public TextLayoutComponent textLayoutComponent()
    {
        return textLayoutComponent;
    }

    public TextInputComponent textInputComponent()
    {
        return textInputComponent;
    }

    ///////////////
    // DELEGATES //
    ///////////////

    public Property<Boolean> expandToTextProperty()
    {
        return textLayoutComponent.expandToTextProperty();
    }

    public boolean expandToText()
    {
        return textLayoutComponent.expandToText();
    }

    public void expandToText(boolean expandToText)
    {
        textLayoutComponent.expandToText(expandToText);
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
}