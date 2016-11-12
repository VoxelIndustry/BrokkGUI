package org.yggard.brokkgui.element;

import org.yggard.brokkgui.behavior.GuiButtonBehavior;
import org.yggard.brokkgui.control.GuiToggleButtonBase;
import org.yggard.brokkgui.skin.GuiCheckboxSkin;
import org.yggard.brokkgui.skin.GuiSkinBase;

public class GuiCheckbox extends GuiToggleButtonBase
{
    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiCheckboxSkin(this, new GuiButtonBehavior<>(this));
    }
}