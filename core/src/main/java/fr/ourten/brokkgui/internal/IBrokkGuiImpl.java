package fr.ourten.brokkgui.internal;

public interface IBrokkGuiImpl
{
    int getScreenWidth();

    int getScreenHeight();

    void closeGui();

    IGuiRenderer getRenderer();
}