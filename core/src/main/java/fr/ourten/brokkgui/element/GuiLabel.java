package fr.ourten.brokkgui.element;

import fr.ourten.brokkgui.behavior.GuiBehaviorBase;
import fr.ourten.brokkgui.control.GuiLabeled;
import fr.ourten.brokkgui.skin.GuiLabeledSkinBase;

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
    protected GuiLabeledSkinBase<GuiLabel, GuiBehaviorBase<GuiLabel>> makeDefaultSkin()
    {
        return new GuiLabeledSkinBase<>(this, new GuiBehaviorBase<>(this));
    }
}