package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.TextAssistComponent;
import net.voxelindustry.brokkgui.component.impl.TextInputComponent;
import net.voxelindustry.brokkgui.control.GuiLabeled;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.style.StyledElement;
import net.voxelindustry.brokkgui.text.GuiOverflow;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.brokkgui.text.TextLayoutComponent;

/**
 * @author Ourten 2 oct. 2016
 */
public class GuiTextField extends GuiElement implements StyledElement
{
    private TextComponent       textComponent;
    private TextLayoutComponent textLayoutComponent;
    private TextInputComponent  textInputComponent;
    private TextAssistComponent textAssistComponent;

    public GuiTextField(String text)
    {
        textComponent.text(text);
    }

    public GuiTextField()
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
        textAssistComponent = provide(TextAssistComponent.class);

        textComponent.textPadding(new RectBox(5));
        textLayoutComponent.textOverflow(GuiOverflow.HIDDEN);

        textComponent.textAlignment(RectAlignment.LEFT_CENTER);
    }

    @Override
    public String type()
    {
        return "text-field";
    }

    public boolean promptTextAlwaysDisplayed()
    {
        return textAssistComponent.promptTextAlwaysDisplayed();
    }

    public void promptTextAlwaysDisplayed(boolean always)
    {
        textAssistComponent.promptTextAlwaysDisplayed(always);
    }

    public GuiLabeled promptTextLabel()
    {
        return textAssistComponent.promptTextLabel();
    }

    public void promptTextLabel(GuiLabeled label)
    {
        textAssistComponent.promptTextLabel(label);
    }

    public String promptText()
    {
        return textAssistComponent.promptText();
    }

    public void promptText(String promptText)
    {
        textAssistComponent.promptText(promptText);
    }

    public GuiLabeled helperTextLabel()
    {
        return textAssistComponent.helperTextLabel();
    }

    public void helperTextLabel(GuiLabeled label)
    {
        textAssistComponent.helperTextLabel(label);
    }

    public String helperText()
    {
        return textAssistComponent.helperText();
    }

    public void helperText(String helperText)
    {
        textAssistComponent.helperText(helperText);
    }

    public GuiLabeled errorTextLabel()
    {
        return textAssistComponent.errorTextLabel();
    }

    public void errorTextLabel(GuiLabeled label)
    {
        textAssistComponent.errorTextLabel(label);
    }

    public String errorText()
    {
        return textAssistComponent.errorText();
    }

    public void errorText(String errorText)
    {
        textAssistComponent.errorText(errorText);
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

    public TextAssistComponent textAssistComponent()
    {
        return textAssistComponent;
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

    public Property<RectAlignment> textAlignmentProperty()
    {
        return textComponent.textAlignmentProperty();
    }

    public RectAlignment textAlignment()
    {
        return textComponent.textAlignment();
    }

    public void textAlignment(RectAlignment alignment)
    {
        textComponent.textAlignment(alignment);
    }
}