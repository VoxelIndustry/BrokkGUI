package org.yggard.brokkgui.element;

import org.yggard.brokkgui.behavior.GuiButtonBehavior;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.control.GuiButtonBase;
import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.skin.GuiButtonSkin;
import org.yggard.brokkgui.skin.GuiSkinBase;

public class GuiButton extends GuiButtonBase
{
    public GuiButton(String text, GuiNode icon)
    {
        super("button", text, icon);

        this.getLabel().setTextAlignment(EAlignment.MIDDLE_CENTER);
    }

    public GuiButton(String text)
    {
        this(text, null);
    }

    public GuiButton()
    {
        this("");
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiButtonSkin<>(this, new GuiButtonBehavior<>(this));
    }
}