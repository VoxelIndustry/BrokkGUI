package org.yggard.brokkgui;

import org.yggard.brokkgui.component.GuiNode;

import fr.ourten.teabeans.value.BaseProperty;

/**
 * @author Ourten 8 oct. 2016
 */
public class GuiFocusManager
{
    private static volatile GuiFocusManager INSTANCE;

    public static GuiFocusManager getInstance()
    {
        if (GuiFocusManager.INSTANCE == null)
            synchronized (GuiFocusManager.class)
            {
                if (GuiFocusManager.INSTANCE == null)
                    GuiFocusManager.INSTANCE = new GuiFocusManager();
            }
        return GuiFocusManager.INSTANCE;
    }

    private final BaseProperty<GuiNode> focusedNodeProperty;

    private GuiFocusManager()
    {
        this.focusedNodeProperty = new BaseProperty<>(null, "focusedNodeProperty");
    }

    public GuiNode getFocusedNode()
    {
        return this.focusedNodeProperty.getValue();
    }

    private void setFocusedNode(final GuiNode node)
    {
        this.focusedNodeProperty.setValue(node);
    }

    public void requestFocus(final GuiNode guiNode)
    {
        if (this.getFocusedNode() != null)
            this.getFocusedNode().internalSetFocused(false);
        this.setFocusedNode(guiNode);
        if (this.getFocusedNode() != null)
            this.getFocusedNode().internalSetFocused(true);
    }
}