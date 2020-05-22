package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.sprite.Texture;

public interface ShapeDefinition
{
    void drawColored(Transform transform, IGuiRenderer renderer, float startX, float startY, Color color, float zLevel, RectBox spritePosition);

    void drawColoredEmpty(Transform transform, IGuiRenderer renderer, float startX, float startY, float lineWidth,
                          Color color, float zLevel);

    void drawTextured(Transform shape, IGuiRenderer renderer, float startX, float startY, Texture texture, float zLevel, RectBox spritePosition);

    boolean isMouseInside(GuiElement element, float mouseX, float mouseY);
}
