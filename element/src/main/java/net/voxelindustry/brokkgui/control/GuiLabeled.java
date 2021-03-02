package net.voxelindustry.brokkgui.control;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.brokkgui.text.TextLayoutComponent;

import javax.annotation.Nonnull;

public abstract class GuiLabeled extends GuiSkinedElement
{
    private final Property<GuiElement> iconProperty;
    private final Property<RectSide>   iconSideProperty    = new Property<>(RectSide.LEFT);
    private final Property<Float>      iconPaddingProperty = new Property<>(2F);

    private TextComponent       textComponent;
    private TextLayoutComponent textLayoutComponent;

    private final String startingText;

    public GuiLabeled(String text, GuiElement icon)
    {
        startingText = text;
        iconProperty = new Property<>(icon);

        textComponent.text(startingText);

        style().styleClass().add("text");
    }

    public GuiLabeled(String text)
    {
        this(text, null);
    }

    public GuiLabeled()
    {
        this("", null);
    }

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        textComponent = provide(TextComponent.class);
        textLayoutComponent = provide(TextLayoutComponent.class);
    }

    public Property<RectAlignment> getTextAlignmentProperty()
    {
        return textComponent().textAlignmentProperty();
    }

    public Property<String> getTextProperty()
    {
        return textComponent.textProperty();
    }

    public Property<RectBox> textPaddingProperty()
    {
        return textComponent().textPaddingProperty();
    }

    public Property<GuiElement> getIconProperty()
    {
        return iconProperty;
    }

    public Property<RectSide> getIconSideProperty()
    {
        return iconSideProperty;
    }

    public Property<Float> getIconPaddingProperty()
    {
        return iconPaddingProperty;
    }

    public RectAlignment getTextAlignment()
    {
        return getTextAlignmentProperty().getValue();
    }

    public void setTextAlignment(RectAlignment alignment)
    {
        getTextAlignmentProperty().setValue(alignment);
    }

    public String getText()
    {
        return textComponent.text();
    }

    public void setText(@Nonnull String text)
    {
        textComponent.text(text);
    }

    public RectBox textPadding()
    {
        return textPaddingProperty().getValue();
    }

    public void setTextPadding(RectBox textPadding)
    {
        textPaddingProperty().setValue(textPadding);
    }

    public GuiElement getIcon()
    {
        return iconProperty.getValue();
    }

    public void setIcon(GuiElement icon)
    {
        iconProperty.setValue(icon);
    }

    public RectSide getIconSide()
    {
        return iconSideProperty.getValue();
    }

    public void setIconSide(RectSide iconSide)
    {
        iconSideProperty.setValue(iconSide);
    }

    public float getIconPadding()
    {
        return iconPaddingProperty.getValue();
    }

    public void setIconPadding(float iconPadding)
    {
        iconPaddingProperty.setValue(iconPadding);
    }

    ////////////////
    // COMPONENTS //
    ////////////////

    public TextComponent textComponent()
    {
        return textComponent;
    }

    public TextLayoutComponent textLayoutComponent()
    {
        return textLayoutComponent;
    }

    ///////////////
    // DELEGATES //
    ///////////////

    public Property<String> ellipsisProperty()
    {
        return textLayoutComponent.ellipsisProperty();
    }

    public Property<Boolean> expandToTextProperty()
    {
        return textLayoutComponent.expandToTextProperty();
    }

    public String ellipsis()
    {
        return textLayoutComponent.ellipsis();
    }

    public void ellipsis(String ellipsis)
    {
        textLayoutComponent.ellipsis(ellipsis);
    }

    public boolean expandToText()
    {
        return textLayoutComponent.expandToText();
    }

    public void expandToText(boolean expandToText)
    {
        textLayoutComponent.expandToText(expandToText);
    }
}