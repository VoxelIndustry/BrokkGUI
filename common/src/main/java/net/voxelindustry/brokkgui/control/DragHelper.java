package net.voxelindustry.brokkgui.control;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.data.RectArea;
import net.voxelindustry.brokkgui.event.GuiMouseEvent;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class DragHelper
{
    public static void setDraggable(GuiNode node)
    {
        setDraggable(node, null, null);
    }

    public static void setDraggable(GuiNode node, @Nullable RectArea nodeDragStart, @Nullable RectArea dragArea)
    {
        AtomicBoolean isDragged = new AtomicBoolean(false);

        node.getEventDispatcher().addHandler(GuiMouseEvent.DRAG_START, e ->
        {
            if (nodeDragStart == null || nodeDragStart.isPointInside(e.getMouseX(), e.getMouseY()))
                isDragged.set(true);
        });
        node.getEventDispatcher().addHandler(GuiMouseEvent.DRAGGING, e ->
        {
            if (isDragged.get() && (dragArea == null || dragArea.isPointInside(e.getMouseX(), e.getMouseY())))
            {
                node.setxTranslate(e.getMouseX());
                node.setyTranslate(e.getMouseY());
            }
        });
        node.getEventDispatcher().addHandler(GuiMouseEvent.DRAG_STOP, e -> isDragged.set(false));
    }
}
