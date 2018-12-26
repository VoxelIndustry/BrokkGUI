package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class Line extends GuiShape
{
    public Line(float startX, float startY, float endX, float endY)
    {
        super("line");
        this.setxTranslate(startX);
        this.setyTranslate(startY);

        this.setWidth(Math.abs(startX - endX));
        this.setHeight(Math.abs(startY - endY));
    }

    public Line(float endX, float endY)
    {
        this(0, 0, endX, endY);
    }

    public Line()
    {
        this(0, 0);
    }

    @Override
    public void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        if (pass == RenderPass.MAIN)
            renderer.getHelper().drawColoredLine(renderer, this.getxPos() + this.getxTranslate(),
                    this.getyPos() + this.getyTranslate(), this.getxPos() + this.getWidth(),
                    this.getyPos() + this.getHeight(), this.getLineWeight(), this.getzLevel(), this.getColor());
    }
}