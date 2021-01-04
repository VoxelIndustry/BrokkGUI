package net.voxelindustry.brokkgui.text;

import com.google.common.collect.Lists;
import fr.ourten.teabeans.binding.Expression;
import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.value.Observable;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.RenderComponent;
import net.voxelindustry.brokkgui.component.RequiredOverride;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.internal.IGuiHelper;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;

import java.util.List;

public class TextComponent extends GuiComponent implements RenderComponent
{
    private final Property<String>  textProperty        = new Property<>("");
    private final Property<String>  renderTextProperty  = createRenderProperty("");
    private final Property<Integer> lineSpacingProperty = createRenderProperty(1);

    private final Property<RectBox> textPaddingProperty = new Property<>(RectBox.EMPTY);

    private final List<ObservableValue<RectBox>> textPaddingList = Lists.newArrayList(textPaddingProperty);

    private final Expression<RectBox> computedTextPadding = new Expression<>(
            () ->
            {
                element().markRenderDirty();
                return textPaddingList.stream().map(ObservableValue::getValue).reduce(RectBox.EMPTY, RectBox::sum);
            },
            textPaddingList.toArray(new Observable[0]));

    private final Property<RectAlignment> textAlignmentProperty = createRenderProperty(RectAlignment.MIDDLE_CENTER);

    private final Property<Boolean> multilineProperty = createRenderProperty(false);

    protected Property<Color>   shadowColorProperty;
    protected Property<Boolean> useShadowProperty;
    protected Property<Color>   colorProperty;
    protected Property<String>  fontProperty;
    protected Property<Float>   fontSizeProperty;

    protected Property<Boolean> strikeThroughProperty;
    protected Property<Boolean> italicProperty;
    protected Property<Boolean> boldProperty;
    protected Property<Boolean> underlineProperty;

    protected Property<Color> outlineColorProperty;
    protected Property<Float> outlineWidthProperty;
    protected Property<Color> glowColorProperty;
    protected Property<Float> glowWidthProperty;

    private final ObservableValue<Float> lazyTextWidth;
    private final ObservableValue<Float> lazyTextHeight;

    private final TextSettings textSettings = TextSettings.build()
            .fontName("default")
            .textColor(Color.WHITE)
            .create();

    public TextComponent()
    {
        renderTextProperty.bindProperty(textProperty);

        lazyTextWidth = renderTextProperty.combine(multilineProperty, (renderText, multiline) ->
        {
            updateTextSettings();

            if (multiline)
                return BrokkGuiPlatform.getInstance().getTextHelper().getStringWidthMultiLine(renderText, textSettings);
            return BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth(renderText, textSettings);
        });

        lazyTextHeight = renderTextProperty.combine(multilineProperty, lineSpacingProperty, (renderText, multiline, lineSpacing) ->
        {
            if (multiline)
                return BrokkGuiPlatform.getInstance().getTextHelper().getStringHeightMultiLine(renderText, textSettings);
            return BrokkGuiPlatform.getInstance().getTextHelper().getStringHeight(textSettings);
        });
    }

    @Override
    public void renderContent(IRenderCommandReceiver renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (pass != RenderPass.MAIN)
            return;

        float xPos = transform().leftPos();
        float yPos = transform().topPos();

        RectBox currentTextPadding = computedTextPadding();

        if (textAlignment().isLeft())
            xPos += currentTextPadding.getLeft();
        else if (textAlignment().isRight())
            xPos += transform().width() - lazyTextWidth.getValue() - currentTextPadding.getRight();
        else
            xPos += currentTextPadding.getLeft() + (transform().width() - currentTextPadding.getHorizontal()) / 2 - lazyTextWidth.getValue() / 2;

        if (textAlignment().isUp())
            yPos += currentTextPadding.getTop();
        else if (textAlignment().isDown())
            yPos += transform().height() - lazyTextHeight.getValue() - currentTextPadding.getBottom();
        else
            yPos += currentTextPadding.getTop() + (transform().height() - currentTextPadding.getVertical()) / 2 - lazyTextHeight.getValue() / 2;

        updateTextSettings();

        if (multiline())
            renderer.drawStringMultiline(
                    renderText(),
                    xPos,
                    yPos,
                    transform().zLevel(),
                    RenderPass.MAIN,
                    textSettings);
        else
            renderer.drawString(
                    renderText(),
                    xPos,
                    yPos,
                    transform().zLevel(),
                    RenderPass.MAIN,
                    textSettings);
    }

    public void updateTextSettings()
    {
        textSettings
                .textColor(color())
                .shadowColor(useShadow() ? shadowColor() : Color.ALPHA)
                .fontName(font())
                .fontSize(fontSize())
                .lineSpacingMultiplier(lineSpacing())
                .strikethrough(strikeThrough())
                .italic(italic())
                .bold(bold())
                .underline(underline())
                .outlineColor(outlineColor())
                .outlineWidth(outlineWidth())
                .glowColor(glowColor())
                .glowWidth(glowWidth());
    }

    private float textWidth(IGuiHelper helper)
    {
        return lazyTextWidth.getValue();
    }

    private float textHeight(IGuiHelper helper)
    {
        return lazyTextHeight.getValue();
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    @RequiredOverride
    public Property<Color> shadowColorProperty()
    {
        if (shadowColorProperty == null)
            shadowColorProperty = createRenderProperty(Color.WHITE);
        return shadowColorProperty;
    }

    @RequiredOverride
    public Property<Boolean> useShadowProperty()
    {
        if (useShadowProperty == null)
            useShadowProperty = createRenderProperty(false);
        return useShadowProperty;
    }

    @RequiredOverride
    public Property<Color> colorProperty()
    {
        if (colorProperty == null)
            colorProperty = createRenderProperty(Color.BLACK);
        return colorProperty;
    }

    @RequiredOverride
    public Property<String> fontProperty()
    {
        if (fontProperty == null)
            fontProperty = createRenderProperty("default");
        return fontProperty;
    }

    @RequiredOverride
    public Property<Float> fontSizeProperty()
    {
        if (fontSizeProperty == null)
            fontSizeProperty = createRenderProperty(BrokkGuiPlatform.getInstance().getTextHelper().getDefaultFontSize());
        return fontSizeProperty;
    }

    @RequiredOverride
    public Property<Boolean> strikeThroughProperty()
    {
        if (strikeThroughProperty == null)
            strikeThroughProperty = createRenderProperty(false);
        return strikeThroughProperty;
    }

    @RequiredOverride
    public Property<Boolean> italicProperty()
    {
        if (italicProperty == null)
            italicProperty = createRenderProperty(false);
        return italicProperty;
    }

    @RequiredOverride
    public Property<Boolean> boldProperty()
    {
        if (boldProperty == null)
            boldProperty = createRenderProperty(false);
        return boldProperty;
    }

    @RequiredOverride
    public Property<Boolean> underlineProperty()
    {
        if (underlineProperty == null)
            underlineProperty = createRenderProperty(false);
        return underlineProperty;
    }

    @RequiredOverride
    public Property<Color> outlineColorProperty()
    {
        if (outlineColorProperty == null)
            outlineColorProperty = createRenderProperty(Color.ALPHA);
        return outlineColorProperty;
    }

    @RequiredOverride
    public Property<Float> outlineWidthProperty()
    {
        if (outlineWidthProperty == null)
            outlineWidthProperty = createRenderProperty(0F);
        return outlineWidthProperty;
    }

    @RequiredOverride
    public Property<Color> glowColorProperty()
    {
        if (glowColorProperty == null)
            glowColorProperty = createRenderProperty(Color.ALPHA);
        return glowColorProperty;
    }

    @RequiredOverride
    public Property<Float> glowWidthProperty()
    {
        if (glowWidthProperty == null)
            glowWidthProperty = createRenderProperty(0F);
        return glowWidthProperty;
    }

    public Property<String> textProperty()
    {
        return textProperty;
    }

    public Property<String> renderTextProperty()
    {
        return renderTextProperty;
    }

    public Property<Integer> lineSpacingProperty()
    {
        return lineSpacingProperty;
    }

    public Property<RectBox> textPaddingProperty()
    {
        return textPaddingProperty;
    }

    public ObservableValue<RectBox> computedTextPaddingValue()
    {
        return computedTextPadding;
    }

    public Property<RectAlignment> textAlignmentProperty()
    {
        return textAlignmentProperty;
    }

    public Property<Boolean> multilineProperty()
    {
        return multilineProperty;
    }

    ////////////
    // VALUES //
    ////////////

    public String text()
    {
        return textProperty().getValue();
    }

    public void text(String text)
    {
        textProperty().setValue(text);
    }

    public String renderText()
    {
        return renderTextProperty().getValue();
    }

    public void renderText(String renderText)
    {
        renderTextProperty().setValue(renderText);
    }

    public int lineSpacing()
    {
        return lineSpacingProperty().getValue();
    }

    public void lineSpacing(int lineSpacing)
    {
        lineSpacingProperty().setValue(lineSpacing);
    }

    public RectBox textPadding()
    {
        return textPaddingProperty().getValue();
    }

    public void textPadding(RectBox textPadding)
    {
        if (textPaddingProperty().isBound())
            textPaddingProperty().unbind();
        textPaddingProperty().setValue(textPadding);
    }

    public void addTextPaddingProperty(ObservableValue<RectBox> textPaddingValue)
    {
        if (textPaddingList.contains(textPaddingValue))
            return;
        textPaddingList.add(textPaddingValue);
        recomputeTextPadding();
    }

    public void removeTextPaddingProperty(ObservableValue<RectBox> textPaddingValue)
    {
        textPaddingList.remove(textPaddingValue);
        recomputeTextPadding();
    }

    public void replaceTextPaddingProperty(ObservableValue<RectBox> previousValue, ObservableValue<RectBox> currentValue)
    {
        if (previousValue != null)
            textPaddingList.remove(previousValue);
        textPaddingList.add(currentValue);
        recomputeTextPadding();
    }

    private void recomputeTextPadding()
    {
        Observable[] currentDependencies = computedTextPadding.getDependencies().toArray(new Observable[0]);
        for (Observable observable : currentDependencies)
        {
            if (!textPaddingList.contains(observable))
                computedTextPadding.unbind(observable);
        }
        textPaddingList.forEach(observable ->
        {
            if (!computedTextPadding.getDependencies().contains(observable))
                computedTextPadding.bind(observable);
        });
        computedTextPadding.invalidate();
    }

    public RectBox computedTextPadding()
    {
        return computedTextPadding.getValue();
    }

    public RectAlignment textAlignment()
    {
        return textAlignmentProperty().getValue();
    }

    public void textAlignment(RectAlignment alignment)
    {
        if (textAlignmentProperty().isBound())
            textAlignmentProperty().unbind();
        textAlignmentProperty().setValue(alignment);
    }

    public boolean multiline()
    {
        return multilineProperty().getValue();
    }

    public void multiline(boolean multiline)
    {
        multilineProperty().setValue(multiline);
    }

    @RequiredOverride
    public Color shadowColor()
    {
        return shadowColorProperty().getValue();
    }

    @RequiredOverride
    public void shadowColor(Color shadowColor)
    {
        shadowColorProperty().setValue(shadowColor);
    }

    @RequiredOverride
    public boolean useShadow()
    {
        return useShadowProperty().getValue();
    }

    @RequiredOverride
    public void useShadow(boolean useShadow)
    {
        useShadowProperty().setValue(useShadow);
    }

    @RequiredOverride
    public Color color()
    {
        return colorProperty().getValue();
    }

    @RequiredOverride
    public void color(Color color)
    {
        colorProperty().setValue(color);
    }

    @RequiredOverride
    public String font()
    {
        if (fontProperty == null)
            return "default";
        return fontProperty().getValue();
    }

    @RequiredOverride
    public void font(String font)
    {
        fontProperty().setValue(font);
    }

    @RequiredOverride
    public float fontSize()
    {
        if (fontSizeProperty == null)
            return BrokkGuiPlatform.getInstance().getTextHelper().getDefaultFontSize();
        return fontSizeProperty().getValue();
    }

    @RequiredOverride
    public void fontSize(float fontSize)
    {
        fontSizeProperty().setValue(fontSize);
    }

    @RequiredOverride
    public boolean strikeThrough()
    {
        if (strikeThroughProperty == null)
            return false;
        return strikeThroughProperty().getValue();
    }

    @RequiredOverride
    public void strikeThrough(boolean strikeThrough)
    {
        strikeThroughProperty().setValue(strikeThrough);
    }

    @RequiredOverride
    public boolean italic()
    {
        if (italicProperty == null)
            return false;
        return italicProperty().getValue();
    }

    @RequiredOverride
    public void italic(boolean italic)
    {
        italicProperty().setValue(italic);
    }

    @RequiredOverride
    public boolean bold()
    {
        if (boldProperty == null)
            return false;
        return boldProperty().getValue();
    }

    @RequiredOverride
    public void bold(boolean bold)
    {
        boldProperty().setValue(bold);
    }

    @RequiredOverride
    public boolean underline()
    {
        if (underlineProperty == null)
            return false;
        return underlineProperty().getValue();
    }

    @RequiredOverride
    public void underline(boolean underline)
    {
        underlineProperty().setValue(underline);
    }

    @RequiredOverride
    public Color outlineColor()
    {
        if (outlineColorProperty == null)
            return Color.ALPHA;
        return outlineColorProperty().getValue();
    }

    @RequiredOverride
    public void outlineColor(Color outlineColor)
    {
        outlineColorProperty().setValue(outlineColor);
    }

    @RequiredOverride
    public float outlineWidth()
    {
        if (outlineWidthProperty == null)
            return 0;
        return outlineWidthProperty().getValue();
    }

    @RequiredOverride
    public void outlineWidth(float outlineWidth)
    {
        outlineWidthProperty().setValue(outlineWidth);
    }

    @RequiredOverride
    public Color glowColor()
    {
        if (glowColorProperty == null)
            return Color.ALPHA;
        return glowColorProperty().getValue();
    }

    @RequiredOverride
    public void glowColor(Color glowColor)
    {
        glowColorProperty().setValue(glowColor);
    }

    @RequiredOverride
    public float glowWidth()
    {
        if (glowWidthProperty == null)
            return 0;
        return glowWidthProperty().getValue();
    }

    @RequiredOverride
    public void glowWidth(float glowWidth)
    {
        glowWidthProperty().setValue(glowWidth);
    }

    public TextSettings textSettings()
    {
        return textSettings;
    }
}