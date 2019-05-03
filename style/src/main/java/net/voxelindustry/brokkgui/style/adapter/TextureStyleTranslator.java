package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.paint.Texture;

import java.text.NumberFormat;

public class TextureStyleTranslator implements IStyleDecoder<Texture>, IStyleEncoder<Texture>, IStyleValidator<Texture>
{
    private final NumberFormat textureFormat;

    TextureStyleTranslator()
    {
        textureFormat = NumberFormat.getInstance();
        textureFormat.setMinimumFractionDigits(0);
        textureFormat.setMaximumFractionDigits(1);
    }

    @Override
    public String encode(Texture value, boolean prettyPrint)
    {
        return value.getResource() + " " + textureFormat.format(value.getUMin()) + "," + textureFormat.format(value.getVMin())
                + " -> " + textureFormat.format(value.getUMax()) + "," + textureFormat.format(value.getVMax());
    }

    @Override
    public Texture decode(String style)
    {
        if (!style.startsWith("url("))
            return null;

        String[] split = style.replace("url(", "").replace(")", "").split(",");

        switch (split.length)
        {
            case 1:
                return new Texture(split[0].replace("\"", "").trim());
            case 3:
                return new Texture(split[0].replace("\"", "").trim(), Float.parseFloat(split[1].trim()),
                        Float.parseFloat(split[2].trim()));
            case 5:
                return new Texture(split[0].replace("\"", "").trim(), Float.parseFloat(split[1].trim()),
                        Float.parseFloat(split[2].trim()), Float.parseFloat(split[3].trim()),
                        Float.parseFloat(split[4].trim()));
            default:
                return null;
        }
    }

    @Override
    public int validate(String style)
    {
        if (!style.startsWith("url("))
            return 0;

        return style.substring(0, style.indexOf(')') + 1).length();
    }
}
