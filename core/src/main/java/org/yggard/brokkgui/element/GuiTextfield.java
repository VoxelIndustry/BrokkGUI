package org.yggard.brokkgui.element;

import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.behavior.GuiTextfieldBehavior;
import org.yggard.brokkgui.behavior.ITextInput;
import org.yggard.brokkgui.control.GuiControl;
import org.yggard.brokkgui.event.CursorMoveEvent;
import org.yggard.brokkgui.event.TextTypedEvent;
import org.yggard.brokkgui.skin.GuiSkinBase;
import org.yggard.brokkgui.skin.GuiTextfieldSkin;
import org.yggard.brokkgui.validation.BaseTextValidator;
import org.yggard.hermod.EventHandler;

import java.util.List;

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
        super("textfield");

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
        return this.getTextProperty().getValue();
    }

    @Override
    public void setText(final String text)
    {
        this.getTextProperty().setValue(text);
    }

    public String getPromptText()
    {
        return this.getPrompTextProperty().getValue();
    }

    public void setPromptText(final String text)
    {
        this.getPrompTextProperty().setValue(text);
    }

    @Override
    public boolean isEditable()
    {
        return this.getEditableProperty().getValue();
    }

    @Override
    public void setEditable(final boolean editable)
    {
        this.getEditableProperty().setValue(editable);
    }

    /**
     * @return an immutable list
     */
    public List<BaseTextValidator> getValidators()
    {
        return this.getValidatorsProperty().getValue();
    }

    public void addValidator(final BaseTextValidator validator)
    {
        this.getValidatorsProperty().add(validator);
    }

    public void removeValidator(final BaseTextValidator validator)
    {
        this.getValidatorsProperty().remove(validator);
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
        return this.getValidatedProperty().getValue();
    }

    public void setValid(final boolean valid)
    {
        this.getValidatedProperty().setValue(valid);
    }

    public int getMaxTextLength()
    {
        return this.getMaxTextLengthProperty().getValue();
    }

    /**
     * Set the maximum text length this Textfield will be able to contain. Setting
     * this value to -1 will allow infinite.
     *
     * @param length
     */
    public void setMaxTextLength(final int length)
    {
        this.getMaxTextLengthProperty().setValue(length);
    }

    public int getCursorPosition()
    {
        return this.getCursorPositionProperty().getValue();
    }

    public void setCursorPosition(final int cursorPos)
    {
        this.getCursorPositionProperty().setValue(cursorPos);
    }

    public boolean isPromptTextAlwaysDisplayed()
    {
        return this.getPromptTextAlwaysDisplayedProperty().getValue();
    }

    public void setPromptTextAlwaysDisplayed(final boolean always)
    {
        this.getPromptTextAlwaysDisplayedProperty().setValue(always);
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