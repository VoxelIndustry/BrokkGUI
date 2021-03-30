package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.TextInputComponent;
import net.voxelindustry.brokkgui.control.GuiLabeled;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.style.StyledElement;
import net.voxelindustry.brokkgui.text.GuiOverflow;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.brokkgui.text.TextLayoutComponent;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Ourten 2 oct. 2016
 */
public class GuiTextField extends GuiElement implements StyledElement
{
    private final Property<GuiLabeled> helperTextLabelProperty           = new Property<>();
    private final Property<GuiLabeled> promptTextLabelProperty           = new Property<>();
    private final Property<Boolean>    promptTextAlwaysDisplayedProperty = new Property<>(false);

    private TextComponent       textComponent;
    private TextLayoutComponent textLayoutComponent;
    private TextInputComponent  textInputComponent;

    public GuiTextField(String text)
    {
        textComponent.text(text);

        setFocusable(true);

        promptTextLabelProperty().addChangeListener(((observable, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                removeChild(oldValue);

                oldValue.transform().visibleProperty().unbind();
                oldValue.transform().widthProperty().unbind();
                oldValue.transform().heightProperty().unbind();
                oldValue.textComponent().removeTextPaddingProperty(textComponent.computedTextPaddingValue());
                oldValue.style().removeStyleClass("text-field-prompt-text");
            }
            if (newValue != null)
            {
                newValue.transform().visibleProperty().bindProperty(promptTextAlwaysDisplayedProperty
                        .combine(textProperty(), (alwaysDisplay, textContent) -> alwaysDisplay || StringUtils.isEmpty(textContent)));
                newValue.style().addStyleClass("text-field-prompt-text");
                newValue.expandToText(false);
                newValue.transform().widthProperty().bindProperty(transform().widthProperty());
                newValue.transform().heightProperty().bindProperty(transform().heightProperty());
                newValue.textComponent().addTextPaddingProperty(textComponent().computedTextPaddingValue());

                transform().addChild(newValue.transform());

                RelativeBindingHelper.bindToPos(newValue.transform(), transform());
            }
        }));

        helperTextLabelProperty().addChangeListener(((observable, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                removeChild(oldValue);

                oldValue.transform().widthProperty().unbind();
                oldValue.style().removeStyleClass("text-field-helper-text");
            }
            if (newValue != null)
            {
                newValue.style().addStyleClass("text-field-helper-text");
                newValue.expandToText(false);
                newValue.transform().widthProperty().bindProperty(transform().widthProperty());

                transform().addChild(newValue.transform());

                RelativeBindingHelper.bindToPos(newValue.transform(), transform(), null, transform().heightProperty());
            }
        }));
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

        textComponent.textPadding(new RectBox(5));
        textLayoutComponent.textOverflow(GuiOverflow.HIDDEN);

        textComponent.textAlignment(RectAlignment.LEFT_CENTER);
    }

    @Override
    public String type()
    {
        return "textfield";
    }

    public Property<Boolean> promptTextAlwaysDisplayedProperty()
    {
        return promptTextAlwaysDisplayedProperty;
    }

    public Property<GuiLabeled> promptTextLabelProperty()
    {
        return promptTextLabelProperty;
    }

    public boolean promptTextAlwaysDisplayed()
    {
        return promptTextAlwaysDisplayedProperty().getValue();
    }

    public void promptTextAlwaysDisplayed(boolean always)
    {
        promptTextAlwaysDisplayedProperty().setValue(always);
    }

    public GuiLabeled promptTextLabel()
    {
        if (!promptTextLabelProperty.isPresent())
            promptTextLabelProperty.setValue(createDefaultPromptLabel());
        return promptTextLabelProperty().getValue();
    }

    public void promptTextLabel(GuiLabeled label)
    {
        promptTextLabelProperty().setValue(label);
    }

    public String promptText()
    {
        return promptTextLabel().text();
    }

    public void promptText(String promptText)
    {
        promptTextLabel().text(promptText);
    }

    protected GuiLabeled createDefaultPromptLabel()
    {
        GuiLabel label = new GuiLabel();
        label.textAlignment(textAlignment());
        label.textPadding(RectBox.EMPTY);
        return label;
    }

    public Property<GuiLabeled> helperTextLabelProperty()
    {
        return helperTextLabelProperty;
    }

    public GuiLabeled helperTextLabel()
    {
        if (!helperTextLabelProperty.isPresent())
            helperTextLabelProperty.setValue(createDefaultHelperLabel());
        return helperTextLabelProperty().getValue();
    }

    public void helperTextLabel(GuiLabeled label)
    {
        helperTextLabelProperty().setValue(label);
    }

    public String helperText()
    {
        return helperTextLabel().text();
    }

    public void helperText(String helperText)
    {
        helperTextLabel().text(helperText);
    }

    protected GuiLabeled createDefaultHelperLabel()
    {
        GuiLabel label = new GuiLabel();
        label.textAlignment(RectAlignment.LEFT_CENTER);
        label.textPadding(textComponent().textPadding());
        return label;
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