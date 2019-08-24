package net.voxelindustry.brokkgui.style.selector;

import net.voxelindustry.brokkgui.style.StyleHolder;

public interface IStyleSelector
{
    boolean match(StyleHolder styleHolder);

    boolean match(IStyleSelector selector);

    int getSpecificity();

    IStyleSelector add(StyleSelectorType type, String selector);
}
