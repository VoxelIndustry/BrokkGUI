package net.voxelindustry.brokkgui;

import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.window.IGuiSubWindow;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Ourten 8 oct. 2016
 */
public class GuiFocusManager
{
    public static final GuiFocusManager instance = new GuiFocusManager();

    private final Property<GuiElement>    focusedNodeProperty   = new Property<>(null);
    private final Property<IGuiSubWindow> focusedWindowProperty = new Property<>(null);

    private final ListProperty<GuiElement> draggedNodesProperty = new ListProperty<>();

    public GuiElement focusedNode()
    {
        return focusedNodeProperty.getValue();
    }

    private void focusedNode(GuiElement node, IGuiSubWindow focusedWindow)
    {
        focusedNodeProperty.setValue(node);
        focusedWindowProperty.setValue(focusedWindow);
    }

    public IGuiSubWindow focusedWindow()
    {
        return focusedWindowProperty.getValue();
    }

    public void requestFocus(GuiElement guiElement, IGuiSubWindow focusedWindow)
    {
        if (focusedNode() != null)
            focusedNode().internalSetFocused(false);
        focusedNode(guiElement, guiElement == null ? null : focusedWindow);
        if (focusedNode() != null)
            focusedNode().internalSetFocused(true);
    }

    public void removeWindowFocus(IGuiSubWindow window)
    {
        if (Objects.equals(focusedWindowProperty.getValue(), window))
        {
            focusedNodeProperty.setValue(null);
            focusedWindowProperty.setValue(null);
        }
    }

    public void removeFocusedNode(GuiElement focusedNode, IGuiSubWindow focusedWindow)
    {
        if (Objects.equals(focusedWindowProperty.getValue(), focusedWindow) && Objects.equals(focusedNodeProperty.getValue(), focusedNode))
        {
            focusedNodeProperty.setValue(null);
            focusedWindowProperty.setValue(null);

            focusedNode.internalSetFocused(false);
        }
    }

    public void draggedNode(GuiElement element)
    {
        draggedNodesProperty.add(element);
    }

    public boolean isDraggedNode(GuiElement element)
    {
        return draggedNodesProperty.contains(element);
    }

    public void removeDraggedNode(GuiElement element)
    {
        draggedNodesProperty.remove(element);
    }

    public Collection<GuiElement> draggedNodes()
    {
        return draggedNodesProperty.getValue();
    }
}