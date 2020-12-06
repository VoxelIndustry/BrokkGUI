package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.data.Vector2i;
import net.voxelindustry.brokkgui.sprite.Texture;

/**
 * @author Ourten 5 oct. 2016
 */
public interface IGuiHelper
{
    void bindTexture(Texture texture);

    void beginScissor();

    void endScissor();

    void scissorBox(float f, float g, float h, float i);

    void translateVecToScreenSpace(Vector2i vec);

  
    void startAlphaMask(double opacity);

    void closeAlphaMask();
}