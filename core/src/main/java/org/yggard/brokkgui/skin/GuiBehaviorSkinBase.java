package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.behavior.GuiBehaviorBase;
import org.yggard.brokkgui.control.GuiControl;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Background;
import org.yggard.brokkgui.paint.EGuiRenderPass;

public class GuiBehaviorSkinBase<C extends GuiControl, B extends GuiBehaviorBase<C>> extends GuiSkinBase<C>
{
    private final B behavior;

    public GuiBehaviorSkinBase(final C model, final B behavior)
    {
        super(model);
        this.behavior = behavior;
    }

    @Override
    public void render(final EGuiRenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        if (pass == EGuiRenderPass.MAIN)
            if (this.getModel().getBackgroundProperty().isPresent())
                this.getModel().getBackgroundProperty().getValue().renderNode(renderer, pass, mouseX, mouseY);
        super.render(pass, renderer, mouseX, mouseY);
    }

    public B getBehavior()
    {
        return this.behavior;
    }

    public Background getBackground()
    {
        return this.getModel().getBackgroundProperty().getOrDefault(Background.EMPTY);
    }
}