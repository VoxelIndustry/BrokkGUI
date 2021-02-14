package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.sprite.Texture;

public class RectangleShape implements ShapeDefinition
{
    public static final RectangleShape RECTANGLE_SHAPE = new RectangleShape();

    @Override
    public void drawColored(Transform transform, IRenderCommandReceiver renderer, float startX, float startY, Color color,
                            float zLevel, RectBox spritePosition)
    {
        if (spritePosition == RectBox.EMPTY)
            renderer.drawColoredRect(startX, startY, transform.width(), transform.height(), zLevel,
                    color, RenderPass.BACKGROUND);
        else
            renderer.drawColoredRect(startX + spritePosition.getLeft(),
                    startY + spritePosition.getTop(),
                    transform.width() - spritePosition.getHorizontal(),
                    transform.height() - spritePosition.getVertical(),
                    zLevel, color, RenderPass.BACKGROUND);
    }

    @Override
    public void drawColoredEmpty(Transform shape, IRenderCommandReceiver renderer, float startX, float startY, float lineWidth,
                                 Color color, float zLevel)
    {
        renderer.drawColoredEmptyRect(startX, startY, shape.width(), shape.height(),
                zLevel, color, lineWidth, RenderPass.BACKGROUND);
    }

    @Override
    public void drawTextured(Transform shape, IRenderCommandReceiver renderer, float startX, float startY, Texture texture,
                             float zLevel, RectBox spritePosition)
    {
        if (spritePosition == RectBox.EMPTY)
            renderer.drawTexturedRect(startX, startY, texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(), shape.width(), shape.height(), zLevel, RenderPass.BACKGROUND);
        else
            renderer.drawTexturedRect(startX + spritePosition.getLeft(),
                    startY + spritePosition.getTop(),
                    texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(),
                    shape.width() - spritePosition.getHorizontal(),
                    shape.height() - spritePosition.getVertical(),
                    zLevel,
                    RenderPass.BACKGROUND);
    }

    @Override
    public boolean isMouseInside(GuiElement element, float mouseX, float mouseY)
    {
        if (mouseX >= element.transform().leftPos()
                && mouseX < element.transform().rightPos()
                && mouseY >= element.transform().topPos()
                && mouseY < element.transform().bottomPos())
            return true;
        return false;
    }
}
