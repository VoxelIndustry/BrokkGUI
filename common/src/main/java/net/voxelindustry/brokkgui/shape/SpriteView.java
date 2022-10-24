package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.data.Resource;
import net.voxelindustry.brokkgui.sprite.Texture;

public class SpriteView extends Rectangle
{
    public SpriteView(float xLeft, float yLeft, float width, float height)
    {
        super(xLeft, yLeft, width, height);
    }

    public SpriteView(float width, float height)
    {
        this(0, 0, width, height);
    }

    public SpriteView()
    {
        this(0, 0, 0, 0);
    }

    @Override
    public String type()
    {
        return "sprite";
    }

    public void sprite(Texture texture)
    {
        paint().backgroundTexture(texture);
    }

    public void sprite(Resource resource, float size)
    {
        sprite(resource, size, size);
    }

    public void sprite(Resource resource, float width, float height)
    {
        paint().backgroundTexture(new Texture(resource.path(), 0, 0));
    }

    public static SpriteView ofResource(Resource texture, float size)
    {
        return ofResource(texture, size, size);
    }

    public static SpriteView ofResource(Resource texture, float width, float height)
    {
        var sprite = new SpriteView(width, height);
        sprite.sprite(texture, width, height);
        return sprite;
    }

    public static SpriteView ofTexture(Texture texture, float size)
    {
        return ofTexture(texture, size, size);
    }

    public static SpriteView ofTexture(Texture texture, float width, float height)
    {
        var sprite = new SpriteView(width, height);
        sprite.sprite(texture);
        return sprite;
    }
}