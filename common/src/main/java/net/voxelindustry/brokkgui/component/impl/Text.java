package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.TextLayoutHelper;
import net.voxelindustry.brokkgui.event.ComponentEvent;

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
        this.expandToTextProperty = new BaseProperty<>(Boolean.TRUE, "expandToTextProperty");
        this.textPaddingProperty = new BaseProperty<>(RectBox.EMPTY, "textPaddingProperty");
    }

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        if (this.expandToText())
            this.bindSizeToText();

        element().getEventDispatcher().addHandler(ComponentEvent.ANY, this::onComponentChange);
    }

    private void onComponentChange(ComponentEvent event)
    {
        if (event.getComponent() != null && event.getComponent().getClass() != Icon.class)
            return;

        if (this.expandToText())
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
        Icon icon = this.element().get(Icon.class);
        if (icon != null)
        {
            this.element().transform().widthProperty().bind(TextLayoutHelper.createMinimalWidthBinding(this, icon));
            this.element().transform().heightProperty().bind(TextLayoutHelper.createMinimalHeightBinding(this, icon));
        }
        else
        {
            this.element().transform().widthProperty().bind(TextLayoutHelper.createMinimalWidthBinding(this));
            this.element().transform().heightProperty().bind(TextLayoutHelper.createMinimalHeightBinding(this));
        }
    }
}
