package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.sprite.Texture;

public class CircleShape implements ShapeDefinition
{
    @Override
    public void drawColored(GuiShape shape, IGuiRenderer renderer, float startX, float startY, Color color,
                            float zLevel, RectBox spritePosition)
    {
        renderer.getHelper().drawColoredCircle(renderer, startX, startY, shape.getWidth(), zLevel, color);
    }

    @Override
    public void drawColoredEmpty(GuiShape shape, IGuiRenderer renderer, float startX, float startY, float lineWidth,
                                 Color color, float zLevel)
    {
        renderer.getHelper().drawColoredEmptyCircle(renderer, startX, startY, shape.getWidth(), zLevel, color,
                lineWidth);
    }

    @Override
    public void drawTextured(GuiShape shape, IGuiRenderer renderer, float startX, float startY, Texture texture,
                             float zLevel, RectBox spritePosition)
    {
        renderer.getHelper().drawTexturedCircle(renderer, startX, startY, texture.getUMin(), texture.getVMin(),
                texture.getUMax(), texture.getVMax(), shape.getWidth(), zLevel);
    }

    @Override
    public boolean isMouseInside(GuiShape shape, float mouseX, float mouseY)
    {
        if (!Rectangle.SHAPE.isMouseInside(shape, mouseX, mouseY))
            return false;

        float pointX = (mouseX - shape.getxPos() - shape.getxTranslate());
        float pointY = (mouseY - shape.getyPos() - shape.getyTranslate());
        return (pointX * pointX) + (pointY * pointY) < shape.getWidth() * shape.getWidth();
    }
}
