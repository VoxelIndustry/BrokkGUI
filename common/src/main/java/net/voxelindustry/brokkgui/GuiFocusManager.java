package net.voxelindustry.brokkgui;

import fr.ourten.teabeans.property.Property;
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

    private final Property<GuiElement> focusedNodeProperty;

    private GuiFocusManager()
    {
        focusedNodeProperty = new Property<>(null);
    }

    public GuiElement getFocusedNode()
    {
        return focusedNodeProperty.getValue();
    }

    private void setFocusedNode(GuiElement node)
    {
        focusedNodeProperty.setValue(node);
    }

    public void requestFocus(GuiElement guiElement)
    {
        if (getFocusedNode() != null)
            getFocusedNode().internalSetFocused(false);
        setFocusedNode(guiElement);
        if (getFocusedNode() != null)
            getFocusedNode().internalSetFocused(true);
    }
}