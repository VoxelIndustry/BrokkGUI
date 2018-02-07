package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.behavior.GuiButtonBehavior;
import org.yggard.brokkgui.control.GuiButtonBase;

public class GuiButtonSkin<C extends GuiButtonBase, B extends GuiButtonBehavior<C>> extends GuiLabeledSkinBase<C, B>
{
    public GuiButtonSkin(final C model, final B behaviour)
    {
        super(model, behaviour);
    }
}