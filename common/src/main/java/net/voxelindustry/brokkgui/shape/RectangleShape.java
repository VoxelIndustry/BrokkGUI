package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.sprite.Texture;

public class RectangleShape implements ShapeDefinition
{
    @Override
    public void drawColored(GuiShape shape, IGuiRenderer renderer, float startX, float startY, Color color,
                            float zLevel, RectBox spritePosition)
    {
        if (spritePosition == RectBox.EMPTY)
            renderer.getHelper().drawColoredRect(renderer, startX, startY, shape.getWidth(), shape.getHeight(), zLevel,
                    color);
        else
            renderer.getHelper().drawColoredRect(renderer,
                    startX + spritePosition.getLeft(),
                    startY + spritePosition.getTop(),
                    shape.getWidth() - spritePosition.getHorizontal(),
                    shape.getHeight() - spritePosition.getVertical(),
                    zLevel, color);
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
                             float zLevel, RectBox spritePosition)
    {
        if (spritePosition == RectBox.EMPTY)
            renderer.getHelper().drawTexturedRect(renderer, startX, startY, texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(), shape.getWidth(), shape.getHeight(), zLevel);
        else
            renderer.getHelper().drawTexturedRect(renderer,
                    startX + spritePosition.getLeft(),
                    startY + spritePosition.getTop(),
                    texture.getUMin(), texture.getVMin(),
                    texture.getUMax(), texture.getVMax(),
                    shape.getWidth() - spritePosition.getHorizontal(),
                    shape.getHeight() - spritePosition.getVertical(),
                    zLevel);
    }

    @Override
    public boolean isMouseInside(GuiShape shape, float mouseX, float mouseY)
    {
        return mouseX > shape.getxPos() + shape.getxTranslate()
                && mouseX < shape.getxPos() + shape.getxTranslate() + shape.getWidth()
                && mouseY > shape.getyPos() + shape.getyTranslate()
                && mouseY < shape.getyPos() + shape.getyTranslate() + shape.getHeight();
    }
}
