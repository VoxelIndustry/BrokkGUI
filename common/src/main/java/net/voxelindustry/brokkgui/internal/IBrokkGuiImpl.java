package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.window.IGuiWindow;

public interface IBrokkGuiImpl
{
    void askOpen();

    void askClose();

    IGuiRenderer getRenderer();

    String getThemeID();

    float getGuiRelativePosX(float guiXRelativePos, float guiWidth);

    float getGuiRelativePosY(float guiYRelativePos, float guiHeight);

    IGuiWindow getGui();

    void setGuiWindow(IGuiWindow window);
}