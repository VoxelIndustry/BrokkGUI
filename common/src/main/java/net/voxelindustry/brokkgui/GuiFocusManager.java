package net.voxelindustry.brokkgui;

import net.voxelindustry.brokkgui.component.GuiElement;

public class GuiFocusManager
{
    private static GuiFocusManager INSTANCE;

    public static GuiFocusManager instance()
    {
        if (GuiFocusManager.INSTANCE == null)
            GuiFocusManager.INSTANCE = new GuiFocusManager();
        return GuiFocusManager.INSTANCE;
    }

    private GuiElement focusedNode;

    private GuiFocusManager()
    {
    }

    public GuiElement focusedNode()
    {
        return this.focusedNode;
    }

    private void focusedNode(GuiElement element)
    {
        this.focusedNode = element;
    }

    public void requestFocus(GuiElement element)
    {
        if (this.focusedNode() != null)
            this.focusedNode().internalSetFocused(false);

        this.focusedNode(element);

        if (this.focusedNode() != null)
            this.focusedNode().internalSetFocused(true);
    }
}