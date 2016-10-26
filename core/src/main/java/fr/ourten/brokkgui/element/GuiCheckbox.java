package fr.ourten.brokkgui.element;

import fr.ourten.brokkgui.behavior.GuiButtonBehavior;
import fr.ourten.brokkgui.control.GuiToggleButtonBase;
import fr.ourten.brokkgui.skin.GuiCheckboxSkin;
import fr.ourten.brokkgui.skin.GuiSkinBase;

public class GuiCheckbox extends GuiToggleButtonBase
{
    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiCheckboxSkin(this, new GuiButtonBehavior<>(this));
    }
}