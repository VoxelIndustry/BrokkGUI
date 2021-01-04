package net.voxelindustry.brokkgui.skin;

import fr.ourten.teabeans.binding.Binding;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.behavior.GuiTextfieldBehavior;
import net.voxelindustry.brokkgui.element.input.GuiTextfield;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.internal.ITextHelper;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.shape.Text;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.text.TextSettings;
import net.voxelindustry.brokkgui.validation.BaseTextValidator;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Ourten 2 oct. 2016
 */
public class GuiTextfieldSkin<T extends GuiTextfield> extends GuiBehaviorSkinBase<T, GuiTextfieldBehavior<T>>
{
    private final Property<String>  ellipsedPromptProperty;
    private final Property<Integer> displayOffsetProperty;
    // Here to keep last value of the displayOffsetProperty
    private       int               displayOffset = 0;
    private final Property<String>  displayedTextProperty;

    private final TextSettings errorTextSettings = TextSettings.build()
            .fontName("default")
            .textColor(Color.RED)
            .create();

    private final Text text;
    private final Text promptText;

    public GuiTextfieldSkin(T model, GuiTextfieldBehavior<T> behaviour)
    {
        super(model, behaviour);

        ellipsedPromptProperty = new Property<>("");
        displayOffsetProperty = new Property<>(0);
        displayedTextProperty = new Property<>("");

        bindDisplayText();
        text = new Text(model.getText());
        promptText = new Text(model.getPromptText());

        text.get(StyleComponent.class).styleClass().add("text");
        promptText.get(StyleComponent.class).styleClass().add("prompt");
        getModel().get(StyleComponent.class).registerProperty("cursor-color", Color.WHITE.shade(0.3f), Color.class);

        text.textProperty().bindProperty(displayedTextProperty);

        text.transform().xPosProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(transform().xPosProperty(), transform().xTranslateProperty(),
                        getModel().getTextPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                return getModel().getLeftPos() + getModel().getTextPadding().getLeft();
            }
        });
        text.transform().yPosProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(transform().yPosProperty(), transform().yTranslateProperty(),
                        getModel().getTextPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                return getModel().getTopPos() + getModel().getTextPadding().getTop();
            }
        });
        text.transform().widthProperty().bindProperty(displayedTextProperty.map(displayText -> BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth(displayText, text.textSettings())));
        text.transform().height(BrokkGuiPlatform.getInstance().getTextHelper().getStringHeight(text.textSettings()));

        getModel().addChild(text);

        promptText.textProperty().bindProperty(ellipsedPromptProperty);

        promptText.transform().xPosProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(transform().xPosProperty(), transform().xTranslateProperty(),
                        getModel().getTextPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                return getModel().getLeftPos() + getModel().getTextPadding().getLeft();
            }
        });
        promptText.transform().yPosProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(transform().yPosProperty(), transform().yTranslateProperty(),
                        getModel().getTextPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                return getModel().getTopPos() + getModel().getTextPadding().getTop();
            }
        });
        promptText.transform().widthProperty().bindProperty(ellipsedPromptProperty.map(ellipsedPromptText -> BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth(ellipsedPromptText, promptText.textSettings())));
        promptText.transform().height(BrokkGuiPlatform.getInstance().getTextHelper().getStringHeight(promptText.textSettings()));

        promptText.visibleProperty().bindProperty(new Binding<Boolean>()
        {
            {
                super.bind(getModel().getPrompTextProperty(), getModel().getPromptTextAlwaysDisplayedProperty(),
                        getModel().getTextProperty());
            }

            @Override
            public Boolean computeValue()
            {
                return !StringUtils.isEmpty(getModel().getPromptText())
                        && (getModel().isPromptTextAlwaysDisplayed()
                        || StringUtils.isEmpty(getModel().getText()));
            }
        });

        getModel().addChild(promptText);
    }

    @Override
    public void render(RenderPass pass, IRenderCommandReceiver renderer, int mouseX, int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);

        if (pass == RenderPass.FOREGROUND)
        {
            float x = getModel().getLeftPos();
            float y = getModel().getTopPos();
            float xPadding = getModel().getTextPadding().getLeft();
            float yPadding = getModel().getTextPadding().getTop();

            // Cursor
            if (getModel().focusedProperty().getValue())
            {
                renderer.drawColoredRect(renderer,
                        x + xPadding - 1 + BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth(
                                getModel().getText().substring(displayOffsetProperty.getValue(),
                                        getModel().getCursorPos()), text.textSettings()), y + yPadding - 1, 1,
                        BrokkGuiPlatform.getInstance().getTextHelper().getStringHeight(text.textSettings()) + 1, getModel().transform().zLevel() + 1, getCursorColor(), RenderPass.FOREGROUND);
            }
        }
        else if (pass == RenderPass.HOVER)
            if (!getModel().isValid())
            {
                int i = 0;
                for (BaseTextValidator validator : getModel().getValidators())
                    if (validator.isErrored())
                    {
                        renderer.drawString(validator.getMessage(),
                                getModel().getLeftPos(),
                                getModel().getTopPos()
                                        + getModel().height()
                                        + i * (BrokkGuiPlatform.getInstance().getTextHelper().getStringHeight(errorTextSettings) + 1),
                                getModel().transform().zLevel(), RenderPass.HOVER, errorTextSettings);
                        i++;
                    }
            }
    }

    private void bindDisplayText()
    {
        ellipsedPromptProperty.bindProperty(new Binding<String>()
        {
            {
                super.bind(getModel().getPrompTextProperty(), getModel().getPromptEllipsisProperty(),
                        getModel().getTextPaddingProperty(), transform().widthProperty());
            }

            @Override
            public String computeValue()
            {
                return trimTextToWidth(getModel().getPromptText(), getModel().getPromptEllipsis(),
                        (int) (getModel().width() - getModel().getTextPadding().getLeft() - getModel().getTextPadding().getRight()),
                        BrokkGuiPlatform.getInstance().getTextHelper());
            }

        });

        displayOffsetProperty.bindProperty(new Binding<Integer>()
        {
            {
                super.bind(getModel().getCursorPosProperty(), getModel().getTextPaddingProperty(),
                        transform().widthProperty(), getModel().getExpandToTextProperty());
            }

            @Override
            public Integer computeValue()
            {
                if (getModel().expandToText())
                    return 0;

                if (getModel().getCursorPos() <= displayOffset)
                {
                    displayOffset = getModel().getCursorPos();
                    return displayOffset;
                }
                ITextHelper helper = BrokkGuiPlatform.getInstance().getTextHelper();
                while (helper.getStringWidth(getModel().getText().substring(displayOffset, getModel().getCursorPos()), text.textSettings())
                        > getModel().width() - getModel().getTextPadding().getLeft() - getModel().getTextPadding().getRight())
                {
                    displayOffset++;
                }
                displayOffset = Math.max(0, displayOffset);
                return displayOffset;
            }

        });

        displayedTextProperty.bindProperty(new Binding<String>()
        {
            {
                super.bind(displayOffsetProperty, getModel().getTextProperty(), transform().widthProperty(),
                        getModel().getTextPaddingProperty(), getModel().getExpandToTextProperty());
            }

            @Override
            public String computeValue()
            {
                String rightPart = getModel().getText().substring(displayOffsetProperty.getValue());

                if (getModel().expandToText())
                    return getModel().getText();
                if (StringUtils.isEmpty(rightPart))
                    return rightPart;

                ITextHelper helper = BrokkGuiPlatform.getInstance().getTextHelper();
                while (helper.getStringWidth(rightPart, text.textSettings()) > getModel().width()
                        - getModel().getTextPadding().getLeft() - getModel().getTextPadding().getRight())
                {
                    rightPart = rightPart.substring(0, rightPart.length() - 1);
                }
                return rightPart;
            }
        });
    }

    public Color getCursorColor()
    {
        return getModel().get(StyleComponent.class).getValue("cursor-color", Color.class, Color.ALPHA);
    }

    public String trimTextToWidth(String textToTrim, String ellipsis, int width, ITextHelper helper)
    {
        String trimmed = helper.trimStringToWidth(textToTrim, width, text.textSettings());
        if (!trimmed.equals(textToTrim))
        {
            if (trimmed.length() >= ellipsis.length())
                trimmed = trimmed.substring(0, trimmed.length() - ellipsis.length()) + ellipsis;
            else
                trimmed = "";
        }
        return trimmed;
    }

    public Text getText()
    {
        return text;
    }

    public Text getPromptText()
    {
        return promptText;
    }
}