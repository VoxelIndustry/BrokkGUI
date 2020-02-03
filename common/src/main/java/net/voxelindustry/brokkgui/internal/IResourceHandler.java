package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.data.Resource;

import java.io.IOException;
import java.util.List;

public interface IResourceHandler
{
    boolean isTypeSupported(String resourceType);

    String readToString(Resource resource) throws IOException;

    List<String> readToLines(Resource resource) throws IOException;
}
