package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.behavior.GuiTextfieldBehavior;
import net.voxelindustry.brokkgui.component.ITextInput;
import net.voxelindustry.brokkgui.control.GuiSkinedElement;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.event.CursorMoveEvent;
import net.voxelindustry.brokkgui.event.TextTypedEvent;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;
import net.voxelindustry.brokkgui.skin.GuiTextfieldSkin;
import net.voxelindustry.brokkgui.validation.BaseTextValidator;
import net.voxelindustry.hermod.EventHandler;

import java.util.List;

/**
 * @author Ourten 2 oct. 2016
 */
public class GuiTextfield extends GuiSkinedElement implements ITextInput
{
    private final BaseProperty<String> textProperty;
    private final BaseProperty<String> promptTextProperty;
    private final BaseProperty<String> promptEllipsisProperty;

    private final BaseProperty<Boolean> promptTextAlwaysDisplayedProperty;
    private final BaseProperty<Boolean> editableProperty;
    private final BaseProperty<Boolean> validatedProperty;
    private final BaseProperty<Boolean> expandToTextProperty;

    private final BaseProperty<Integer> maxTextLengthProperty;
    private final BaseProperty<Integer> cursorPosProperty;
    private final BaseProperty<RectBox> textPaddingProperty;

    private final BaseListProperty<BaseTextValidator> validatorsProperty;

    private EventHandler<TextTypedEvent>  onTextTyped;
    private EventHandler<CursorMoveEvent> onCursorMoveEvent;

    public GuiTextfield(String text)
    {
        textProperty = new BaseProperty<>(text, "textProperty");
        promptTextProperty = new BaseProperty<>("", "promptTextProperty");
        promptEllipsisProperty = new BaseProperty<>("...", "promptEllipsisProperty");

        maxTextLengthProperty = new BaseProperty<>(-1, "maxTextLength");
        cursorPosProperty = new BaseProperty<>(0, "cursorPosProperty");
        textPaddingProperty = new BaseProperty<>(new RectBox(2), "textPaddingProperty");

        promptTextAlwaysDisplayedProperty = new BaseProperty<>(false, "promptTextAlwaysDisplayedProperty");
        editableProperty = new BaseProperty<>(true, "editableProperty");
        validatedProperty = new BaseProperty<>(true, "validatedProperty");

        expandToTextProperty = new BaseProperty<>(false, "expandToTextProperty");

        validatorsProperty = new BaseListProperty<>(null, "validatorsProperty");

        setFocusable(true);
    }

    public GuiTextfield()
    {
        this("");
    }

    @Override
    public String type()
    {
        return "textfield";
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiTextfieldSkin<>(this, new GuiTextfieldBehavior<>(this));
    }

    public BaseProperty<String> getTextProperty()
    {
        return textProperty;
    }

    public BaseProperty<String> getPrompTextProperty()
    {
        return promptTextProperty;
    }

    public BaseProperty<Boolean> getEditableProperty()
    {
        return editableProperty;
    }

    public BaseListProperty<BaseTextValidator> getValidatorsProperty()
    {
        return validatorsProperty;
    }

    public BaseProperty<Boolean> getValidatedProperty()
    {
        return validatedProperty;
    }

    public BaseProperty<Boolean> getPromptTextAlwaysDisplayedProperty()
    {
        return promptTextAlwaysDisplayedProperty;
    }

    public BaseProperty<Integer> getMaxTextLengthProperty()
    {
        return maxTextLengthProperty;
    }

    public BaseProperty<Integer> getCursorPosProperty()
    {
        return cursorPosProperty;
    }

    public BaseProperty<RectBox> getTextPaddingProperty()
    {
        return textPaddingProperty;
    }

    public BaseProperty<String> getPromptEllipsisProperty()
    {
        return promptEllipsisProperty;
    }

    public BaseProperty<Boolean> getExpandToTextProperty()
    {
        return expandToTextProperty;
    }

    @Override
    public String getText()
    {
        return getTextProperty().getValue();
    }

    @Override
    public void setText(String text)
    {
        getTextProperty().setValue(text);
    }

    public String getPromptText()
    {
        return getPrompTextProperty().getValue();
    }

    public void setPromptText(String text)
    {
        getPrompTextProperty().setValue(text);
    }

    @Override
    public boolean isEditable()
    {
        return getEditableProperty().getValue();
    }

    @Override
    public void setEditable(boolean editable)
    {
        getEditableProperty().setValue(editable);
    }

    /**
     * @return an immutable list
     */
    public List<BaseTextValidator> getValidators()
    {
        return getValidatorsProperty().getValue();
    }

    public void addValidator(BaseTextValidator validator)
    {
        getValidatorsProperty().add(validator);
    }

    public void removeValidator(BaseTextValidator validator)
    {
        getValidatorsProperty().remove(validator);
    }

    @Override
    public void validate()
    {
        setValid(true);
        if (!getValidators().isEmpty())
            getValidators().forEach(validator ->
            {
                validator.setErrored(false);
                validator.validate(getText());
                if (validator.isErrored())
                    setValid(false);
            });
    }

    public boolean isValid()
    {
        return getValidatedProperty().getValue();
    }

    public void setValid(boolean valid)
    {
        getValidatedProperty().setValue(valid);
    }

    public int getMaxTextLength()
    {
        return getMaxTextLengthProperty().getValue();
    }

    /**
     * Set the maximum text length this Textfield will be able to contain. Setting
     * this value to -1 will allow infinite.
     *
     * @param length
     */
    public void setMaxTextLength(int length)
    {
        getMaxTextLengthProperty().setValue(length);
    }

    public int getCursorPos()
    {
        return getCursorPosProperty().getValue();
    }

    public void setCursorPos(int cursorPos)
    {
        if (cursorPos >= 0 && cursorPos <= getText().length())
        {
            if (getOnCursorMoveEvent() != null)
                getEventDispatcher().dispatchEvent(CursorMoveEvent.TYPE,
                        new CursorMoveEvent(this, getCursorPos(), cursorPos));
            getCursorPosProperty().setValue(cursorPos);
        }
    }

    public boolean isPromptTextAlwaysDisplayed()
    {
        return getPromptTextAlwaysDisplayedProperty().getValue();
    }

    public void setPromptTextAlwaysDisplayed(boolean always)
    {
        getPromptTextAlwaysDisplayedProperty().setValue(always);
    }

    public void setPromptEllipsis(String ellipsis)
    {
        getPromptEllipsisProperty().setValue(ellipsis);
    }

    public String getPromptEllipsis()
    {
        return getPromptEllipsisProperty().getValue();
    }

    public void setTextPadding(RectBox padding)
    {
        getTextPaddingProperty().setValue(padding);
    }

    public RectBox getTextPadding()
    {
        return getTextPaddingProperty().getValue();
    }

    public boolean expandToText()
    {
        return expandToTextProperty.getValue();
    }

    public void setExpandToText(boolean expandToText)
    {
        if (expandToText && !expandToText())
            bindSizeToText();
        else if (!expandToText && expandToText())
            transform().widthProperty().unbind();
        expandToTextProperty.setValue(expandToText);
    }

    private void bindSizeToText()
    {
        transform().widthProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getTextProperty(),
                        getTextPaddingProperty(), transform().heightProperty());
            }

            @Override
            public Float computeValue()
            {
                return Math.max(transform().height(),
                        Math.max(BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getPromptText()),
                                BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getText())) +
                                getTextPadding().getLeft() + getTextPadding().getRight());
            }
        });
    }

    ////////////
    // EVENTS //
    ////////////

    public EventHandler<TextTypedEvent> getOnTextTyped()
    {
        return onTextTyped;
    }

    public void setOnTextTyped(EventHandler<TextTypedEvent> onTextTyped)
    {
        getEventDispatcher().removeHandler(TextTypedEvent.TYPE, this.onTextTyped);
        this.onTextTyped = onTextTyped;
        getEventDispatcher().addHandler(TextTypedEvent.TYPE, this.onTextTyped);
    }

    public EventHandler<CursorMoveEvent> getOnCursorMoveEvent()
    {
        return onCursorMoveEvent;
    }

    public void setOnCursorMoveEvent(EventHandler<CursorMoveEvent> onCursorMoveEvent)
    {
        getEventDispatcher().removeHandler(CursorMoveEvent.TYPE, this.onCursorMoveEvent);
        this.onCursorMoveEvent = onCursorMoveEvent;
        getEventDispatcher().addHandler(CursorMoveEvent.TYPE, this.onCursorMoveEvent);
    }
}