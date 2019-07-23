package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.Texture;

public interface ShapeDefinition
{
    void drawColored(Transform transform, IGuiRenderer renderer, float startX, float startY, Color color, float zLevel);

    void drawColoredEmpty(Transform transform, IGuiRenderer renderer, float startX, float startY, float lineWidth,
                          Color color, float zLevel);

    void drawTextured(Transform transform, IGuiRenderer renderer, float startX, float startY, Texture texture, float zLevel);

    boolean isMouseInside(GuiElement element, int mouseX, int mouseY);
}
