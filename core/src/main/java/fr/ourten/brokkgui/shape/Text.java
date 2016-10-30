package fr.ourten.brokkgui.shape;

import fr.ourten.brokkgui.data.EAlignment;
import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.EGuiRenderPass;
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
        return this.textProperty.getValue();
    }

    public void setText(final String text)
    {
        this.textProperty.setValue(text);
    }

    public int getLineSpacing()
    {
        return this.lineSpacingProperty.getValue();
    }

    public void setLineSpacing(final int lineSpacing)
    {
        this.lineSpacingProperty.setValue(lineSpacing);
    }

    public EAlignment getTextAlignment()
    {
        return this.textAlignmentProperty.getValue();
    }

    public void setTextAlignment(final EAlignment textAlignment)
    {
        this.textAlignmentProperty.setValue(textAlignment);
    }

    public boolean hasShadow()
    {
        return this.shadowProperty.getValue();
    }

    public void setShadow(final boolean hasShadow)
    {
        this.shadowProperty.setValue(hasShadow);
    }

    public boolean wrapText()
    {
        return this.wrapTextProperty.getValue();
    }

    public void setWrapText(final boolean wrapText)
    {
        this.wrapTextProperty.setValue(wrapText);
    }
}