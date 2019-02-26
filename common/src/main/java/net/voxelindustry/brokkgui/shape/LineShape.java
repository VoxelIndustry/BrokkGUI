package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.Texture;

import java.util.function.Supplier;

public class LineShape implements ShapeDefinition
{
    private Supplier<Float> lineWidthSupplier;

    public LineShape(Supplier<Float> lineWidthSupplier)
    {
        this.lineWidthSupplier = lineWidthSupplier;
    }

    @Override
    public void drawColored(GuiShape shape, IGuiRenderer renderer, float startX, float startY, Color color,
                            float zLevel)
    {
        renderer.getHelper().drawColoredLine(renderer, startX, startY,
                startX + shape.getWidth(), startY + shape.getHeight(),
                lineWidthSupplier.get(), zLevel, color);
    }

    @Override
    public void drawColoredEmpty(GuiShape shape, IGuiRenderer renderer, float startX, float startY, float lineWidth,
                                 Color color, float zLevel)
    {
        renderer.getHelper().drawColoredLine(renderer, startX, startY,
                startX + shape.getWidth(), startY + shape.getHeight(),
                lineWidthSupplier.get(), zLevel, color);
    }

    @Override
    public void drawTextured(GuiShape shape, IGuiRenderer renderer, float startX, float startY, Texture texture,
                             float zLevel)
    {
        // TODO : Add helper for drawing textured lines
    }

    @Override
    public boolean isMouseInside(GuiShape shape, int mouseX, int mouseY)
    {
        // TODO : Impl a check for non-axis-aligned boxes
        return false;
    }
}
