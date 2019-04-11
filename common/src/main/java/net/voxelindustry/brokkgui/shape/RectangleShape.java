package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.Texture;

public class RectangleShape implements ShapeDefinition
{
    @Override
    public void drawColored(GuiShape shape, IGuiRenderer renderer, float startX, float startY, Color color,
                            float zLevel)
    {
        renderer.getHelper().drawColoredRect(renderer, startX, startY, shape.getWidth(), shape.getHeight(), zLevel,
                color);
    }

    @Override
    public void drawColoredEmpty(GuiShape shape, IGuiRenderer renderer, float startX, float startY, float lineWidth,
                                 Color color, float zLevel)
    {
        renderer.getHelper().drawColoredEmptyRect(renderer, startX, startY, shape.getWidth(), shape.getHeight(),
                zLevel, color, lineWidth);
    }

    @Override
    public void drawTextured(GuiShape shape, IGuiRenderer renderer, float startX, float startY, Texture texture,
                             float zLevel)
    {
        renderer.getHelper().drawTexturedRect(renderer, startX, startY, texture.getUMin(), texture.getVMin(),
                texture.getUMax(), texture.getVMax(), shape.getWidth(), shape.getHeight(), zLevel);
    }

    @Override
    public boolean isMouseInside(GuiShape shape, int mouseX, int mouseY)
    {
        return mouseX > shape.getxPos() + shape.getxTranslate() && mouseX < shape.getxPos() + shape.getxTranslate() + shape.getWidth() &&
                mouseY > shape.getyPos() + shape.getyTranslate() && mouseY < shape.getyPos() + shape.getyTranslate() + shape.getHeight();
    }
}
