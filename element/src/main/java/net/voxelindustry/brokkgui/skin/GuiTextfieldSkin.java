package net.voxelindustry.brokkgui.skin;

import fr.ourten.teabeans.binding.Binding;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.behavior.GuiTextfieldBehavior;
import net.voxelindustry.brokkgui.element.input.GuiTextfield;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.internal.ITextHelper;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.shape.Text;
import net.voxelindustry.brokkgui.style.StyleComponent;

/**
 * @author Ourten 2 oct. 2016
 */
public class GuiTextfieldSkin<T extends GuiTextfield> extends GuiBehaviorSkinBase<T, GuiTextfieldBehavior<T>>
{
    private final Property<String> displayedTextProperty;

    private final Text text;

    public GuiTextfieldSkin(T model, GuiTextfieldBehavior<T> behaviour)
    {
        super(model, behaviour);

        displayedTextProperty = new Property<>("");

        text = new Text(model.getText());

        bindDisplayText();

        text.get(StyleComponent.class).styleClass().add("text");
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
    }

    @Override
    public void render(RenderPass pass, IRenderCommandReceiver renderer, int mouseX, int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);


    }

    private void bindDisplayText()
    {
/*        displayOffsetProperty.bindProperty(new Binding<Integer>()
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
        });*/
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
}