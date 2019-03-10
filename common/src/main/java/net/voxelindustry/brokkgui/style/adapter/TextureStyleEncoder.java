package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.paint.Texture;

import java.text.NumberFormat;

public class TextureStyleEncoder implements IStyleEncoder<Texture>
{
    private final NumberFormat colorFormat;

    TextureStyleEncoder()
    {
        colorFormat = NumberFormat.getInstance();
        colorFormat.setMinimumFractionDigits(0);
        colorFormat.setMaximumFractionDigits(1);
    }

    @Override
    public String encode(Texture value, boolean prettyPrint)
    {
        return value.getResource() + " " + colorFormat.format(value.getUMin()) + "," + colorFormat.format(value.getVMin())
                + " -> " + colorFormat.format(value.getUMax()) + "," + colorFormat.format(value.getVMax());
    }
}
