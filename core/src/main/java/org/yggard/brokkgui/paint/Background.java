package org.yggard.brokkgui.paint;

import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.shape.Rectangle;

public class Background extends Rectangle
{
    public static final Background EMPTY = new Background(Color.ALPHA);

    public Background(final Texture texture)
    {
        this();
        this.getStyle().getStyleProperty("-texture", Texture.class).setValue(texture);
    }

    public Background(final Color color)
    {
        this();
        this.getStyle().getStyleProperty("-color", Color.class).setValue(color);
    }

    public Background()
    {
        this.setType("background");
    }

    public void attach(final GuiNode node)
    {
        this.getxPosProperty().bind(node.getxPosProperty());
        this.getyPosProperty().bind(node.getyPosProperty());

        this.getxTranslateProperty().bind(node.getxTranslateProperty());
        this.getyTranslateProperty().bind(node.getyTranslateProperty());

        this.getWidthProperty().bind(node.getWidthProperty());
        this.getHeightProperty().bind(node.getHeightProperty());

        node.getStyle().registerAlias("background", this.getStyle());
    }

    public void detach(GuiNode oldNode)
    {
        this.getxPosProperty().unbind();
        this.getyPosProperty().unbind();

        this.getxTranslateProperty().unbind();
        this.getyTranslateProperty().unbind();

        this.getWidthProperty().unbind();
        this.getHeightProperty().unbind();

        oldNode.getStyle().removeAlias("background");
    }
}