package net.voxelindustry.brokkgui.style.adapter.translator;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.data.Resource;
import net.voxelindustry.brokkgui.style.adapter.IStyleTranslator;

import java.util.concurrent.atomic.AtomicInteger;

public class ResourceStyleTranslator implements IStyleTranslator<Resource>
{
    @Override
    public Resource decode(String style, AtomicInteger consumedLength)
    {
        var pathWithKey = style.split("\\(");

        var key = pathWithKey[0].trim();
        var path = pathWithKey[1].replace(")", "").replace("\"", "");

        if (!BrokkGuiPlatform.getInstance().getResourceHandler().isTypeSupported(key))
            throw new UnsupportedOperationException("Unsupported resource type used. Check your current binding resource support. type=" + key);

        if (consumedLength != null)
            consumedLength.set(style.substring(0, style.indexOf(')') + 1).length());
        return new Resource(key, path);
    }

    @Override
    public String encode(Resource value, boolean prettyPrint)
    {
        return value.path();
    }
}
