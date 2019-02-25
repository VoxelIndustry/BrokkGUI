package net.voxelindustry.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.behavior.GuiTextfieldBehavior;
import net.voxelindustry.brokkgui.element.input.GuiTextfield;
import net.voxelindustry.brokkgui.internal.IGuiHelper;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.shape.Text;
import net.voxelindustry.brokkgui.validation.BaseTextValidator;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Ourten 2 oct. 2016
 */
public class GuiTextfieldSkin<T extends GuiTextfield> extends GuiBehaviorSkinBase<T, GuiTextfieldBehavior<T>>
{
    private final BaseProperty<String>  ellipsedPromptProperty;
    private final BaseProperty<Integer> displayOffsetProperty;
    // Here to keep last value of the displayOffsetProperty
    private       int                   displayOffset = 0;
    private final BaseProperty<String>  displayedTextProperty;

    private Text text, promptText;

    public GuiTextfieldSkin(T model, GuiTextfieldBehavior<T> behaviour)
    {
        super(model, behaviour);

        this.ellipsedPromptProperty = new BaseProperty<>("", "ellipsedPromptProperty");
        this.displayOffsetProperty = new BaseProperty<>(0, "displayOffsetProperty");
        this.displayedTextProperty = new BaseProperty<>("", "displayedTextProperty");

        this.bindDisplayText();

        this.text = new Text(model.getText());
        this.promptText = new Text(model.getPromptText());

        text.addStyleClass("text");
        promptText.addStyleClass("prompt");
        getModel().getStyle().registerProperty("cursor-color", Color.WHITE.shade(0.3f), Color.class);

        text.getTextProperty().bind(this.displayedTextProperty);
        text.getzLevelProperty().bind(model.getzLevelProperty());

        text.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getxPosProperty(), getModel().getxTranslateProperty(),
                        getModel().getTextPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                return getModel().getxPos() + getModel().getxTranslate() + getModel().getTextPadding().getLeft();
            }
        });
        text.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getyPosProperty(), getModel().getyTranslateProperty(),
                        getModel().getTextPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                return getModel().getyPos() + getModel().getyTranslate() + getModel().getTextPadding().getTop();
            }
        });
        text.getWidthProperty().bind(BaseExpression.transform(this.displayedTextProperty,
                BrokkGuiPlatform.getInstance().getGuiHelper()::getStringWidth));
        text.setHeight(BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight());

        getModel().addChild(text);

        promptText.getTextProperty().bind(this.ellipsedPromptProperty);
        promptText.getzLevelProperty().bind(model.getzLevelProperty());

        promptText.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getxPosProperty(), getModel().getxTranslateProperty(),
                        getModel().getTextPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                return getModel().getxPos() + getModel().getxTranslate() + getModel().getTextPadding().getLeft();
            }
        });
        promptText.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getyPosProperty(), getModel().getyTranslateProperty(),
                        getModel().getTextPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                return getModel().getyPos() + getModel().getyTranslate() + getModel().getTextPadding().getTop();
            }
        });
        promptText.getWidthProperty().bind(BaseExpression.transform(this.ellipsedPromptProperty,
                BrokkGuiPlatform.getInstance().getGuiHelper()::getStringWidth));
        promptText.setHeight(BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight());

        promptText.getVisibleProperty().bind(new BaseBinding<Boolean>()
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
    public void render(final RenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);

        if (pass == RenderPass.FOREGROUND)
        {
            float x = getModel().getxPos() + getModel().getxTranslate();
            float y = getModel().getyPos() + getModel().getyTranslate();
            float xPadding = getModel().getTextPadding().getLeft();
            float yPadding = getModel().getTextPadding().getTop();

            // Cursor
            if (getModel().getFocusedProperty().getValue())
            {
                renderer.getHelper().drawColoredRect(renderer,
                        x + xPadding - 1 + renderer.getHelper().getStringWidth(
                                getModel().getText().substring(this.displayOffsetProperty.getValue(),
                                        getModel().getCursorPos())), y + yPadding - 1, 1,
                        renderer.getHelper().getStringHeight() + 1, getModel().getzLevel() + 1, getCursorColor());
            }
        }
        else if (pass == RenderPass.HOVER)
            if (!getModel().isValid())
            {
                int i = 0;
                for (final BaseTextValidator validator : getModel().getValidators())
                    if (validator.isErrored())
                    {
                        renderer.getHelper().drawString(validator.getMessage(),
                                getModel().getxPos() + getModel().getxTranslate(),
                                getModel().getyPos() + getModel().getyTranslate()
                                        + getModel().getHeight()
                                        + i * (renderer.getHelper().getStringHeight() + 1),
                                getModel().getzLevel(), Color.RED);
                        i++;
                    }
            }
    }

    private void bindDisplayText()
    {
        this.ellipsedPromptProperty.bind(new BaseBinding<String>()
        {
            {
                super.bind(getModel().getPrompTextProperty(), getModel().getPromptEllipsisProperty(),
                        getModel().getTextPaddingProperty(), getModel().getWidthProperty());
            }

            @Override
            public String computeValue()
            {
                return trimTextToWidth(getModel().getPromptText(), getModel().getPromptEllipsis(),
                        (int) (getModel().getWidth() - getModel().getTextPadding().getLeft() - getModel().getTextPadding().getRight()),
                        BrokkGuiPlatform.getInstance().getGuiHelper());
            }

        });

        displayOffsetProperty.bind(new BaseBinding<Integer>()
        {
            {
                super.bind(getModel().getCursorPosProperty(), getModel().getTextPaddingProperty(),
                        getModel().getWidthProperty(), getModel().getExpandToTextProperty());
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
                IGuiHelper helper = BrokkGuiPlatform.getInstance().getGuiHelper();
                while (helper.getStringWidth(getModel().getText().substring(displayOffset, getModel().getCursorPos()))
                        > getModel().getWidth() - getModel().getTextPadding().getLeft() - getModel().getTextPadding().getRight())
                {
                    displayOffset++;
                }
                displayOffset = Math.max(0, displayOffset);
                return displayOffset;
            }

        });

        displayedTextProperty.bind(new BaseBinding<String>()
        {
            {
                super.bind(displayOffsetProperty, getModel().getTextProperty(), getModel().getWidthProperty(),
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

                IGuiHelper helper = BrokkGuiPlatform.getInstance().getGuiHelper();
                while (helper.getStringWidth(rightPart) > getModel().getWidth()
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
        return getModel().getStyle().getStyleProperty("cursor-color", Color.class).getValue();
    }

    public String trimTextToWidth(String textToTrim, String ellipsis, int width, IGuiHelper helper)
    {
        String trimmed = helper.trimStringToPixelWidth(textToTrim, width);
        if (!trimmed.equals(textToTrim))
        {
            if (trimmed.length() >= ellipsis.length())
                trimmed = trimmed.substring(0, trimmed.length() - ellipsis.length()) + ellipsis;
            else
                trimmed = "";
        }
        return trimmed;
    }
}