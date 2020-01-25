package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.sprite.Texture;

public interface ShapeDefinition
{
    void drawColored(GuiShape shape, IGuiRenderer renderer, float startX, float startY, Color color, float zLevel);

    void drawColoredEmpty(GuiShape shape, IGuiRenderer renderer, float startX, float startY, float lineWidth,
                          Color color, float zLevel);

    void drawTextured(GuiShape shape, IGuiRenderer renderer, float startX, float startY, Texture texture, float zLevel);

    boolean isMouseInside(GuiShape shape, int mouseX, int mouseY);
}
