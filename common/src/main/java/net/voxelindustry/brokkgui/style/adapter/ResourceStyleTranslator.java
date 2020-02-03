package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.data.Resource;

public class ResourceStyleTranslator implements IStyleDecoder<Resource>, IStyleEncoder<Resource>, IStyleValidator<Resource>
{
    @Override
    public Resource decode(String style)
    {
        String[] pathWithKey = style.split("\\(");

        String key = pathWithKey[0].trim();
        String path = pathWithKey[1].replace(")", "").replace("\"", "");

        if (!BrokkGuiPlatform.getInstance().getResourceHandler().isTypeSupported(key))
            throw new UnsupportedOperationException("Unsupported resource type used. Check that your current binding resource support. type=" + key);

        return new Resource(key, path);
    }

    @Override
    public String encode(Resource value, boolean prettyPrint)
    {
        return value.getPath();
    }

    @Override
    public int validate(String style)
    {
        return style.substring(0, style.indexOf(')') + 1).length();
    }
}
