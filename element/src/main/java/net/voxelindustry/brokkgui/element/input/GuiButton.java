package net.voxelindustry.brokkgui.element.input;

import net.voxelindustry.brokkgui.behavior.GuiButtonBehavior;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.control.GuiButtonBase;
import net.voxelindustry.brokkgui.control.GuiLabeled;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.skin.GuiButtonSkin;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

public class GuiButton extends GuiButtonBase
{
    public GuiButton(String text, GuiNode icon)
    {
        super("button", text, icon);

        this.getLabel().setTextAlignment(RectAlignment.MIDDLE_CENTER);
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
    protected GuiLabeled createGuiLabel(String text, GuiNode icon)
    {
        return new GuiLabel(text, icon);
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiButtonSkin<>(this, new GuiButtonBehavior<>(this));
    }
}