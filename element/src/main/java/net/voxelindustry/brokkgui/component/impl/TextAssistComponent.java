package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.property.specific.StringProperty;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiLabeled;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.brokkgui.validation.BaseTextValidator;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class TextAssistComponent extends GuiComponent
{
    private final Property<GuiLabeled> helperTextLabelProperty           = new Property<>();
    private final Property<GuiLabeled> promptTextLabelProperty           = new Property<>();
    private final Property<Boolean>    promptTextAlwaysDisplayedProperty = new Property<>(false);

    private final Property<GuiLabeled> errorTextLabelProperty = new Property<>();
    private final Property<Boolean>    validProperty          = new Property<>(true);
    private final StringProperty       validatedTextProperty  = new StringProperty();

    private final ListProperty<BaseTextValidator> validatorsProperty = new ListProperty<>(null);

    private TextComponent textComponent;

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        textComponent = element.get(TextComponent.class);
        textComponent.textProperty().addChangeListener(obs -> validate());
        validatorsProperty.addChangeListener(obs -> validate());

        var helperTextOffsetY = transform().heightProperty().combine(validProperty(), (height, isValid) ->
        {
            if (!isValid && !StringUtils.isEmpty(errorText()))
                return height.floatValue() + errorTextLabel().height();
            return height;
        });

        promptTextLabelProperty().addChangeListener(((observable, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                transform().removeChild(oldValue.transform());

                oldValue.transform().visibleProperty().unbind();
                oldValue.transform().widthProperty().unbind();
                oldValue.transform().heightProperty().unbind();
                oldValue.textComponent().removeTextPaddingProperty(textComponent.computedTextPaddingValue());
                oldValue.style().removeStyleClass("text-field-prompt-text");
            }
            if (newValue != null)
            {
                newValue.transform().visibleProperty().bindProperty(promptTextAlwaysDisplayedProperty
                        .combine(textComponent.textProperty(), (alwaysDisplay, textContent) -> alwaysDisplay || StringUtils.isEmpty(textContent)));
                newValue.style().addStyleClass("text-field-prompt-text");
                newValue.expandToText(false);
                newValue.transform().widthProperty().bindProperty(transform().widthProperty());
                newValue.transform().heightProperty().bindProperty(transform().heightProperty());
                newValue.textComponent().addTextPaddingProperty(textComponent.computedTextPaddingValue());

                transform().addChild(newValue.transform());

                RelativeBindingHelper.bindToPos(newValue.transform(), transform());
            }
        }));

        helperTextLabelProperty().addChangeListener(((observable, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                transform().removeChild(oldValue.transform());

                oldValue.transform().widthProperty().unbind();
                oldValue.style().removeStyleClass("text-field-helper-text");
            }
            if (newValue != null)
            {
                newValue.style().addStyleClass("text-field-helper-text");
                newValue.expandToText(false);
                newValue.transform().widthProperty().bindProperty(transform().widthProperty());

                transform().addChild(newValue.transform());
                RelativeBindingHelper.bindToPos(newValue.transform(), transform(), null, helperTextOffsetY);
            }
        }));

        errorTextLabelProperty().addChangeListener(((observable, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                transform().removeChild(oldValue.transform());

                oldValue.transform().widthProperty().unbind();
                oldValue.style().removeStyleClass("text-field-error-text");
            }
            if (newValue != null)
            {
                newValue.style().addStyleClass("text-field-error-text");
                newValue.expandToText(false);
                newValue.transform().widthProperty().bindProperty(transform().widthProperty());

                transform().addChild(newValue.transform());

                RelativeBindingHelper.bindToPos(newValue.transform(), transform(), null, transform().heightProperty());

                newValue.transform().visibleProperty().bindProperty(validProperty());
            }
        }));
    }

    public Property<Boolean> validProperty()
    {
        return validProperty;
    }

    public boolean valid()
    {
        return validProperty().getValue();
    }

    public void valid(boolean valid)
    {
        validProperty().setValue(valid);
    }

    public ListProperty<BaseTextValidator> validatorsProperty()
    {
        return validatorsProperty;
    }

    public List<BaseTextValidator> validators()
    {
        return validatorsProperty().getValue();
    }

    public void addValidator(BaseTextValidator validator)
    {
        validatorsProperty().add(validator);
    }

    public void removeValidator(BaseTextValidator validator)
    {
        validatorsProperty().remove(validator);
    }

    public void validate()
    {
        valid(true);
        if (!validators().isEmpty())
        {
            for (var validator : validators())
            {
                validator.setInvalid(false);
                validator.validate(textComponent.text());
                if (validator.isInvalid())
                    valid(false);
            }
        }

        if (valid())
            validatedTextProperty().setValue(textComponent.text());
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
        if (promptTextLabelProperty().isPresent())
            return promptTextLabel().text();
        return "";
    }

    public void promptText(String promptText)
    {
        promptTextLabel().text(promptText);
    }

    protected GuiLabeled createDefaultPromptLabel()
    {
        GuiLabel label = new GuiLabel();
        label.textAlignment(textComponent.textAlignment());
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
        if (helperTextLabelProperty().isPresent())
            return helperTextLabel().text();
        return "";
    }

    public void helperText(String helperText)
    {
        helperTextLabel().text(helperText);
    }

    protected GuiLabeled createDefaultHelperLabel()
    {
        GuiLabel label = new GuiLabel();
        label.textAlignment(RectAlignment.LEFT_CENTER);
        label.textPadding(textComponent.textPadding());
        return label;
    }

    public Property<GuiLabeled> errorTextLabelProperty()
    {
        return errorTextLabelProperty;
    }

    public GuiLabeled errorTextLabel()
    {
        if (!errorTextLabelProperty.isPresent())
            errorTextLabelProperty.setValue(createDefaultErrorLabel());
        return errorTextLabelProperty().getValue();
    }

    public void errorTextLabel(GuiLabeled label)
    {
        errorTextLabelProperty().setValue(label);
    }

    public String errorText()
    {
        if (errorTextLabelProperty().isPresent())
            return errorTextLabel().text();
        return "";
    }

    public void errorText(String errorText)
    {
        errorTextLabel().text(errorText);
    }

    protected GuiLabeled createDefaultErrorLabel()
    {
        GuiLabel label = new GuiLabel();
        label.textAlignment(RectAlignment.LEFT_CENTER);
        label.textPadding(textComponent.textPadding());
        return label;
    }

    public StringProperty validatedTextProperty()
    {
        return validatedTextProperty;
    }

    public String validatedText()
    {
        return validatedTextProperty().getValue();
    }
}
