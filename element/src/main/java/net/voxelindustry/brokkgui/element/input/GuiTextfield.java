package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.binding.Binding;
import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
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
    private final Property<String> textProperty;
    private final Property<String> promptTextProperty;
    private final Property<String> promptEllipsisProperty;

    private final Property<Boolean> promptTextAlwaysDisplayedProperty;
    private final Property<Boolean> editableProperty;
    private final Property<Boolean> validatedProperty;
    private final Property<Boolean> expandToTextProperty;

    private final Property<Integer> maxTextLengthProperty;
    private final Property<Integer> cursorPosProperty;
    private final Property<RectBox> textPaddingProperty;

    private final ListProperty<BaseTextValidator> validatorsProperty;

    private EventHandler<TextTypedEvent>  onTextTyped;
    private EventHandler<CursorMoveEvent> onCursorMoveEvent;

    public GuiTextfield(String text)
    {
        textProperty = new Property<>(text);
        promptTextProperty = new Property<>("");
        promptEllipsisProperty = new Property<>("...");

        maxTextLengthProperty = new Property<>(-1);
        cursorPosProperty = new Property<>(0);
        textPaddingProperty = new Property<>(new RectBox(2));

        promptTextAlwaysDisplayedProperty = new Property<>(false);
        editableProperty = new Property<>(true);
        validatedProperty = new Property<>(true);

        expandToTextProperty = new Property<>(false);

        validatorsProperty = new ListProperty<>(null);

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

    public Property<String> getTextProperty()
    {
        return textProperty;
    }

    public Property<String> getPrompTextProperty()
    {
        return promptTextProperty;
    }

    public Property<Boolean> getEditableProperty()
    {
        return editableProperty;
    }

    public ListProperty<BaseTextValidator> getValidatorsProperty()
    {
        return validatorsProperty;
    }

    public Property<Boolean> getValidatedProperty()
    {
        return validatedProperty;
    }

    public Property<Boolean> getPromptTextAlwaysDisplayedProperty()
    {
        return promptTextAlwaysDisplayedProperty;
    }

    public Property<Integer> getMaxTextLengthProperty()
    {
        return maxTextLengthProperty;
    }

    public Property<Integer> getCursorPosProperty()
    {
        return cursorPosProperty;
    }

    public Property<RectBox> getTextPaddingProperty()
    {
        return textPaddingProperty;
    }

    public Property<String> getPromptEllipsisProperty()
    {
        return promptEllipsisProperty;
    }

    public Property<Boolean> getExpandToTextProperty()
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
        transform().widthProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(getTextProperty(),
                        getTextPaddingProperty(), transform().heightProperty());
            }

            @Override
            public Float computeValue()
            {
                return Math.max(transform().height(),
                        Math.max(BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth(getPromptText()),
                                BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth(getText())) +
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