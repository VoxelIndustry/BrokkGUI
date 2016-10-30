package fr.ourten.brokkgui.element;

import fr.ourten.brokkgui.behavior.GuiBehaviorBase;
import fr.ourten.brokkgui.control.GuiLabeled;
import fr.ourten.brokkgui.skin.GuiLabeledSkinBase;
import fr.ourten.brokkgui.skin.GuiSkinBase;

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