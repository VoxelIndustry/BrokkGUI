package net.voxelindustry.brokkgui.shape;

public class Line extends GuiShape
{
    public Line(float startX, float startY, float endX, float endY)
    {
        super("line", null);
        this.setShape(new LineShape(this::getLineThin));
        this.setxTranslate(startX);
        this.setyTranslate(startY);

        this.setWidth(Math.abs(startX - endX));
        this.setHeight(Math.abs(startY - endY));

        this.getStyle().registerProperty("line-thin", 1f, Float.class);
    }

    public Line(float endX, float endY)
    {
        this(0, 0, endX, endY);
    }

    public Line()
    {
        this(0, 0);
    }

    public float getLineThin()
    {
        return this.getStyle().getStyleValue("line-thin", Float.class, 1f);
    }

    public void setLineThin(float lineThin)
    {
        this.getStyle().setPropertyDirect("line-thin", lineThin, Float.class);
    }
}