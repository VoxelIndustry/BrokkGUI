package net.voxelindustry.brokkgui.style;

public class DummyStyleHolder extends StyleHolder
{
    private String id;
    private String type;

    public DummyStyleHolder(String id, String type)
    {
        this.id = id;
        this.type = type;
    }

    public DummyStyleHolder(String id)
    {
        this(id, "");
    }

    public DummyStyleHolder()
    {
        this("");
    }

    @Override
    public String type()
    {
        return this.type;
    }

    @Override
    public String id()
    {
        return this.id;
    }
}
