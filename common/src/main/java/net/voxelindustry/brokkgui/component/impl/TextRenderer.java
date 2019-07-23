package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.RenderComponent;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class TextRenderer extends GuiComponent implements RenderComponent
{
    private Text      text;
    private Transform transform;

    protected BaseProperty<Color>   colorProperty;
    protected BaseProperty<Color>   shadowColorProperty;
    protected BaseProperty<Boolean> shadowProperty;

    public TextRenderer()
    {
    }

    @Override
    public void attach(GuiElement element)
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

    ////////////////
    // PROPERTIES //
    ////////////////

    public BaseProperty<Color> colorProperty()
    {
        if (colorProperty == null)
            colorProperty = new BaseProperty<>(Color.WHITE, "colorProperty");
        return colorProperty;
    }

    public BaseProperty<Color> shadowColorProperty()
    {
        if (shadowColorProperty == null)
            shadowColorProperty = new BaseProperty<>(Color.BLACK, "shadowColorProperty");
        return shadowColorProperty;
    }

    public BaseProperty<Boolean> shadowProperty()
    {
        if (shadowProperty == null)
            shadowProperty = new BaseProperty<>(false, "shadowProperty");
        return shadowProperty;
    }

    ////////////
    // VALUES //
    ////////////

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
