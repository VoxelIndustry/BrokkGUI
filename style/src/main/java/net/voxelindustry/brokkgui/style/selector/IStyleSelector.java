package net.voxelindustry.brokkgui.style.selector;

import net.voxelindustry.brokkgui.style.StyleComponent;

public interface IStyleSelector
{
    boolean match(StyleComponent styleComponent);

    boolean match(IStyleSelector selector);

    int getSpecificity();

    IStyleSelector add(StyleSelectorType type, String selector);
}
