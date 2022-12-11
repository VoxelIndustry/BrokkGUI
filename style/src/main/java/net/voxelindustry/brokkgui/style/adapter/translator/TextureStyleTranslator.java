package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.sprite.Texture;
import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;

import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class TextureStyleTranslator implements IStyleTranslator<Texture>
{
    private final NumberFormat textureFormat;

    public TextureStyleTranslator()
    {
        textureFormat = NumberFormat.getInstance();
        textureFormat.setMinimumFractionDigits(0);
        textureFormat.setMaximumFractionDigits(1);
    }

    @Override
    public String encode(Texture value, boolean prettyPrint)
    {
        return value.getResource()
                + " (" + value.getPixelWidth() + "px, " + value.getPixelHeight() + "px) "
                + textureFormat.format(value.getUMin()) + "," + textureFormat.format(value.getVMin())
                + " -> " + textureFormat.format(value.getUMax()) + "," + textureFormat.format(value.getVMax());
    }

    @Override
    public Texture decode(String style, AtomicInteger consumedLength)
    {
        var doesStartWithURL = style.startsWith("url(");
        var doesStartWithAssets = style.startsWith("assets(");

        if (!doesStartWithURL && !doesStartWithAssets)
            return Texture.EMPTY;

        if (doesStartWithURL)
            BrokkGuiPlatform.getInstance().getLogger().warning("Deprecated texture specification used. url(...) will be removed from BrokkGUI in version 1.0.0.");

        String[] split = style.substring(style.indexOf("(") + 1, style.indexOf(")")).split(",");

        String resource = "";
        float uMin = 0;
        float vMin = 0;
        float uMax = 1;
        float vMax = 1;

        int pixelWidth = 0;
        int pixelHeight = 0;

        for (int index = 0; index < split.length; index++)
        {
            if (index == 0)
            {
                resource = split[0].replace("\"", "").trim();
                continue;
            }

            if (split[index].contains("px"))
            {
                if (index % 2 == 1)
                    pixelWidth = Integer.parseInt(split[index].replace("px", "").trim());
                else
                    pixelHeight = Integer.parseInt(split[index].replace("px", "").trim());
            }
            else
            {
                if (index == 1)
                    uMin = Float.parseFloat(split[index].trim());
                else if (index == 2)
                    vMin = Float.parseFloat(split[index].trim());
                else if (index == 3)
                    uMax = Float.parseFloat(split[index].trim());
                else if (index == 4)
                    vMax = Float.parseFloat(split[index].trim());
            }
        }

        if (consumedLength != null)
            consumedLength.set(style.indexOf(')')+1);
        return new Texture(resource, uMin, vMin, uMax, vMax, pixelWidth, pixelHeight);
    }
}
