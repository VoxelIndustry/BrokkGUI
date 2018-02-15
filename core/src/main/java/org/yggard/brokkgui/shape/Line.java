package org.yggard.brokkgui.shape;

import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.RenderPass;

public class Line extends GuiShape
{
    public Line(final float startX, final float startY, final float endX, final float endY)
    {
        super("line");
        this.setxTranslate(startX);
        this.setyTranslate(startY);

        this.setWidth(endX);
        this.setHeight(endY);
    }

    public Line(final float endX, final float endY)
    {
        this(0, 0, endX, endY);
    }

    public Line()
    {
        this(0, 0);
    }

    @Override
    public void renderContent(final IGuiRenderer renderer, final RenderPass pass, final int mouseX, final int mouseY)
    {
        if (pass == RenderPass.MAIN)
            renderer.getHelper().drawColoredLine(renderer, this.getxPos() + this.getxTranslate(),
                    this.getyPos() + this.getyTranslate(), this.getxPos() + this.getWidth(),
                    this.getyPos() + this.getHeight(), this.getLineWeight(), this.getzLevel(), this.getColor());
    }
}