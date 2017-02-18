package org.yggard.brokkgui.paint;

import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.shape.Rectangle;

public class Background extends Rectangle
{
    public Background(final Texture texture)
    {
        this.setFill(texture);
    }

    public Background(final Color color)
    {
        this.setFill(color);
    }

    public Background()
    {
        this(Color.ALPHA);
    }

    public void attach(final GuiNode node)
    {
        this.getxPosProperty().bind(node.getxPosProperty());
        this.getyPosProperty().bind(node.getyPosProperty());

        this.getxTranslateProperty().bind(node.getxTranslateProperty());
        this.getyTranslateProperty().bind(node.getyTranslateProperty());

        this.getWidthProperty().bind(node.getWidthProperty());
        this.getHeightProperty().bind(node.getHeightProperty());
    }

    public void detach()
    {
        this.getxPosProperty().unbind();
        this.getyPosProperty().unbind();

        this.getxTranslateProperty().unbind();
        this.getyTranslateProperty().unbind();

        this.getWidthProperty().unbind();
        this.getHeightProperty().unbind();
    }

    @Override
    public void renderNode(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        super.renderNode(renderer, pass, mouseX, mouseY);
    }
}