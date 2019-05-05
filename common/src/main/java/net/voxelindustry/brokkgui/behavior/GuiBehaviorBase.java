package net.voxelindustry.brokkgui.behavior;

import net.voxelindustry.brokkgui.component.GuiElement;

public class GuiBehaviorBase<C extends GuiElement>
{
    private C model;

    public GuiBehaviorBase(final C model)
    {
        if (model == null)
            throw new IllegalArgumentException("Cannot pass a null model");
        this.model = model;
    }

    public void setModel(final C model)
    {
        this.model = model;
    }

    public C getModel()
    {
        return this.model;
    }
}