package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.Transform;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.Texture;

public class RectangleShape implements ShapeDefinition
{
    @Override
    public void drawColored(Transform transform, IGuiRenderer renderer, float startX, float startY, Color color,
                            float zLevel)
    {
        renderer.getHelper().drawColoredRect(renderer, startX, startY, transform.width(), transform.height(),
                zLevel, color);
    }

    @Override
    public void drawColoredEmpty(Transform transform, IGuiRenderer renderer, float startX, float startY,
                                 float lineWidth,
                                 Color color, float zLevel)
    {
        renderer.getHelper().drawColoredEmptyRect(renderer, startX, startY, transform.width(), transform.height(),
                zLevel, color, lineWidth);
    }

    @Override
    public void drawTextured(Transform transform, IGuiRenderer renderer, float startX, float startY, Texture texture,
                             float zLevel)
    {
        renderer.getHelper().drawTexturedRect(renderer, startX, startY, texture.getUMin(), texture.getVMin(),
                texture.getUMax(), texture.getVMax(), transform.width(), transform.height(), zLevel);
    }

    @Override
    public boolean isMouseInside(GuiElement element, int mouseX, int mouseY)
    {
        Transform transform = element.transform();

        return mouseX > transform.leftPos() && mouseX < transform.rightPos() &&
                mouseY > transform.topPos() && mouseY < transform.bottomPos();
    }
}
