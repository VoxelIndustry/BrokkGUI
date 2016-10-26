package fr.ourten.brokkgui.data;

import fr.ourten.brokkgui.component.GuiNode;
import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.ObservableValue;

/**
 * @author Ourten 9 oct. 2016
 */
public class RelativeBindingHelper
{
    public static final void bindSizeRelative(final GuiNode toBind, final GuiNode parent, final float widthRatio,
            final float heightRatio)
    {
        RelativeBindingHelper.bindWidthRelative(toBind, parent, widthRatio);
        RelativeBindingHelper.bindHeightRelative(toBind, parent, heightRatio);
    }

    public static final void bindWidthRelative(final GuiNode toBind, final GuiNode parent, final float widthRatio)
    {
        toBind.getWidthProperty().bind(
                BaseExpression.constantCombine(parent.getWidthProperty(), widthRatio, (width, ratio) -> width * ratio));
    }

    public static final void bindHeightRelative(final GuiNode toBind, final GuiNode parent, final float heightRatio)
    {
        toBind.getHeightProperty().bind(BaseExpression.constantCombine(parent.getHeightProperty(), heightRatio,
                (height, ratio) -> height * ratio));
    }

    public static final void bindSizeRelative(final GuiNode toBind, final GuiNode parent,
            final ObservableValue<Float> widthRatio, final ObservableValue<Float> heightRatio)
    {
        RelativeBindingHelper.bindWidthRelative(toBind, parent, widthRatio);
        RelativeBindingHelper.bindHeightRelative(toBind, parent, heightRatio);
    }

    public static final void bindWidthRelative(final GuiNode toBind, final GuiNode parent,
            final ObservableValue<Float> widthRatio)
    {
        toBind.getWidthProperty()
                .bind(BaseExpression.biCombine(parent.getWidthProperty(), widthRatio, (width, ratio) -> width * ratio));
    }

    public static final void bindHeightRelative(final GuiNode toBind, final GuiNode parent,
            final ObservableValue<Float> heightRatio)
    {
        toBind.getHeightProperty().bind(
                BaseExpression.biCombine(parent.getHeightProperty(), heightRatio, (height, ratio) -> height * ratio));
    }

    public static final void bindToPos(final GuiNode toBind, final GuiNode parent, final ObservableValue<Float> addX,
            final ObservableValue<Float> addY)
    {
        if (addX != null)
            toBind.getxPosProperty()
                    .bind(BaseExpression.getExpression(
                            () -> parent.getxPos() + parent.getxTranslate() + addX.getValue(), parent.getxPosProperty(),
                            parent.getxTranslateProperty(), addX));
        else
            toBind.getxPosProperty().bind(BaseExpression.getExpression(() -> parent.getxPos() + parent.getxTranslate(),
                    parent.getxPosProperty(), parent.getxTranslateProperty()));
        if (addY != null)
            toBind.getyPosProperty()
                    .bind(BaseExpression.getExpression(
                            () -> parent.getyPos() + parent.getyTranslate() + addY.getValue(), parent.getyPosProperty(),
                            parent.getyTranslateProperty(), addY));
        else
            toBind.getyPosProperty().bind(BaseExpression.getExpression(() -> parent.getyPos() + parent.getyTranslate(),
                    parent.getyPosProperty(), parent.getyTranslateProperty()));
    }

    public static final void bindToPos(final GuiNode toBind, final GuiNode parent)
    {
        toBind.getxPosProperty().bind(BaseExpression.biCombine(parent.getxPosProperty(), parent.getxTranslateProperty(),
                (x, translate) -> x * translate));
        toBind.getyPosProperty().bind(BaseExpression.biCombine(parent.getyPosProperty(), parent.getyTranslateProperty(),
                (y, translate) -> y * translate));
    }

    public static final void bindToCenter(final GuiNode toBind, final GuiNode parent)
    {
        toBind.getxPosProperty()
                .bind(BaseExpression.tetraCombine(parent.getxPosProperty(),
                        parent.getWidthProperty(), parent.getxTranslateProperty(), toBind.getWidthProperty(),
                        (xPos, width, xTranslate, childWidth) -> xPos + xTranslate + (width / 2 - childWidth / 2)));
        toBind.getyPosProperty()
                .bind(BaseExpression.tetraCombine(parent.getyPosProperty(),
                        parent.getHeightProperty(), parent.getyTranslateProperty(), toBind.getHeightProperty(),
                        (yPos, height, yTranslate, childHeight) -> yPos + yTranslate + (height / 2 - childHeight / 2)));
    }

    public static final void bindToRelative(final GuiNode toBind, final GuiNode parent, final float ratioX,
            final float ratioY)
    {
        toBind.getxPosProperty().bind(BaseExpression.tetraCombine(parent.getxPosProperty(), parent.getWidthProperty(),
                parent.getxTranslateProperty(), toBind.getWidthProperty(),
                (xPos, width, xTranslate, childWidth) -> xPos + xTranslate + (width / (1 / ratioX) - childWidth / 2)));
        toBind.getyPosProperty()
                .bind(BaseExpression.tetraCombine(parent.getyPosProperty(), parent.getHeightProperty(),
                        parent.getyTranslateProperty(), toBind.getHeightProperty(), (yPos, height, yTranslate,
                                childHeight) -> yPos + yTranslate + (height / (1 / ratioY) - childHeight / 2)));
    }
}