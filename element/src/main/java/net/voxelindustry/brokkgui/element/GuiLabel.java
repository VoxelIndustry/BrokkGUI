package net.voxelindustry.brokkgui.element;

import net.voxelindustry.brokkgui.behavior.GuiBehaviorBase;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.control.GuiLabeled;
import net.voxelindustry.brokkgui.skin.GuiLabeledSkinBase;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

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