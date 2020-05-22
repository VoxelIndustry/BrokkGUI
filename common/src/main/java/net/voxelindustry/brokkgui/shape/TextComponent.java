package net.voxelindustry.brokkgui.shape;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.RenderComponent;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class TextComponent extends GuiComponent implements RenderComponent
{
    private final BaseProperty<String>  textProperty;
    private final BaseProperty<Integer> lineSpacingProperty;

    protected BaseProperty<Color>   shadowColorProperty;
    protected BaseProperty<Boolean> useShadowProperty;
    protected BaseProperty<Color>   colorProperty;

    public TextComponent()
    {
        textProperty = new BaseProperty<>("", "textProperty");
        lineSpacingProperty = new BaseProperty<>(1, "lineSpacingProperty");
    }

    @Override
    public void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (pass == RenderPass.MAIN)
        {
            renderer.getHelper().drawString(
                    text(),
                    transform().leftPos(),
                    transform().topPos(),
                    transform().zLevel(),
                    color(),
                    useShadow() ? shadowColor() : Color.ALPHA);
        }
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

    public BaseProperty<Integer> lineSpacingProperty()
    {
        return lineSpacingProperty;
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

    public int lineSpacing()
    {
        return lineSpacingProperty().getValue();
    }

    public void lineSpacing(int lineSpacing)
    {
        lineSpacingProperty().setValue(lineSpacing);
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