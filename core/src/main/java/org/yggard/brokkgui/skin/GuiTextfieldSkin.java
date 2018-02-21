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
    
    public GuiTextfieldSkin(final T model, final GuiTextfieldBehavior<T> behaviour)
    {
        super(model, behaviour);

        this.getModel().getStyle().registerProperty("-text-color", Color.WHITE, Color.class);
        
        this.ellipsedPromptProperty = new BaseProperty<>("", "ellipsedPromptProperty");
        
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
                if (!StringUtils.isEmpty(this.getModel().getPromptText())
                        && (this.getModel().isPromptTextAlwaysDisplayed()
                                || StringUtils.isEmpty(this.getModel().getText())))
                    renderer.getHelper().drawString(this.ellipsedPromptProperty.getValue(), x + getModel().getTextPadding(),
                            y + getModel().getTextPadding(), this.getModel().getzLevel(), color.shade(0.5f));
                if (this.getModel().getCursorPosition() == this.getModel().getText().length())
                    renderer.getHelper().drawColoredRect(renderer,
                            x + getModel().getTextPadding() + 1 + renderer.getHelper().getStringWidth(
                                    this.getModel().getText().substring(0, this.getModel().getCursorPosition())),
                            y + renderer.getHelper().getStringHeight() + getModel().getTextPadding() - 1, 5, 1,
                            this.getModel().getzLevel(), color.shade(0.7f));
                renderer.getHelper().drawString(this.getModel().getText(), x + getModel().getTextPadding() + 1, 
                        y + getModel().getTextPadding() + 1, this.getModel().getzLevel(), color.shade(0.7f));

                if (this.getModel().getCursorPosition() != this.getModel().getText().length())
                    renderer.getHelper().drawColoredRect(renderer,
                            x + getModel().getTextPadding() + renderer.getHelper().getStringWidth(
                                    this.getModel().getText().substring(0, this.getModel().getCursorPosition())),
                            y + getModel().getTextPadding() - 1, 1, renderer.getHelper().getStringHeight() + 1,
                            this.getModel().getzLevel(), color.shade(0.3f));
                else
                    renderer.getHelper().drawColoredRect(renderer,
                            x + getModel().getTextPadding() + renderer.getHelper().getStringWidth(
                                    this.getModel().getText().substring(0, this.getModel().getCursorPosition())),
                            y + renderer.getHelper().getStringHeight() + 1, 5, 1, this.getModel().getzLevel(),
                            color.shade(0.3f));
                renderer.getHelper().drawString(this.getModel().getText(), x + getModel().getTextPadding(),
                        y + getModel().getTextPadding(), this.getModel().getzLevel(),
                        color);
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