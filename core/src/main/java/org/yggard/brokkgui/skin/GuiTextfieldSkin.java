package org.yggard.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;
import org.apache.commons.lang3.StringUtils;
import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.behavior.GuiTextfieldBehavior;
import org.yggard.brokkgui.element.GuiTextfield;
import org.yggard.brokkgui.internal.IGuiHelper;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.validation.BaseTextValidator;

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

    public GuiTextfieldSkin(T model, GuiTextfieldBehavior<T> behaviour)
    {
        super(model, behaviour);

        getModel().getStyle().registerProperty("-text-color", Color.WHITE, Color.class);
        getModel().getStyle().registerProperty("-cursor-color", Color.WHITE.shade(0.3f), Color.class);

        this.ellipsedPromptProperty = new BaseProperty<>("", "ellipsedPromptProperty");
        this.displayOffsetProperty = new BaseProperty<>(0, "displayOffsetProperty");
        this.displayedTextProperty = new BaseProperty<>("", "displayedTextProperty");

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
                if(getModel().expandToText())
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

    @Override
    public void render(final RenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);

        if (pass == RenderPass.MAIN)
        {
            Color color = !getModel().isValid() ? Color.RED : this.getTextColor();

            if (color != null)
            {
                float x = getModel().getxPos() + getModel().getxTranslate();
                float y = getModel().getyPos() + getModel().getyTranslate();
                float xPadding = getModel().getTextPadding().getLeft();
                float yPadding = getModel().getTextPadding().getTop();
                // Prompt text
                if (!StringUtils.isEmpty(getModel().getPromptText())
                        && (getModel().isPromptTextAlwaysDisplayed()
                        || StringUtils.isEmpty(getModel().getText())))
                {
                    renderer.getHelper().drawString(this.ellipsedPromptProperty.getValue(), x + xPadding,
                            y + yPadding, getModel().getzLevel(), color.shade(0.5f));
                }
                // Text
                renderer.getHelper().drawString(this.getDisplayedText(), x + xPadding,
                        y + xPadding, getModel().getzLevel(), color, color.shade(0.7f));
                // Cursor
                if (getModel().getFocusedProperty().getValue())
                {
                    renderer.getHelper().drawColoredRect(renderer,
                            x + xPadding - 1 + renderer.getHelper().getStringWidth(
                                    getModel().getText().substring(this.displayOffsetProperty.getValue(),
                                            getModel().getCursorPos())), y + xPadding - 1, 1,
                            renderer.getHelper().getStringHeight() + 1, getModel().getzLevel(), getCursorColor());
                }
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

    public String getDisplayedText()
    {
        return displayedTextProperty.getValue();
    }

    public Color getTextColor()
    {
        return getModel().getStyle().getStyleProperty("-text-color", Color.class).getValue();
    }

    public Color getCursorColor()
    {
        return getModel().getStyle().getStyleProperty("-cursor-color", Color.class).getValue();
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