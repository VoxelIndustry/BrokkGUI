package net.voxelindustry.brokkgui.internal;

public interface IBrokkGuiWrapper
{
    int getScreenWidth();

    int getScreenHeight();

    void askOpen();

    void askClose();

    IGuiRenderer getRenderer();

    String getThemeID();

    default boolean allowDebug()
    {
        return true;
    }
}