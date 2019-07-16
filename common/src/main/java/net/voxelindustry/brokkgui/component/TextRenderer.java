package net.voxelindustry.brokkgui.component;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class TextRenderer extends GuiComponent implements RenderComponent
{
    private Text      text;
    private Transform transform;

    private final BaseProperty<Color>   colorProperty;
    private final BaseProperty<Color>   shadowColorProperty;
    private final BaseProperty<Boolean> shadowProperty;

    public TextRenderer()
    {
        colorProperty = new BaseProperty<>(Color.BLACK, "colorProperty");
        shadowColorProperty = new BaseProperty<>(Color.WHITE, "shadowColorProperty");

        shadowProperty = new BaseProperty<>(true, "shadowProperty");
    }

    @Override
    protected void attach(GuiElement element)
    {
        super.attach(element);

        if (!element.has(Text.class))
            throw new GuiComponentException(
                    "TextRenderer must be added to a GuiElement already containing the component Text!");

        this.text = element.get(Text.class);
        this.transform = element.transform();
    }

    @Override
    public void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (pass == RenderPass.MAIN)
        {
            renderer.getHelper().drawString(text.text(), transform.leftPos(),
                    transform.topPos(), transform.zLevel(),
                    color(), this.shadow() ? this.shadowColor() : Color.ALPHA);
        }
    }

    public BaseProperty<Color> colorProperty()
    {
        return colorProperty;
    }

    public BaseProperty<Color> shadowColorProperty()
    {
        return shadowColorProperty;
    }

    public BaseProperty<Boolean> shadowProperty()
    {
        return shadowProperty;
    }

    public Color color()
    {
        return colorProperty().getValue();
    }

    public void color(Color color)
    {
        colorProperty().setValue(color);
    }

    public Color shadowColor()
    {
        return shadowColorProperty().getValue();
    }

    public void shadowColor(Color color)
    {
        shadowColorProperty().setValue(color);
    }

    public boolean shadow()
    {
        return shadowProperty().getValue();
    }

    public void shadow(boolean shadow)
    {
        shadowProperty().setValue(shadow);
    }
}
