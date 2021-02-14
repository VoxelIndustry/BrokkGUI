package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.sprite.Texture;

import java.util.function.Supplier;

public class LineShape implements ShapeDefinition
{
    private Supplier<Float> lineWidthSupplier;

    public LineShape(Supplier<Float> lineWidthSupplier)
    {
        this.lineWidthSupplier = lineWidthSupplier;
    }

    @Override
    public void drawColored(Transform transform, IRenderCommandReceiver renderer, float startX, float startY, Color color,
                            float zLevel, RectBox spritePosition)
    {
        renderer.drawColoredLine(startX, startY,
                startX + transform.width(), startY + transform.height(),
                lineWidthSupplier.get(), zLevel, color, RenderPass.BACKGROUND);
    }

    @Override
    public void drawColoredEmpty(Transform transform, IRenderCommandReceiver renderer, float startX, float startY, float lineWidth,
                                 Color color, float zLevel)
    {
        renderer.drawColoredLine(startX, startY,
                startX + transform.width(), startY + transform.height(),
                lineWidthSupplier.get(), zLevel, color, RenderPass.BACKGROUND);
    }

    @Override
    public void drawTextured(Transform transform, IRenderCommandReceiver renderer, float startX, float startY, Texture texture,
                             float zLevel, RectBox spritePosition)
    {
        // TODO : Add helper for drawing textured lines
    }

    @Override
    public boolean isMouseInside(GuiElement element, float mouseX, float mouseY)
    {
        if (!Rectangle.SHAPE.isMouseInside(element, mouseX, mouseY)) // Bad behavior expected with very high line width
            return false;                                         // and at line's extremities

        /*
            Let AM = (mouseX - startX, mouseY - startY) a vec2
            Let AB = (shapeWidth, shapeHeight) a vec2
            dist(M, AB) = |det(AB,AM)| / ||AB||
        */

        // Vector from segment beginning to mouse position
        float AMx = mouseX - (element.transform().xPos() + element.transform().xTranslate());
        float AMy = mouseY - (element.transform().yPos() + element.transform().yTranslate());

        float ABx = element.transform().width();
        float ABy = element.transform().height();

        // Remember : det(A,B) = Ax * By - Ay * Bx

        double dist = Math.abs(AMx * ABy - AMy * ABx) / Math.sqrt(ABx * ABx + ABy * ABy);

        return dist < lineWidthSupplier.get() / 4d;
    }

}
