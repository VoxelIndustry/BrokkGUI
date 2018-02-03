package org.yggard.brokkgui.shape;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.paint.TextStyle;

public class Text extends GuiShape
{
    private final BaseProperty<EAlignment> textAlignmentProperty;
    private final BaseProperty<TextStyle>  textStyleProperty;
    private final BaseProperty<String>     textProperty;
    private final BaseProperty<Integer>    lineSpacingProperty;

    public Text(final float posX, final float posY, final String text)
    {
        super("text");

        this.setxTranslate(posX);
        this.setyTranslate(posY);

        this.textProperty = new BaseProperty<>(text, "textProperty");
        this.lineSpacingProperty = new BaseProperty<>(1, "lineSpacingProperty");
        this.textAlignmentProperty = new BaseProperty<>(EAlignment.MIDDLE_CENTER, "textAlignmentProperty");
        this.textStyleProperty = new BaseProperty<>(new TextStyle(null), "textStyleProperty");
    }

    public Text(final String text)
    {
        this(0, 0, text);
    }

    @Override
    public void renderContent(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        if (pass == EGuiRenderPass.MAIN)
            renderer.getHelper().drawString(this.getText(), (int) (this.getxPos() + this.getxTranslate()),
                    (int) (this.getyPos() + this.getyTranslate()), this.getzLevel(),
                    this.getTextStyle().getTextColor(), this.getTextStyle().useShadow() ? this.getTextStyle()
                            .getShadowColor() : Color.ALPHA, this.getTextAlignment());
    }

    public BaseProperty<String> getTextProperty()
    {
        return this.textProperty;
    }

    public BaseProperty<Integer> getLineSpacingProperty()
    {
        return this.lineSpacingProperty;
    }

    public BaseProperty<EAlignment> getTextAlignmentProperty()
    {
        return this.textAlignmentProperty;
    }

    public BaseProperty<TextStyle> getTextStyleProperty()
    {
        return this.textStyleProperty;
    }

    public String getText()
    {
        return this.getTextProperty().getValue();
    }

    public void setText(final String text)
    {
        this.getTextProperty().setValue(text);
    }

    public int getLineSpacing()
    {
        return this.getLineSpacingProperty().getValue();
    }

    public void setLineSpacing(final int lineSpacing)
    {
        this.getLineSpacingProperty().setValue(lineSpacing);
    }

    public EAlignment getTextAlignment()
    {
        return this.getTextAlignmentProperty().getValue();
    }

    public void setTextAlignment(final EAlignment textAlignment)
    {
        this.getTextAlignmentProperty().setValue(textAlignment);
    }

    public TextStyle getTextStyle()
    {
        return this.getTextStyleProperty().getValue();
    }

    public void setTextStyle(final TextStyle textStyle)
    {
        this.getTextStyleProperty().setValue(textStyle);
    }
}