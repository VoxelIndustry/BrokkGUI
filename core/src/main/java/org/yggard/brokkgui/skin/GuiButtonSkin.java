package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.behavior.GuiButtonBehavior;
import org.yggard.brokkgui.element.GuiButton;

public class GuiButtonSkin extends GuiLabeledSkinBase<GuiButton, GuiButtonBehavior<GuiButton>>
{
    public GuiButtonSkin(final GuiButton model, final GuiButtonBehavior<GuiButton> behaviour)
    {
        super(model, behaviour);
    }
}