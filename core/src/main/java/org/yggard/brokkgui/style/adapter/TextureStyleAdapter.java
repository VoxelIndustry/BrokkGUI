package org.yggard.brokkgui.style.adapter;

import org.yggard.brokkgui.paint.Texture;

public class TextureStyleAdapter implements IStyleAdapter<Texture>
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
                return new Texture(splitted[0].replace("\"", "").trim(), Integer.parseInt(splitted[1].trim()),
                        Integer.parseInt(splitted[2].trim()));
            case 5:
                return new Texture(splitted[0].replace("\"", "").trim(), Integer.parseInt(splitted[1].trim()),
                        Integer.parseInt(splitted[2].trim()), Integer.parseInt(splitted[3].trim()),
                        Integer.parseInt(splitted[4].trim()));
            default:
                return null;
        }
    }
}
