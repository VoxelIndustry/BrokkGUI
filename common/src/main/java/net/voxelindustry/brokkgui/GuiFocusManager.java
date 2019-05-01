package net.voxelindustry.brokkgui;

import net.voxelindustry.brokkgui.exp.component.GuiElement;

public class GuiFocusManager
{
    private static GuiFocusManager INSTANCE;

    public static GuiFocusManager getInstance()
    {
        if (GuiFocusManager.INSTANCE == null)
            GuiFocusManager.INSTANCE = new GuiFocusManager();
        return GuiFocusManager.INSTANCE;
    }

    private GuiElement focusedNode;

    private GuiFocusManager()
    {
    }

    public GuiElement getFocusedNode()
    {
        return this.focusedNode;
    }

    private void setFocusedNode(GuiElement element)
    {
        this.focusedNode = element;
    }

    public void requestFocus(GuiElement element)
    {
        if (this.getFocusedNode() != null)
            this.getFocusedNode().internalSetFocused(false);

        this.setFocusedNode(element);

        if (this.getFocusedNode() != null)
            this.getFocusedNode().internalSetFocused(true);
    }
}