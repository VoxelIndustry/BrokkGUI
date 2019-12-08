package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.Observable;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.event.ComponentEvent;
import net.voxelindustry.brokkgui.layout.ILayoutBox;
import net.voxelindustry.brokkgui.platform.TextPlatform;

import javax.annotation.Nonnull;

import static net.voxelindustry.brokkgui.platform.TextPlatform.trimTextToWidth;

public class Text extends GuiComponent implements ILayoutBox
{
    private final BaseProperty<RectAlignment> textAlignmentProperty;
    private final BaseProperty<String>        textProperty;

    private final BaseProperty<String>  ellipsisProperty;
    private final BaseProperty<Boolean> expandToTextProperty;
    private final BaseProperty<RectBox> textPaddingProperty;

    private final BaseProperty<RectBox> elementContentPaddingProperty;

    private boolean isLayoutDirty = false;

    private float layoutWidth;
    private float layoutHeight;
    private float layoutPosX;
    private float layoutPosY;

    private String textTrimmedForRender;

    public Text()
    {
        this.textProperty = new BaseProperty<>("", "textProperty");
        this.textAlignmentProperty = new BaseProperty<>(RectAlignment.MIDDLE_CENTER, "textAlignmentProperty");
        this.ellipsisProperty = new BaseProperty<>("...", "ellipsisProperty");
        this.expandToTextProperty = new BaseProperty<>(Boolean.TRUE, "expandToTextProperty");
        this.textPaddingProperty = new BaseProperty<>(RectBox.EMPTY, "textPaddingProperty");

        this.elementContentPaddingProperty = new BaseProperty<>(RectBox.EMPTY, "elementContentPaddingProperty");

        ValueInvalidationListener dirtyLayoutListener = this::dirtyOnChange;
        this.textProperty.addListener(dirtyLayoutListener);
        this.textAlignmentProperty.addListener(dirtyLayoutListener);
        this.ellipsisProperty.addListener(dirtyLayoutListener);
        this.expandToTextProperty.addListener(dirtyLayoutListener);
        this.textPaddingProperty.addListener(dirtyLayoutListener);
    }

    private void dirtyOnChange(Observable obs)
    {
        this.isLayoutDirty = true;
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
        if (event.getComponent() == null || event.getComponent().getClass() != Icon.class)
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

    public BaseProperty<RectBox> elementContentPaddingProperty()
    {
        return this.elementContentPaddingProperty;
    }

    public void elementContentPaddingProperty(ObservableValue<RectBox> elementContentPaddingProperty)
    {
        this.elementContentPaddingProperty.bind(elementContentPaddingProperty);
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

    public RectBox elementContentPadding()
    {
        return this.elementContentPaddingProperty().getValue();
    }

    public void elementContentPadding(RectBox elementContentPadding)
    {
        this.elementContentPaddingProperty().setValue(elementContentPadding);
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
       /*x if (icon != null)
        {
            this.element().transform().widthProperty().bind(TextLayoutHelper.createMinimalWidthBinding(this, icon));
            this.element().transform().heightProperty().bind(TextLayoutHelper.createMinimalHeightBinding(this, icon));
        }
        else
        {
            this.element().transform().widthProperty().bind(TextLayoutHelper.createMinimalWidthBinding(this));
            this.element().transform().heightProperty().bind(TextLayoutHelper.createMinimalHeightBinding(this));
        }*/
    }

    public String textTrimmedForRender()
    {
        return this.textTrimmedForRender;
    }

    ////////////
    // LAYOUT //
    ////////////

    @Override
    public float minWidth()
    {
        return BrokkGuiPlatform.instance().guiHelper().getStringWidth(ellipsis()) + textPadding().getHorizontal();
    }

    @Override
    public float minHeight()
    {
        return BrokkGuiPlatform.instance().guiHelper().getStringHeight() + textPadding().getVertical();
    }

    @Override
    public float prefWidth()
    {
        return BrokkGuiPlatform.instance().guiHelper().getStringWidth(text()) + textPadding().getHorizontal();
    }

    @Override
    public float prefHeight()
    {
        return BrokkGuiPlatform.instance().guiHelper().getStringHeight() + textPadding().getVertical();
    }

    @Override
    public float maxWidth()
    {
        return BrokkGuiPlatform.instance().guiHelper().getStringWidth(text()) + textPadding().getHorizontal();
    }

    @Override
    public float maxHeight()
    {
        return BrokkGuiPlatform.instance().guiHelper().getStringHeight() + textPadding().getVertical();
    }

    @Override
    public boolean isLayoutDirty()
    {
        return isLayoutDirty;
    }

    @Override
    public void layoutWidth(float layoutWidth)
    {
        this.layoutWidth = layoutWidth;

        float widthForText = layoutWidth - textPadding().getHorizontal();

        if (widthForText < TextPlatform.stringWidth(text()))
            this.textTrimmedForRender = trimTextToWidth(text(),
                    layoutWidth - textPadding().getHorizontal() - TextPlatform.stringWidth(ellipsis()))
                    + ellipsis();
        else
            this.textTrimmedForRender = text();
    }

    @Override
    public void layoutHeight(float layoutHeight)
    {
        this.layoutHeight = layoutHeight;
    }

    @Override
    public void layoutPosX(float layoutPosX)
    {
        this.layoutPosX = layoutPosX;
    }

    @Override
    public void layoutPosY(float layoutPosY)
    {
        this.layoutPosY = layoutPosY;
    }

    public float layoutWidth()
    {
        return this.layoutWidth;
    }

    public float layoutHeight()
    {
        return this.layoutHeight;
    }

    public float layoutPosX()
    {
        return this.layoutPosX;
    }

    public float layoutPosY()
    {
        return this.layoutPosY;
    }
}
