package org.yggard.brokkgui.skin;

import org.apache.commons.lang3.StringUtils;
import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.behavior.GuiTextfieldBehavior;
import org.yggard.brokkgui.element.GuiTextfield;
import org.yggard.brokkgui.internal.IGuiHelper;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.validation.BaseTextValidator;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;

/**
 * @author Ourten 2 oct. 2016
 */
public class GuiTextfieldSkin<T extends GuiTextfield> extends GuiBehaviorSkinBase<T, GuiTextfieldBehavior<T>>
{
    
    private final BaseProperty<String> ellipsedPromptProperty;
    private final BaseProperty<Integer> displayOffsetProperty;
    // Here to keep last value of the displayOffsetProperty
    private int displayOffset = 0;
    private final BaseProperty<String> displayedTextProperty;
    
    public GuiTextfieldSkin(final T model, final GuiTextfieldBehavior<T> behaviour)
    {
        super(model, behaviour);

        this.getModel().getStyle().registerProperty("-text-color", Color.WHITE, Color.class);
        
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
            public String computeValue() {
                return trimTextToWidth(getModel().getPromptText(), getModel().getPromptEllipsisProperty().getValue(),
                        (int) (getModel().getWidth() - getModel().getTextPaddingProperty().getValue() * 2), 
                        BrokkGuiPlatform.getInstance().getGuiHelper());
            }
            
        });
        
        displayOffsetProperty.bind(new BaseBinding<Integer>() 
        {
            
            {
                super.bind(getModel().getCursorPositionProperty(), getModel().getTextPaddingProperty(), getModel().getWidthProperty());
            }

            @Override
            public Integer computeValue() 
            {
                if(getModel().getCursorPosition() <= displayOffset)
                {
                    displayOffset = getModel().getCursorPosition();
                    return displayOffset;
                }
                IGuiHelper helper = BrokkGuiPlatform.getInstance().getGuiHelper();
                while(helper.getStringWidth(getModel().getText().substring(
                        displayOffset, 
                        getModel().getCursorPosition())) > getModel().getWidth() - 2 * getModel().getTextPaddingProperty().getValue())
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
                super.bind(displayOffsetProperty, getModel().getTextProperty());
            }

            @Override
            public String computeValue() 
            {
                String rightPart = getModel().getText().substring(displayOffsetProperty.getValue());
                IGuiHelper helper = BrokkGuiPlatform.getInstance().getGuiHelper();
                while(helper.getStringWidth(rightPart) > getModel().getWidth() - 2 * getModel().getTextPaddingProperty().getValue())
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
            final Color color = !this.getModel().isValid() ? Color.RED : this.getTextColor();

            if (color != null)
            {
                final float x = this.getModel().getxPos() + this.getModel().getxTranslate();
                final float y = this.getModel().getyPos() + this.getModel().getyTranslate();
                final float padding = this.getModel().getTextPadding();

                // Prompt text
                if (!StringUtils.isEmpty(this.getModel().getPromptText())
                        && (this.getModel().isPromptTextAlwaysDisplayed()
                                || StringUtils.isEmpty(this.getModel().getText())))
                {
                    renderer.getHelper().drawString(this.ellipsedPromptProperty.getValue(), x + padding,
                            y + this.getModel().getTextPaddingProperty().getValue(),
                            this.getModel().getzLevel(), color.shade(0.5f));
                }
                
                // Text shadow
                renderer.getHelper().drawString(this.displayedTextProperty.getValue(), x + padding + 1, 
                        y + padding + 1, this.getModel().getzLevel(), color.shade(0.7f));
                
                // Cursor
                if (this.getModel().getFocusedProperty().getValue())
                {
                    renderer.getHelper().drawColoredRect(renderer,
                            x + padding - 1 + renderer.getHelper().getStringWidth(
                                    this.getModel().getText().substring(this.displayOffsetProperty.getValue(),
                                            this.getModel().getCursorPosition())), y + padding - 1, 1, 
                            renderer.getHelper().getStringHeight() + 1, this.getModel().getzLevel(), color.shade(0.3f));
                }
                
                // Text
                renderer.getHelper().drawString(this.displayedTextProperty.getValue(), x + padding,
                        y + padding, this.getModel().getzLevel(), color);
            }
            else if (pass == RenderPass.SPECIAL)
                if (!this.getModel().isValid())
                {
                    int i = 0;
                    for (final BaseTextValidator validator : this.getModel().getValidators())
                        if (validator.isErrored())
                        {
                            renderer.getHelper().drawString(validator.getMessage(),
                                    this.getModel().getxPos() + this.getModel().getxTranslate(),
                                    this.getModel().getyPos() + this.getModel().getyTranslate()
                                            + this.getModel().getHeight()
                                            + i * (renderer.getHelper().getStringHeight() + 1),
                                    this.getModel().getzLevel(), Color.RED);
                            i++;
                        }
                }
        }
    }

    public Color getTextColor()
    {
        return this.getModel().getStyle().getStyleProperty("-text-color", Color.class).getValue();
    }
    
    public String trimTextToWidth(String textToTrim, String ellips, int width, IGuiHelper helper) {
        String trimmed = helper.trimStringToPixelWidth(textToTrim, width);
        if(!trimmed.equals(textToTrim))
        {
            if(trimmed.length() >= ellips.length())
                trimmed = trimmed.substring(0, trimmed.length() - ellips.length()) + ellips;
            else
                trimmed = "";
        }
        return trimmed;
    }
    
}