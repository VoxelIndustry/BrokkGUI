package net.voxelindustry.brokkgui.data;

import java.util.Objects;

public class Resource
{
    private final String type;
    private final String path;

    public Resource(String type, String path)
    {
        this.type = type;
        this.path = path;
    }

    public String getType()
    {
        return type;
    }

    public String getPath()
    {
        return path;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(path, resource.path) && Objects.equals(type, resource.type);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(path, type);
    }

    @Override
    public String toString()
    {
        return "Resource{" +
                "type='" + type + "', " +
                "path='" + path + '\'' +
                '}';
    }
}
