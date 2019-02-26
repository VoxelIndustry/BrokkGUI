package net.voxelindustry.brokkgui.shape;

public class Rectangle extends GuiShape
{
    public static final RectangleShape SHAPE = new RectangleShape();

    public Rectangle(float xLeft, float yLeft, float width, float height)
    {
        super("rectangle", SHAPE);

        this.setxTranslate(xLeft);
        this.setyTranslate(yLeft);

        this.setWidth(width);
        this.setHeight(height);
    }

    public Rectangle(float width, float height)
    {
        this(0, 0, width, height);
    }

    public Rectangle()
    {
        this(0, 0, 0, 0);
    }
}