package net.voxelindustry.brokkgui.internal;

public interface IBrokkGuiImpl
{
    void askOpen();

    void askClose();

    IGuiRenderer getRenderer();

    String getThemeID();

    float getGuiRelativePosX();

    float getGuiRelativePosY();
}