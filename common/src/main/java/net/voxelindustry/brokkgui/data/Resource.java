package net.voxelindustry.brokkgui.data;

public record Resource(String type, String path)
{
    @Override
    public String toString()
    {
        return "Resource{" +
                "type='" + type + "', " +
                "path='" + path + '\'' +
                '}';
    }
}
