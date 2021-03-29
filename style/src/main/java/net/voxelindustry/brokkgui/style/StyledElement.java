package net.voxelindustry.brokkgui.style;

import net.voxelindustry.brokkgui.component.ComponentHolder;

public interface StyledElement extends ComponentHolder
{
    default StyleComponent style()
    {
        return get(StyleComponent.class);
    }
}
