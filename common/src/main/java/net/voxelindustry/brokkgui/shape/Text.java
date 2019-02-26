package net.voxelindustry.brokkgui.shape;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class Text extends GuiShape
{
    private final BaseProperty<String>  textProperty;
    private final BaseProperty<Integer> lineSpacingProperty;

    public Text(float posX, float posY, String text)
    {
        super("text", Rectangle.SHAPE);

        this.setxTranslate(posX);
        this.setyTranslate(posY);

        this.textProperty = new BaseProperty<>(text, "textProperty");
        this.lineSpacingProperty = new BaseProperty<>(1, "lineSpacingProperty");

        this.getStyle().registerProperty("shadow-color", Color.WHITE, Color.class);
        this.getStyle().registerProperty("shadow", true, Boolean.class);

        this.getStyle().registerProperty("color", Color.BLACK, Color.class);
    }

    public Text(final String text)
    {
        this(0, 0, text);
    }

    @Override
    public void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        super.renderContent(renderer, pass, mouseX, mouseY);

        if (pass == RenderPass.MAIN)
        {
            renderer.getHelper().drawString(this.getText(), this.getxPos() + this.getxTranslate(),
                    this.getyPos() + this.getyTranslate(), this.getzLevel(),
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
        return this.getStyle().getStyleProperty("shadow-color", Color.class).getValue();
    }

    public boolean useShadow()
    {
        return this.getStyle().getStyleProperty("shadow", Boolean.class).getValue();
    }

    public Color getColor()
    {
        return this.getStyle().getStyleProperty("color", Color.class).getValue();
    }
}