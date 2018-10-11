package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.Texture;

public class Circle extends GuiShape
{
    public Circle(final float xPosition, final float yPosition, final float radius)
    {
        super("circle");

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
    public void renderContent(final IGuiRenderer renderer, final RenderPass pass, final int mouseX, final int mouseY)
    {
        if (pass == RenderPass.MAIN)
        {
            if (this.getLineWeight() > 0)
                renderer.getHelper().drawColoredEmptyCircle(renderer, this.getxTranslate() + this.getxPos(),
                        this.getyTranslate() + this.getyPos(), this.getWidth(), this.getzLevel(), this.getLineColor(),
                        this.getLineWeight());
            if (this.getColor().getAlpha() != 0)
                renderer.getHelper().drawColoredCircle(renderer, this.getxTranslate() + this.getxPos(),
                        this.getyTranslate() + this.getyPos(), this.getWidth(), this.getzLevel(),
                        this.getColor());
            if (this.getTexture() != Texture.EMPTY)
            {
                final Texture texture = this.getTexture();
                renderer.getHelper().bindTexture(texture);
                renderer.getHelper().drawTexturedCircle(renderer, this.getxTranslate() + this.getxPos(),
                        this.getyTranslate() + this.getyPos(), texture.getUMin(), texture.getVMin(), texture.getUMax(),
                        texture.getVMax(), this.getWidth(), this.getzLevel());
            }
        }
    }
}