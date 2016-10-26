package fr.ourten.brokkgui.skin;

import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.EGuiRenderPass;

public interface IGuiSkin
{
    void render(final EGuiRenderPass pass, final IGuiRenderer renderer, int mouseX, int mouseY);
}