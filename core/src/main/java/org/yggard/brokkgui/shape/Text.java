package org.yggard.brokkgui.shape;

import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.EGuiRenderPass;

import fr.ourten.teabeans.value.BaseProperty;

public class Text extends GuiShape
{
    private final BaseProperty<EAlignment> textAlignmentProperty;
    private final BaseProperty<Boolean>    shadowProperty, wrapTextProperty;
    private final BaseProperty<String>     textProperty;
    private final BaseProperty<Integer>    lineSpacingProperty;

    public Text(final float posX, final float posY, final String text)
    {
        this.setxTranslate(posY);
        this.setyTranslate(posY);

        this.textProperty = new BaseProperty<>(text, "textProperty");
        this.lineSpacingProperty = new BaseProperty<>(1, "lineSpacingProperty");
        this.textAlignmentProperty = new BaseProperty<>(EAlignment.MIDDLE_CENTER, "textAlignmentProperty");
        this.shadowProperty = new BaseProperty<>(true, "shadowProperty");
        this.wrapTextProperty = new BaseProperty<>(false, "wrapTextProperty");
    }

    public Text(final String text)
    {
        this(0, 0, text);
    }

    @Override
    public void renderNode(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        if (pass == EGuiRenderPass.MAIN)
        {
            if (!this.wrapText())
                renderer.getHelper().drawString(this.getText(), (int) (this.getxPos() + this.getxTranslate()),
                        (int) (this.getyPos() + this.getyTranslate()), this.getzLevel(), this.getColor(),
                        this.getTextAlignment(), this.hasShadow());
            else
                renderer.getHelper().drawString(
                        renderer.getHelper().trimStringToPixelWidth(this.getText(), (int) this.getWidth()),
                        (int) (this.getxPos() + this.getxTranslate()), (int) (this.getyPos() + this.getyTranslate()),
                        this.getzLevel(), this.getColor(), this.getTextAlignment(), this.hasShadow());
        }
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

    public BaseProperty<Boolean> getShadowProperty()
    {
        return this.shadowProperty;
    }

    public BaseProperty<Boolean> getWrapTextProperty()
    {
        return this.wrapTextProperty;
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

    public boolean hasShadow()
    {
        return this.getShadowProperty().getValue();
    }

    public void setShadow(final boolean hasShadow)
    {
        this.getShadowProperty().setValue(hasShadow);
    }

    public boolean wrapText()
    {
        return this.getWrapTextProperty().getValue();
    }

    public void setWrapText(final boolean wrapText)
    {
        this.getWrapTextProperty().setValue(wrapText);
    }
}