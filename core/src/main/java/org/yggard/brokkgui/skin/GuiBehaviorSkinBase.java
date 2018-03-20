package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.behavior.GuiBehaviorBase;
import org.yggard.brokkgui.control.GuiControl;

public class GuiBehaviorSkinBase<C extends GuiControl, B extends GuiBehaviorBase<C>> extends GuiSkinBase<C>
{
    private final B behavior;

    public GuiBehaviorSkinBase(final C model, final B behavior)
    {
        super(model);
        this.behavior = behavior;
    }

    public B getBehavior()
    {
        return this.behavior;
    }
}