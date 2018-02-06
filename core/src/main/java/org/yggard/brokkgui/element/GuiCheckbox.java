package org.yggard.brokkgui.element;

import org.yggard.brokkgui.behavior.GuiTogglableButtonBehavior;
import org.yggard.brokkgui.skin.GuiCheckboxSkin;
import org.yggard.brokkgui.skin.GuiSkinBase;

public class GuiCheckbox extends GuiRadioButton
{
    public GuiCheckbox(final String label)
    {
        super(label);
        this.setType("checkbox");
    }

    public GuiCheckbox()
    {
        this("");
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiCheckboxSkin(this, new GuiTogglableButtonBehavior<>(this));
    }
}