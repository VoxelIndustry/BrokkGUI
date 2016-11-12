package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.behavior.GuiBehaviorBase;
import org.yggard.brokkgui.element.GuiListCell;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.EGuiRenderPass;

public class GuiListCellSkin<T> extends GuiLabeledSkinBase<GuiListCell<T>, GuiBehaviorBase<GuiListCell<T>>>
{
    public GuiListCellSkin(final GuiListCell<T> model)
    {
        super(model, new GuiBehaviorBase<>(model));
    }

    @Override
    public void render(final EGuiRenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);

        if (this.getModel().getGraphic() != null)
            this.getModel().getGraphic().renderNode(renderer, pass, mouseX, mouseY);
    }
}