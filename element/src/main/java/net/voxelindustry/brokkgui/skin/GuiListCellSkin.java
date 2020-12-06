package net.voxelindustry.brokkgui.skin;

import net.voxelindustry.brokkgui.behavior.GuiBehaviorBase;
import net.voxelindustry.brokkgui.element.GuiListCell;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class GuiListCellSkin<T> extends GuiBehaviorSkinBase<GuiListCell<T>, GuiBehaviorBase<GuiListCell<T>>>
{
    public GuiListCellSkin(GuiListCell<T> model)
    {
        super(model, new GuiBehaviorBase<>(model));
    }

    @Override
    public void render(RenderPass pass, IRenderCommandReceiver renderer, int mouseX, int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);

        if (getModel().getGraphicProperty().isPresent())
            getModel().getGraphic().renderNode(renderer, pass, mouseX, mouseY);
    }
}