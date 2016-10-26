package fr.ourten.brokkgui.shape;

import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.EGuiRenderPass;

public class Circle extends GuiShape
{
    public Circle(final float xPosition, final float yPosition, final float radius)
    {
        this.setxTranslate(xPosition);
        this.setyTranslate(yPosition);
        this.setWidth(radius);
        this.setHeight(radius);
    }

    public Circle(final float radius)
    {
        this(0, 0, radius);
    }

    public Circle()
    {
        this(0, 0, 0);
    }

    @Override
    public void renderNode(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        if (pass == EGuiRenderPass.MAIN)
        {
            if (this.getLineWeight() > 0)
                renderer.getHelper().drawColoredEmptyCircle(renderer, this.getxTranslate() + this.getxPos(),
                        this.getyTranslate() + this.getyPos(), this.getWidth(), this.getzLevel(), this.getLineColor(),
                        this.getLineWeight());
            renderer.getHelper().drawColoredCircle(renderer, this.getxTranslate() + this.getxPos(),
                    this.getyTranslate() + this.getyPos(), this.getWidth(), this.getzLevel(), this.getColor());
        }
    }
}