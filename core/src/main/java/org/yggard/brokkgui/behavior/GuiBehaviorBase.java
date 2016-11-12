package org.yggard.brokkgui.behavior;

import org.yggard.brokkgui.control.GuiControl;

public class GuiBehaviorBase<C extends GuiControl>
{
    private C model;

    public GuiBehaviorBase(final C model)
    {
        if (model == null)
            throw new IllegalArgumentException("Cannot pass a null model");
        this.model = model;

        this.model.getFocusedProperty().addListener(property -> this.onFocusChanged());
        this.model.getDisabledProperty().addListener(property -> this.onDisableChanged());
        this.model.getHoveredProperty().addListener(property -> this.onHoverChanged());
    }

    public void setModel(final C model)
    {
        this.model = model;
    }

    public C getModel()
    {
        return this.model;
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

    protected void onFocusChanged()
    {
    }

    protected void onDisableChanged()
    {
    }

    protected void onHoverChanged()
    {
    }
}