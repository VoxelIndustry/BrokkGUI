package org.yggard.brokkgui.style;

public interface ICascadeStylable
{
    ICascadeStylable getParent();

    StyleHolder getStyle();
}
