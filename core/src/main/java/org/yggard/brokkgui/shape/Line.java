package org.yggard.brokkgui.shape;

import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.EGuiRenderPass;

public class Line extends GuiShape
{
    public Line(final float startX, final float startY, final float endX, final float endY, final float lineWeight)
    {
        this.setxTranslate(startX);
        this.setyTranslate(startY);

        this.setWidth(endX);
        this.setHeight(endY);

        this.setLineWeight(lineWeight);
    }

    public Line(final float endX, final float endY)
    {
        this(0, 0, endX, endY, 1);
    }

    public Line()
    {
        this(0, 0);
    }

    @Override
    public void renderNode(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        if (pass == EGuiRenderPass.MAIN)
            renderer.getHelper().drawColoredLine(renderer, this.getxPos() + this.getxTranslate(),
                    this.getyPos() + this.getyTranslate(), this.getxPos() + this.getWidth(),
                    this.getyPos() + this.getHeight(), this.getLineWeight(), this.getzLevel(), (Color) this.getFill());
    }
}