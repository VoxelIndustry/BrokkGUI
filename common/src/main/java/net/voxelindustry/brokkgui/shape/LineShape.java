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
    public void drawBorder(GuiShape shape, IGuiRenderer renderer)
    {

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
        if(!Rectangle.SHAPE.isMouseInside(shape, mouseX, mouseY)) // Bad behavior expected with very high line width
            return false;                                         // and at line's extremities

        /*
            Let AM = (mouseX - startX, mouseY - startY) a vec2
            Let AB = (shapeWidth, shapeHeight) a vec2
            dist(M, AB) = |det(AB,AM)| / ||AB||
        */

        // Vector from segment beginning to mouse position
        float AMx = mouseX - (shape.getxPos() + shape.getxTranslate());
        float AMy = mouseY - (shape.getyPos() + shape.getyTranslate());

        float ABx = shape.getWidth();
        float ABy = shape.getHeight();

        // Remember : det(A,B) = Ax * By - Ay * Bx

        double dist = Math.abs(AMx * ABy - AMy * ABx) / Math.sqrt(ABx * ABx + ABy * ABy);

        return dist < lineWidthSupplier.get() / 4d;
    }

}
