package org.yggard.brokkgui.shape;

import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.paint.Texture;

public class Rectangle extends GuiShape
{
    public Rectangle(final float xLeft, final float yLeft, final float width, final float height)
    {
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
    public void renderNode(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        if (pass == EGuiRenderPass.MAIN)
        {
            if (this.getLineWeight() > 0)
                renderer.getHelper().drawColoredEmptyRect(renderer, this.getxPos() + this.getxTranslate(),
                        this.getyPos() + this.getyTranslate(), this.getWidth(), this.getHeight(), this.getzLevel(),
                        this.getLineColor(), this.getLineWeight());

            if (this.getFill() instanceof Color && ((Color) this.getFill()).getAlpha() != 0)
                renderer.getHelper().drawColoredRect(renderer, this.getxPos() + this.getxTranslate(),
                        this.getyPos() + this.getyTranslate(), this.getWidth(), this.getHeight(), this.getzLevel(),
                        (Color) this.getFill());
            else if (this.getFill() instanceof Texture)
            {
                final Texture texture = (Texture) this.getFill();
                renderer.getHelper().bindTexture(texture);
                renderer.getHelper().drawTexturedRect(renderer, this.getxPos() + this.getxTranslate(),
                        this.getyPos() + this.getyTranslate(), texture.getUMin(), texture.getVMin(), texture.getUMax(),
                        texture.getVMax(), this.getWidth(), this.getHeight(), this.getzLevel());
            }
        }
    }
}