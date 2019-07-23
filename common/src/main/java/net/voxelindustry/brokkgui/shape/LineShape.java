package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.util.MouseInBoundsChecker;

import java.util.function.Supplier;

public class LineShape implements ShapeDefinition
{
    private Supplier<Float> lineWidthSupplier;

    public LineShape(Supplier<Float> lineWidthSupplier)
    {
        this.lineWidthSupplier = lineWidthSupplier;
    }

    @Override
    public void drawColored(Transform transform, IGuiRenderer renderer, float startX, float startY, Color color,
                            float zLevel)
    {
        renderer.getHelper().drawColoredLine(renderer, startX, startY,
                startX + transform.width(), startY + transform.height(),
                lineWidthSupplier.get(), zLevel, color);
    }

    @Override
    public void drawColoredEmpty(Transform transform, IGuiRenderer renderer, float startX, float startY,
                                 float lineWidth,
                                 Color color, float zLevel)
    {
        renderer.getHelper().drawColoredLine(renderer, startX, startY,
                startX + transform.width(), startY + transform.height(),
                lineWidthSupplier.get(), zLevel, color);
    }

    @Override
    public void drawTextured(Transform transform, IGuiRenderer renderer, float startX, float startY, Texture texture,
                             float zLevel)
    {
        // TODO : Add helper for drawing textured lines
    }

    @Override
    public boolean isMouseInside(GuiElement element, int mouseX, int mouseY)
    {
        // Bad behavior expected with very high line width and at line's extremities
        if (!MouseInBoundsChecker.DEFAULT.test(element, mouseX, mouseY))
            return false;

        /*
            Let AM = (mouseX - startX, mouseY - startY) a vec2
            Let AB = (transformWidth, transformHeight) a vec2
            dist(M, AB) = |det(AB,AM)| / ||AB||
        */

        Transform transform = element.transform();

        // Vector from segment beginning to mouse position
        float AMx = mouseX - (transform.xPos() + transform.xTranslate());
        float AMy = mouseY - (transform.yPos() + transform.yTranslate());

        float ABx = transform.width();
        float ABy = transform.height();

        // Remember : det(A,B) = Ax * By - Ay * Bx

        double dist = Math.abs(AMx * ABy - AMy * ABx) / Math.sqrt(ABx * ABx + ABy * ABy);

        return dist < lineWidthSupplier.get() / 4d;
    }

}
