package net.voxelindustry.brokkgui.component;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;

import javax.annotation.Nonnull;

public class Text extends GuiComponent
{
    private final BaseProperty<RectAlignment> textAlignmentProperty;
    private final BaseProperty<String>        textProperty;

    private final BaseProperty<String>  ellipsisProperty;
    private final BaseProperty<Boolean> expandToTextProperty;
    private final BaseProperty<RectBox> textPaddingProperty;

    public Text()
    {
        this.textProperty = new BaseProperty<>("", "textProperty");
        this.textAlignmentProperty = new BaseProperty<>(RectAlignment.MIDDLE_CENTER, "textAlignmentProperty");
        this.ellipsisProperty = new BaseProperty<>("...", "ellipsisProperty");
        this.expandToTextProperty = new BaseProperty<>(true, "expandToTextProperty");
        this.textPaddingProperty = new BaseProperty<>(RectBox.EMPTY, "textPaddingProperty");

        this.bindSizeToText();
    }

    public BaseProperty<RectAlignment> textAlignmentProperty()
    {
        return this.textAlignmentProperty;
    }

    public BaseProperty<String> textProperty()
    {
        return this.textProperty;
    }

    public BaseProperty<String> ellipsisProperty()
    {
        return this.ellipsisProperty;
    }

    public BaseProperty<Boolean> expandToTextProperty()
    {
        return this.expandToTextProperty;
    }

    public BaseProperty<RectBox> textPaddingProperty()
    {
        return textPaddingProperty;
    }

    public RectAlignment textAlignment()
    {
        return this.textAlignmentProperty().getValue();
    }

    public void textAlignment(final RectAlignment alignment)
    {
        this.textAlignmentProperty().setValue(alignment);
    }

    public String text()
    {
        return this.textProperty().getValue();
    }

    public void text(@Nonnull final String text)
    {
        this.textProperty().setValue(text);
    }

    public String ellipsis()
    {
        return this.ellipsisProperty.getValue();
    }

    public void ellipsis(final String ellipsis)
    {
        this.ellipsisProperty.setValue(ellipsis);
    }

    public RectBox textPadding()
    {
        return this.textPaddingProperty.getValue();
    }

    public void textPadding(RectBox textPadding)
    {
        this.textPaddingProperty.setValue(textPadding);
    }

    public boolean expandToText()
    {
        return this.expandToTextProperty.getValue();
    }

    public void expandToText(final boolean expandToText)
    {
        if (expandToText && !this.expandToText())
            this.bindSizeToText();
        else if (!expandToText && this.expandToText())
        {
            this.element().transform().widthProperty().unbind();
            this.element().transform().heightProperty().unbind();
        }
        this.expandToTextProperty.setValue(expandToText);
    }

    private void bindSizeToText()
    {
        this.element().transform().widthProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(textProperty(), textPaddingProperty());
            }

            @Override
            public Float computeValue()
            {

                return BrokkGuiPlatform.instance().guiHelper().getStringWidth(text())
                        + textPadding().getLeft() + textPadding().getRight();
            }
        });

        this.element().transform().heightProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(textProperty(),
                        textPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                return BrokkGuiPlatform.instance().guiHelper().getStringHeight()
                        + textPadding().getTop() + textPadding().getBottom();
            }
        });
    }
}
