package net.voxelindustry.brokkgui.element.input;

import net.voxelindustry.brokkgui.behavior.GuiButtonBehavior;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiButtonBase;
import net.voxelindustry.brokkgui.control.GuiLabeled;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.skin.GuiButtonSkin;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

public class GuiButton extends GuiButtonBase
{
    public GuiButton(String text, GuiElement icon)
    {
        super(text, icon);

        getLabel().setTextAlignment(RectAlignment.MIDDLE_CENTER);
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
    public String type()
    {
        return "button";
    }

    @Override
    protected GuiLabeled createGuiLabel(String text, GuiElement icon)
    {
        return new GuiLabel(text, icon);
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiButtonSkin<>(this, new GuiButtonBehavior<>(this));
    }
}