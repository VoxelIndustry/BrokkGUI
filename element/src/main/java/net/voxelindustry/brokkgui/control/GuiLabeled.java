package net.voxelindustry.brokkgui.control;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.LabelIconComponent;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.style.StyledElement;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.brokkgui.text.TextLayoutComponent;

import javax.annotation.Nonnull;

public abstract class GuiLabeled extends GuiElement implements StyledElement
{
    private LabelIconComponent  iconComponent;
    private TextComponent       textComponent;
    private TextLayoutComponent textLayoutComponent;

    private final String startingText;

    public GuiLabeled(String text, GuiElement icon)
    {
        startingText = text;

        iconComponent.icon(icon);
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

        iconComponent = provide(LabelIconComponent.class);
    }

    public Property<RectAlignment> textAlignmentProperty()
    {
        return textComponent().textAlignmentProperty();
    }

    public Property<String> textProperty()
    {
        return textComponent.textProperty();
    }

    public Property<RectBox> textPaddingProperty()
    {
        return textComponent().textPaddingProperty();
    }

    public Property<GuiElement> iconProperty()
    {
        return iconComponent.iconProperty();
    }

    public Property<RectSide> iconSideProperty()
    {
        return iconComponent.iconSideProperty();
    }

    public Property<Float> iconPaddingProperty()
    {
        return iconComponent.iconPaddingProperty();
    }

    public RectAlignment textAlignment()
    {
        return textAlignmentProperty().getValue();
    }

    public void textAlignment(RectAlignment alignment)
    {
        textAlignmentProperty().setValue(alignment);
    }

    public String text()
    {
        return textComponent.text();
    }

    public void text(@Nonnull String text)
    {
        textComponent.text(text);
    }

    public RectBox textPadding()
    {
        return textPaddingProperty().getValue();
    }

    public void textPadding(RectBox textPadding)
    {
        textPaddingProperty().setValue(textPadding);
    }

    public GuiElement icon()
    {
        return iconComponent.icon();
    }

    public void icon(GuiElement icon)
    {
        iconComponent.icon(icon);
    }

    public RectSide getIconSide()
    {
        return iconComponent.iconSide();
    }

    public void setIconSide(RectSide iconSide)
    {
        iconComponent.iconSide(iconSide);
    }

    public float iconPadding()
    {
        return iconComponent.iconPadding();
    }

    public void iconPadding(float iconPadding)
    {
        iconComponent.iconPadding(iconPadding);
    }

    ////////////////
    // COMPONENTS //
    ////////////////

    public LabelIconComponent iconComponent()
    {
        return iconComponent;
    }

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