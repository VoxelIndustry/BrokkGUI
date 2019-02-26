package net.voxelindustry.brokkgui.skin;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.control.GuiElement;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;

import java.util.ArrayList;
import java.util.List;

public class GuiSkinBase<T extends GuiElement> implements IGuiSkin
{
    private final List<GuiNode> childrens;
    private final T             model;

    public GuiSkinBase(final T model)
    {
        if (model == null)
            throw new IllegalArgumentException("Cannot pass a null model");
        this.model = model;

        this.childrens = new ArrayList<>();
    }

    public T getModel()
    {
        return this.model;
    }

    @Override
    public void render(final RenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
    }

    public List<GuiNode> getChildrens()
    {
        return this.childrens;
    }
}