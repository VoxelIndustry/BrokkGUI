package net.voxelindustry.brokkgui.mock;

import net.voxelindustry.brokkgui.data.Resource;
import net.voxelindustry.brokkgui.internal.IResourceHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MockResourceHandler implements IResourceHandler
{
    @Override
    public boolean isTypeSupported(String resourceType)
    {
        return true;
    }

    @Override
    public String readToString(Resource resource) throws IOException
    {
        return resource.toString();
    }

    @Override
    public List<String> readToLines(Resource resource) throws IOException
    {
        return Collections.singletonList(resource.toString());
    }
}
