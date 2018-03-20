package org.yggard.brokkgui.shape;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.RenderPass;

public class Text extends GuiShape
{
    private final BaseProperty<String>     textProperty;
    private final BaseProperty<Integer>    lineSpacingProperty;

    public Text(final float posX, final float posY, final String text)
    {
        super("text");

        this.setxTranslate(posX);
        this.setyTranslate(posY);

        this.textProperty = new BaseProperty<>(text, "textProperty");
        this.lineSpacingProperty = new BaseProperty<>(1, "lineSpacingProperty");

        this.getStyle().registerProperty("-shadow-color", Color.WHITE, Color.class);
        this.getStyle().registerProperty("-shadow", true, Boolean.class);
    }

    public Text(final String text)
    {
        this(0, 0, text);
    }

    @Override
    public void renderContent(final IGuiRenderer renderer, final RenderPass pass, final int mouseX, final int mouseY)
    {
        if (pass == RenderPass.MAIN)
        {
            if (this.getLineWeight() > 0)
                renderer.getHelper().drawColoredEmptyRect(renderer, this.getxPos() + this.getxTranslate(),
                        this.getyPos() + this.getyTranslate(), this.getWidth(), this.getHeight(), this.getzLevel(),
                        this.getLineColor(), this.getLineWeight());

            renderer.getHelper().drawString(this.getText(), (int) (this.getxPos() + this.getxTranslate()),
                    (int) (this.getyPos() + this.getyTranslate()), this.getzLevel(),
                    this.getColor(), this.useShadow() ? this.getShadowColor() : Color.ALPHA);
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

    public Color getShadowColor()
    {
        return this.getStyle().getStyleProperty("-shadow-color", Color.class).getValue();
    }

    public boolean useShadow()
    {
        return this.getStyle().getStyleProperty("-shadow", Boolean.class).getValue();
    }
}