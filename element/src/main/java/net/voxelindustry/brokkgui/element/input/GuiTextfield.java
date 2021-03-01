package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.binding.Binding;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.behavior.GuiTextfieldBehavior;
import net.voxelindustry.brokkgui.component.impl.TextInputComponent;
import net.voxelindustry.brokkgui.control.GuiSkinedElement;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;
import net.voxelindustry.brokkgui.skin.GuiTextfieldSkin;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.brokkgui.text.TextLayoutComponent;
import net.voxelindustry.brokkgui.text.TextOverflow;
import net.voxelindustry.brokkgui.text.TextSettings;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Ourten 2 oct. 2016
 */
public class GuiTextfield extends GuiSkinedElement
{
    private final Property<String> textProperty;

    private final Property<GuiLabel> promptTextLabelProperty           = new Property<>(null);
    private final Property<Boolean>  promptTextAlwaysDisplayedProperty = new Property<>(false);

    private final Property<Boolean> expandToTextProperty;

    private final Property<RectBox> textPaddingProperty;

    private TextComponent textComponent;

    public GuiTextfield(String text)
    {
        textProperty = new Property<>(text);
        textComponent.text(text);

        textPaddingProperty = new Property<>(new RectBox(2));

        expandToTextProperty = new Property<>(false);

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
                        .combine(getTextProperty(), (alwaysDisplay, textContent) -> alwaysDisplay || !StringUtils.isEmpty(textContent)));
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
        TextLayoutComponent textLayoutComponent = provide(TextLayoutComponent.class);
        TextInputComponent textInputComponent = provide(TextInputComponent.class);

        textComponent().textPadding(new RectBox(5));
        textLayoutComponent.textOverflow(TextOverflow.MASK);

        textComponent.textAlignment(RectAlignment.LEFT_CENTER);
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

    public TextComponent textComponent()
    {
        return textComponent;
    }

    public Property<String> getTextProperty()
    {
        return textProperty;
    }

    public Property<Boolean> getPromptTextAlwaysDisplayedProperty()
    {
        return promptTextAlwaysDisplayedProperty;
    }

    public Property<RectBox> getTextPaddingProperty()
    {
        return textPaddingProperty;
    }

    public Property<Boolean> getExpandToTextProperty()
    {
        return expandToTextProperty;
    }

    public Property<GuiLabel> promptTextLabelProperty()
    {
        return promptTextLabelProperty;
    }

    public String getText()
    {
        return getTextProperty().getValue();
    }

    public void setText(String text)
    {
        getTextProperty().setValue(text);
    }

    public boolean isPromptTextAlwaysDisplayed()
    {
        return getPromptTextAlwaysDisplayedProperty().getValue();
    }

    public void setPromptTextAlwaysDisplayed(boolean always)
    {
        getPromptTextAlwaysDisplayedProperty().setValue(always);
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
                TextSettings textSettings = ((GuiTextfieldSkin<?>) getSkin()).getText().textSettings();

                return Math.max(transform().height(),
                        BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth(getText(), textSettings)) +
                        getTextPadding().getLeft() + getTextPadding().getRight();
            }
        });
    }
}