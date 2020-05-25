package net.voxelindustry.brokkgui.shape;

import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.RenderComponent;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.internal.IGuiHelper;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class TextComponent extends GuiComponent implements RenderComponent
{
    private final BaseProperty<String>  textProperty;
    private final BaseProperty<String>  renderTextProperty;
    private final BaseProperty<Integer> lineSpacingProperty;

    private final BaseProperty<RectBox>       textPaddingProperty;
    private final BaseProperty<RectAlignment> textAlignmentProperty;

    private final BaseProperty<Boolean> multilineProperty;

    protected BaseProperty<Color>   shadowColorProperty;
    protected BaseProperty<Boolean> useShadowProperty;
    protected BaseProperty<Color>   colorProperty;

    private final ObservableValue<Float> lazyTextWidth;
    private final ObservableValue<Float> lazyTextHeight;

    public TextComponent()
    {
        textProperty = new BaseProperty<>("", "textProperty");
        renderTextProperty = new BaseProperty<>("", "renderTextProperty");
        lineSpacingProperty = new BaseProperty<>(1, "lineSpacingProperty");
        textPaddingProperty = new BaseProperty<>(RectBox.EMPTY, "textPaddingProperty");
        textAlignmentProperty = new BaseProperty<>(RectAlignment.MIDDLE_CENTER, "textAlignmentProperty");

        multilineProperty = new BaseProperty<>(false, "multilineProperty");

        renderTextProperty.bind(textProperty);

        lazyTextWidth = renderTextProperty.combine(multilineProperty, (renderText, multiline) ->
        {
            if (multiline)
                return BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidthMultiLine(renderText);
            return BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(renderText);
        });

        lazyTextHeight = renderTextProperty.combine(multilineProperty, lineSpacingProperty, (renderText, multiline, lineSpacing) ->
        {
            if (multiline)
                return BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeightMultiLine(renderText, lineSpacing);
            return BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight();
        });
    }

    @Override
    public void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (pass != RenderPass.MAIN)
            return;

        float xPos = transform().leftPos();
        float yPos = transform().topPos();

        if (textAlignment().isLeft())
            xPos += textPadding().getLeft();
        else if (textAlignment().isRight())
            xPos += transform().width() - lazyTextWidth.getValue() - textPadding().getRight();
        else
            xPos += textPadding().getLeft() + (transform().width() - textPadding().getHorizontal()) / 2 - lazyTextWidth.getValue() / 2;

        if (textAlignment().isUp())
            yPos += textPadding().getTop();
        else if (textAlignment().isDown())
            yPos += transform().height() - lazyTextHeight.getValue() - textPadding().getBottom();
        else
            yPos += textPadding().getTop() + (transform().height() - textPadding().getVertical()) / 2 - lazyTextHeight.getValue() / 2;

        renderer.getHelper().drawString(
                renderText(),
                xPos,
                yPos,
                transform().zLevel(),
                color(),
                useShadow() ? shadowColor() : Color.ALPHA);
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

    public BaseProperty<Color> shadowColorProperty()
    {
        if (shadowColorProperty == null)
            shadowColorProperty = new BaseProperty<>(Color.WHITE, "shadowColorProperty");
        return shadowColorProperty;
    }

    public BaseProperty<Boolean> useShadowProperty()
    {
        if (useShadowProperty == null)
            useShadowProperty = new BaseProperty<>(false, "useShadowProperty");
        return useShadowProperty;
    }

    public BaseProperty<Color> colorProperty()
    {
        if (colorProperty == null)
            colorProperty = new BaseProperty<>(Color.BLACK, "colorProperty");
        return colorProperty;
    }

    public BaseProperty<String> textProperty()
    {
        return textProperty;
    }

    public BaseProperty<String> renderTextProperty()
    {
        return renderTextProperty;
    }

    public BaseProperty<Integer> lineSpacingProperty()
    {
        return lineSpacingProperty;
    }

    public BaseProperty<RectBox> textPaddingProperty()
    {
        return textPaddingProperty;
    }

    public BaseProperty<RectAlignment> textAlignmentProperty()
    {
        return textAlignmentProperty;
    }

    public BaseProperty<Boolean> multilineProperty()
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

    public Color shadowColor()
    {
        return shadowColorProperty().getValue();
    }

    public void shadowColor(Color shadowColor)
    {
        shadowColorProperty().setValue(shadowColor);
    }

    public boolean useShadow()
    {
        return useShadowProperty().getValue();
    }

    public void useShadow(boolean useShadow)
    {
        useShadowProperty().setValue(useShadow);
    }

    public Color color()
    {
        return colorProperty().getValue();
    }

    public void color(Color color)
    {
        colorProperty().setValue(color);
    }
}