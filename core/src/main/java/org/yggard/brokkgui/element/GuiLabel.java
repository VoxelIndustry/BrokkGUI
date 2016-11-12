package org.yggard.brokkgui.element;

import org.yggard.brokkgui.behavior.GuiBehaviorBase;
import org.yggard.brokkgui.control.GuiLabeled;
import org.yggard.brokkgui.skin.GuiLabeledSkinBase;
import org.yggard.brokkgui.skin.GuiSkinBase;

public class GuiLabel extends GuiLabeled
{
    public GuiLabel(final String text)
    {
        super(text);
    }

    public GuiLabel()
    {
        this("");
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiLabeledSkinBase<>(this, new GuiBehaviorBase<>(this));
    }
}