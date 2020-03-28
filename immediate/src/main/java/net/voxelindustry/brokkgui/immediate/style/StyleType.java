package net.voxelindustry.brokkgui.immediate.style;

public class StyleType
{
    public static final StyleType LIGHT  = new StyleType("light");
    public static final StyleType NORMAL = new StyleType("normal");
    public static final StyleType DARK   = new StyleType("dark");

    private String name;

    public StyleType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
