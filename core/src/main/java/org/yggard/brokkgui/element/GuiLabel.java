package org.yggard.brokkgui.element;

import org.yggard.brokkgui.behavior.GuiBehaviorBase;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.control.GuiLabeled;
import org.yggard.brokkgui.skin.GuiLabeledSkinBase;
import org.yggard.brokkgui.skin.GuiSkinBase;

public class GuiLabel extends GuiLabeled
{
    public GuiLabel(String text, GuiNode icon)
    {
        super("label", text, icon);
    }

    public GuiLabel(String text)
    {
        this(text, null);
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