package fr.ourten.brokkgui.element;

import com.google.common.collect.ImmutableList;

import fr.ourten.brokkgui.behavior.GuiTextfieldBehavior;
import fr.ourten.brokkgui.behavior.ITextInput;
import fr.ourten.brokkgui.control.GuiControl;
import fr.ourten.brokkgui.event.CursorMoveEvent;
import fr.ourten.brokkgui.event.EventHandler;
import fr.ourten.brokkgui.event.TextTypedEvent;
import fr.ourten.brokkgui.skin.GuiSkinBase;
import fr.ourten.brokkgui.skin.GuiTextfieldSkin;
import fr.ourten.brokkgui.validation.BaseTextValidator;
import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;

/**
 * @author Ourten 2 oct. 2016
 */
public class GuiTextfield extends GuiControl implements ITextInput
{
    private final BaseProperty<String>                textProperty, promptTextProperty;

    private final BaseProperty<Boolean>               promptTextAlwaysDisplayedProperty, editableProperty,
            validatedProperty;

    private final BaseProperty<Integer>               maxTextLengthProperty;
    private final BaseProperty<Integer>               cursorPositionProperty;

    private final BaseListProperty<BaseTextValidator> validatorsProperty;

    private EventHandler<TextTypedEvent>              onTextTyped;
    private EventHandler<CursorMoveEvent>             onCursorMoveEvent;

    public GuiTextfield(final String text)
    {
        this.textProperty = new BaseProperty<>(text, "textProperty");
        this.promptTextProperty = new BaseProperty<>("", "promptTextProperty");

        this.maxTextLengthProperty = new BaseProperty<>(-1, "maxTextLength");
        this.cursorPositionProperty = new BaseProperty<>(0, "cursorPositionProperty");

        this.promptTextAlwaysDisplayedProperty = new BaseProperty<>(false, "promptTextAlwaysDisplayedProperty");
        this.editableProperty = new BaseProperty<>(true, "editableProperty");
        this.validatedProperty = new BaseProperty<>(true, "validatedProperty");

        this.validatorsProperty = new BaseListProperty<>(null, "validatorsProperty");

        this.setFocusable(true);
    }

    public GuiTextfield()
    {
        this("");
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiTextfieldSkin<>(this, new GuiTextfieldBehavior<>(this));
    }

    public BaseProperty<String> getTextProperty()
    {
        return this.textProperty;
    }

    public BaseProperty<String> getPrompTextProperty()
    {
        return this.promptTextProperty;
    }

    public BaseProperty<Boolean> getEditableProperty()
    {
        return this.editableProperty;
    }

    public BaseListProperty<BaseTextValidator> getValidatorsProperty()
    {
        return this.validatorsProperty;
    }

    public BaseProperty<Boolean> getValidatedProperty()
    {
        return this.validatedProperty;
    }

    public BaseProperty<Boolean> getPromptTextAlwaysDisplayedProperty()
    {
        return this.promptTextAlwaysDisplayedProperty;
    }

    public BaseProperty<Integer> getMaxTextLengthProperty()
    {
        return this.maxTextLengthProperty;
    }

    public BaseProperty<Integer> getCursorPositionProperty()
    {
        return this.cursorPositionProperty;
    }

    @Override
    public String getText()
    {
        return this.textProperty.getValue();
    }

    @Override
    public void setText(final String text)
    {
        this.textProperty.setValue(text);
    }

    public String getPromptText()
    {
        return this.promptTextProperty.getValue();
    }

    public void setPromptText(final String text)
    {
        this.promptTextProperty.setValue(text);
    }

    @Override
    public boolean isEditable()
    {
        return this.editableProperty.getValue();
    }

    @Override
    public void setEditable(final boolean editable)
    {
        this.editableProperty.setValue(editable);
    }

    public ImmutableList<BaseTextValidator> getValidators()
    {
        return this.validatorsProperty.getValue();
    }

    public void addValidator(final BaseTextValidator validator)
    {
        this.validatorsProperty.add(validator);
    }

    public void removeValidator(final BaseTextValidator validator)
    {
        this.validatorsProperty.remove(validator);
    }

    @Override
    public void validate()
    {
        this.setValid(true);
        if (!this.getValidators().isEmpty())
            this.getValidators().forEach(validator ->
            {
                validator.setErrored(false);
                validator.validate(this.getText());
                if (validator.isErrored())
                    this.setValid(false);
            });
    }

    public boolean isValid()
    {
        return this.validatedProperty.getValue();
    }

    public void setValid(final boolean valid)
    {
        this.validatedProperty.setValue(valid);
    }

    public int getMaxTextLength()
    {
        return this.maxTextLengthProperty.getValue();
    }

    /**
     * Set the maximum text length this Textfield will be able to contain.
     * Setting this value to -1 will allow infinite.
     * 
     * @param length
     */
    public void setMaxTextLength(final int length)
    {
        this.maxTextLengthProperty.setValue(length);
    }

    public int getCursorPosition()
    {
        return this.cursorPositionProperty.getValue();
    }

    public void setCursorPosition(final int cursorPos)
    {
        this.cursorPositionProperty.setValue(cursorPos);
    }

    public boolean isPromptTextAlwaysDisplayed()
    {
        return this.promptTextAlwaysDisplayedProperty.getValue();
    }

    public void setPromptTextAlwaysDisplayed(final boolean always)
    {
        this.promptTextAlwaysDisplayedProperty.setValue(always);
    }

    ////////////
    // EVENTS //
    ////////////

    public EventHandler<TextTypedEvent> getOnTextTyped()
    {
        return this.onTextTyped;
    }

    public void setOnTextTyped(final EventHandler<TextTypedEvent> onTextTyped)
    {
        this.onTextTyped = onTextTyped;
    }

    public EventHandler<CursorMoveEvent> getOnCursorMoveEvent()
    {
        return this.onCursorMoveEvent;
    }

    public void setOnCursorMoveEvent(final EventHandler<CursorMoveEvent> onCursorMoveEvent)
    {
        this.onCursorMoveEvent = onCursorMoveEvent;
    }
}