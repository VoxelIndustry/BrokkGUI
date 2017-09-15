package org.yggard.brokkgui.style.tree;

import org.yggard.brokkgui.style.StyleHolder;

public interface IStyleSelector
{
    boolean match(StyleHolder styleHolder);

    boolean match(IStyleSelector selector);

    int getSpecificity();
}
