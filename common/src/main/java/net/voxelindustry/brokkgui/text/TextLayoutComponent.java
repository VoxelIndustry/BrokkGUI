package net.voxelindustry.brokkgui.text;

import fr.ourten.teabeans.binding.Binding;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;

public class TextLayoutComponent extends GuiComponent
{
    private final Property<Boolean> expandToTextProperty = new Property<>(true);

    private final Property<GuiOverflow> textOverflowProperty = createRenderProperty(GuiOverflow.VISIBLE);

    private final Property<String> ellipsisProperty = new Property<>("…");

    private Binding<String> ellipsedTextBinding;

    private TextComponent textComponent;

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        if (!element.has(TextComponent.class))
            throw new GuiComponentException("Cannot attach TextLayoutComponent to an element not having TextComponent!");

        textComponent = element().get(TextComponent.class);

        bindSizeToText();
        bindOverflowLogic();
    }

    /////////////////////
    // LAYOUT BINDINGS //
    /////////////////////

    private void bindOverflowLogic()
    {
        textOverflowProperty.addListener(obs ->
        {
            if (textOverflow() == GuiOverflow.TRUNCATE)
            {
                createOrUpdateEllipsedBinding();
                textComponent.renderTextProperty().bindProperty(ellipsedTextBinding);
            }
            else if (ellipsedTextBinding != null && textComponent.renderTextProperty().isBoundTo(ellipsedTextBinding))
            {
                textComponent.renderTextProperty().unbind();
                textComponent.renderTextProperty().bindProperty(textComponent.textProperty());
                ellipsedTextBinding.unbindAll();
            }

            if (textOverflow() == GuiOverflow.HIDDEN)
                textComponent.textMask(true);
        });
    }

    private void createOrUpdateEllipsedBinding()
    {
        if (ellipsedTextBinding == null)
        {
            ellipsedTextBinding = new Binding<String>()
            {

                @Override
                public String computeValue()
                {
                    textComponent.updateTextSettings();
                    if (!expandToText() && transform().width() < getExpandedWidth())
                        return textHelper().trimStringToWidth(
                                textComponent.text(),
                                getAvailableTextWidth(),
                                ellipsis(),
                                textComponent.textSettings()
                        );
                    return textComponent.text();
                }
            };
        }
        ellipsedTextBinding.bind(textComponent.textProperty(),
                expandToTextProperty(),
                transform().widthProperty(),
                ellipsisProperty(),
                textComponent.computedTextPaddingValue());
    }

    private void bindSizeToText()
    {
        transform().widthProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(
                        textComponent.textProperty(),
                        textComponent.computedTextPaddingValue(),
                        textComponent.italicProperty(),
                        textComponent.boldProperty(),
                        textComponent.multilineProperty(),
                        textComponent.fontSizeProperty(),
                        textComponent.fontProperty());
            }

            @Override
            public Float computeValue()
            {
                textComponent.updateTextSettings();
                float stringWidth = textComponent.multiline() ? textHelper().getStringWidthMultiLine(textComponent.text(), textComponent.textSettings())
                                                              : textHelper().getStringWidth(textComponent.text(), textComponent.textSettings());

                return stringWidth + textComponent.computedTextPadding().getHorizontal();
            }
        });

        transform().heightProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(
                        textComponent.textProperty(),
                        textComponent.computedTextPaddingValue(),
                        textComponent.multilineProperty(),
                        textComponent.fontSizeProperty(),
                        textComponent.fontProperty());
            }

            @Override
            public Float computeValue()
            {
                textComponent.updateTextSettings();
                float stringHeight = textComponent.multiline() ? textHelper().getStringHeightMultiLine(textComponent.text(), textComponent.textSettings())
                                                               : textHelper().getStringHeight(textComponent.textSettings());

                return stringHeight + textComponent.computedTextPadding().getVertical();
            }
        });
    }

    private float getExpandedWidth()
    {
        return textHelper().getStringWidth(textComponent.text(), textComponent.textSettings())
                + textComponent.computedTextPadding().getHorizontal();
    }

    private float getAvailableTextWidth()
    {
        return transform().width() - textComponent.computedTextPadding().getHorizontal();
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    public Property<String> ellipsisProperty()
    {
        return ellipsisProperty;
    }

    public Property<Boolean> expandToTextProperty()
    {
        return expandToTextProperty;
    }

    public Property<GuiOverflow> textOverflowProperty()
    {
        return textOverflowProperty;
    }

    ////////////
    // VALUES //
    ////////////

    public String ellipsis()
    {
        return ellipsisProperty().getValue();
    }

    public void ellipsis(String ellipsis)
    {
        ellipsisProperty().setValue(ellipsis);
    }

    public boolean expandToText()
    {
        return expandToTextProperty().getValue();
    }

    public void expandToText(boolean expandToText)
    {
        if (expandToText && !expandToText())
            bindSizeToText();
        else if (!expandToText && expandToText())
        {
            transform().widthProperty().unbind();
            transform().heightProperty().unbind();
        }
        expandToTextProperty().setValue(expandToText);
    }

    public GuiOverflow textOverflow()
    {
        return textOverflowProperty().getValue();
    }

    public void textOverflow(GuiOverflow guiOverflow)
    {
        textOverflowProperty().setValue(guiOverflow);
    }
}
