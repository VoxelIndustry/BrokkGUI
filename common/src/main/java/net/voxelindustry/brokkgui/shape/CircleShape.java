package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.sprite.Texture;

public class CircleShape implements ShapeDefinition
{
    @Override
    public void drawColored(Transform transform, IGuiRenderer renderer, float startX, float startY, Color color,
                            float zLevel, RectBox spritePosition)
    {
        renderer.getHelper().drawColoredCircle(renderer, startX, startY, transform.width(), zLevel, color);
    }

    @Override
    public void drawColoredEmpty(Transform transform, IGuiRenderer renderer, float startX, float startY, float lineWidth,
                                 Color color, float zLevel)
    {
        renderer.getHelper().drawColoredEmptyCircle(renderer, startX, startY, transform.width(), zLevel, color,
                lineWidth);
    }

    @Override
    public void drawTextured(Transform transform, IGuiRenderer renderer, float startX, float startY, Texture texture,
                             float zLevel, RectBox spritePosition)
    {
        renderer.getHelper().drawTexturedCircle(renderer, startX, startY, texture.getUMin(), texture.getVMin(),
                texture.getUMax(), texture.getVMax(), transform.width(), zLevel);
    }

    @Override
    public boolean isMouseInside(GuiElement element, float mouseX, float mouseY)
    {
        if (!Rectangle.SHAPE.isMouseInside(element, mouseX, mouseY))
            return false;

        float pointX = (mouseX - element.transform().xPos() - element.transform().xTranslate());
        float pointY = (mouseY - element.transform().yPos() - element.transform().yTranslate());
        return (pointX * pointX) + (pointY * pointY) < element.width() * element.width();
    }
}
