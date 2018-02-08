package org.yggard.brokkgui.internal;

public interface IBrokkGuiImpl
{
    int getScreenWidth();

    int getScreenHeight();

    void askOpen();

    void askClose();

    IGuiRenderer getRenderer();

    String getThemeID();
}