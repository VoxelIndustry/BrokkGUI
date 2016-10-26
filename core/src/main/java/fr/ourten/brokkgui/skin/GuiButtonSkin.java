package fr.ourten.brokkgui.skin;

import fr.ourten.brokkgui.behavior.GuiButtonBehavior;
import fr.ourten.brokkgui.element.GuiButton;

public class GuiButtonSkin extends GuiLabeledSkinBase<GuiButton, GuiButtonBehavior<GuiButton>>
{
    public GuiButtonSkin(final GuiButton model, final GuiButtonBehavior<GuiButton> behaviour)
    {
        super(model, behaviour);
    }
}