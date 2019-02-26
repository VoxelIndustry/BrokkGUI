package net.voxelindustry.brokkgui.shape;

public class Circle extends GuiShape
{
    public static final CircleShape SHAPE = new CircleShape();

    public Circle(float xPosition, float yPosition, float radius)
    {
        super("circle", SHAPE);

        this.setxTranslate(xPosition);
        this.setyTranslate(yPosition);
        this.setWidth(radius);
        this.setHeight(radius);
    }

    public Circle(float radius)
    {
        this(0, 0, radius);
    }

    public Circle()
    {
        this(0, 0, 0);
    }
}