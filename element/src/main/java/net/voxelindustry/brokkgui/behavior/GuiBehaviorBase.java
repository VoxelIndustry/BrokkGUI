package net.voxelindustry.brokkgui.behavior;

import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.control.GuiSkinedElement;

public class GuiBehaviorBase<C extends GuiSkinedElement>
{
    private C model;

    public GuiBehaviorBase(C model)
    {
        if (model == null)
            throw new IllegalArgumentException("Cannot pass a null model");
        this.model = model;
    }

    public void setModel(C model)
    {
        this.model = model;
    }

    public C getModel()
    {
        return model;
    }

    public Transform transform()
    {
        return getModel().transform();
    }
}