package net.voxelindustry.brokkgui;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiElement;

/**
 * @author Ourten 8 oct. 2016
 */
public class GuiFocusManager
{
    private static volatile GuiFocusManager INSTANCE;

    public static GuiFocusManager instance()
    {
        if (GuiFocusManager.INSTANCE == null)
            synchronized (GuiFocusManager.class)
            {
                if (GuiFocusManager.INSTANCE == null)
                    GuiFocusManager.INSTANCE = new GuiFocusManager();
            }
        return GuiFocusManager.INSTANCE;
    }

    private final BaseProperty<GuiElement> focusedNodeProperty;

    private GuiFocusManager()
    {
        this.focusedNodeProperty = new BaseProperty<>(null, "focusedNodeProperty");
    }

    public GuiElement getFocusedNode()
    {
        return this.focusedNodeProperty.getValue();
    }

    private void setFocusedNode(final GuiElement node)
    {
        this.focusedNodeProperty.setValue(node);
    }

    public void requestFocus(final GuiElement guiElement)
    {
        if (this.getFocusedNode() != null)
            this.getFocusedNode().internalSetFocused(false);
        this.setFocusedNode(guiElement);
        if (this.getFocusedNode() != null)
            this.getFocusedNode().internalSetFocused(true);
    }
}