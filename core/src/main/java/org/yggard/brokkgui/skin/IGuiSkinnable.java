package org.yggard.brokkgui.skin;

import fr.ourten.teabeans.value.BaseProperty;

public interface IGuiSkinnable
{
    default GuiSkinBase<?> getSkin()
    {
        return this.getSkinProperty().getValue();
    }

    public BaseProperty<GuiSkinBase<?>> getSkinProperty();

    public void setSkin(GuiSkinBase<?> value);
}