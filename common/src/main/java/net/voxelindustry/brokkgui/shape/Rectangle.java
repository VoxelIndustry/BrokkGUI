package net.voxelindustry.brokkgui.shape;

import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.Texture;

public class Rectangle extends GuiShape
{
    public Rectangle(final float xLeft, final float yLeft, final float width, final float height)
    {
        super("rectangle");

        this.setxTranslate(xLeft);
        this.setyTranslate(yLeft);

        this.setWidth(width);
        this.setHeight(height);
    }

    public Rectangle(final float width, final float height)
    {
        this(0, 0, width, height);
    }

    public Rectangle()
    {
        this(0, 0, 0, 0);
    }

    @Override
    public void renderContent(final IGuiRenderer renderer, final RenderPass pass, final int mouseX, final int mouseY)
    {
        if (pass == RenderPass.MAIN)
        {
            if (this.getLineWeight() > 0)
                renderer.getHelper().drawColoredEmptyRect(renderer, this.getxPos() + this.getxTranslate(),
                        this.getyPos() + this.getyTranslate(), this.getWidth(), this.getHeight(), this.getzLevel(),
                        this.getLineColor(), this.getLineWeight());

            if (this.getColor().getAlpha() != 0)
                renderer.getHelper().drawColoredRect(renderer, this.getxPos() + this.getxTranslate(),
                        this.getyPos() + this.getyTranslate(), this.getWidth(), this.getHeight(), this.getzLevel(),
                        this.getColor());

            if (this.getTexture() != Texture.EMPTY)
            {
                final Texture texture = this.getTexture();
                renderer.getHelper().bindTexture(texture);
                renderer.getHelper().drawTexturedRect(renderer, this.getxPos() + this.getxTranslate(),
                        this.getyPos() + this.getyTranslate(), texture.getUMin(), texture.getVMin(), texture.getUMax(),
                        texture.getVMax(), this.getWidth(), this.getHeight(), this.getzLevel());
            }
        }
    }
}