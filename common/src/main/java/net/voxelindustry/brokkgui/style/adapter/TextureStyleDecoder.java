package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.paint.Texture;

public class TextureStyleDecoder implements IStyleDecoder<Texture>
{
    @Override
    public Texture decode(String style)
    {
        if (!style.startsWith("url("))
            return null;

        String[] splitted = style.replace("url(", "").replace(")", "").split(",");

        switch (splitted.length)
        {
            case 1:
                return new Texture(splitted[0].replace("\"", "").trim());
            case 3:
                return new Texture(splitted[0].replace("\"", "").trim(), Float.parseFloat(splitted[1].trim()),
                        Float.parseFloat(splitted[2].trim()));
            case 5:
                return new Texture(splitted[0].replace("\"", "").trim(), Float.parseFloat(splitted[1].trim()),
                        Float.parseFloat(splitted[2].trim()), Float.parseFloat(splitted[3].trim()),
                        Float.parseFloat(splitted[4].trim()));
            default:
                return null;
        }
    }
}
