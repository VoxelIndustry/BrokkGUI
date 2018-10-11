package net.voxelindustry.brokkgui.data;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.component.GuiNode;

/**
 * @author Ourten 9 oct. 2016
 */
public class RelativeBindingHelper
{
    public static void bindSizeRelative(GuiNode toBind, GuiNode parent, float widthRatio, float heightRatio)
    {
        RelativeBindingHelper.bindWidthRelative(toBind, parent, widthRatio);
        RelativeBindingHelper.bindHeightRelative(toBind, parent, heightRatio);
    }

    public static void bindWidthRelative(GuiNode toBind, GuiNode parent, float widthRatio)
    {
        toBind.getWidthProperty().bind(
                BaseExpression.constantCombine(parent.getWidthProperty(), widthRatio, (width, ratio) -> width * ratio));
    }

    public static void bindHeightRelative(GuiNode toBind, GuiNode parent, float heightRatio)
    {
        toBind.getHeightProperty().bind(BaseExpression.constantCombine(parent.getHeightProperty(), heightRatio,
                (height, ratio) -> height * ratio));
    }

    public static void bindSizeRelative(GuiNode toBind, GuiNode parent, ObservableValue<Float> widthRatio,
                                        ObservableValue<Float> heightRatio)
    {
        RelativeBindingHelper.bindWidthRelative(toBind, parent, widthRatio);
        RelativeBindingHelper.bindHeightRelative(toBind, parent, heightRatio);
    }

    public static void bindWidthRelative(GuiNode toBind, GuiNode parent, ObservableValue<Float> widthRatio)
    {
        toBind.getWidthProperty()
                .bind(BaseExpression.biCombine(parent.getWidthProperty(), widthRatio, (width, ratio) -> width * ratio));
    }

    public static void bindHeightRelative(GuiNode toBind, GuiNode parent, ObservableValue<Float> heightRatio)
    {
        toBind.getHeightProperty().bind(
                BaseExpression.biCombine(parent.getHeightProperty(), heightRatio, (height, ratio) -> height * ratio));
    }

    public static void bindToPos(GuiNode toBind, GuiNode parent, ObservableValue<Float> addX, ObservableValue<Float>
            addY)
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

    public static void bindToPos(GuiNode toBind, GuiNode parent, float addX, float addY)
    {
        toBind.getxPosProperty().bind(BaseExpression.biCombine(parent.getxPosProperty(), parent.getxTranslateProperty(),
                (x, translate) -> x + translate + addX));
        toBind.getyPosProperty().bind(BaseExpression.biCombine(parent.getyPosProperty(), parent.getyTranslateProperty(),
                (y, translate) -> y + translate + addY));
    }

    public static void bindToPos(GuiNode toBind, GuiNode parent)
    {
        toBind.getxPosProperty().bind(BaseExpression.biCombine(parent.getxPosProperty(), parent.getxTranslateProperty(),
                (x, translate) -> x + translate));
        toBind.getyPosProperty().bind(BaseExpression.biCombine(parent.getyPosProperty(), parent.getyTranslateProperty(),
                (y, translate) -> y + translate));
    }

    public static void bindToCenter(GuiNode toBind, GuiNode parent)
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

    public static void bindToCenter(GuiNode toBind, GuiNode parent, float addX, float addY)
    {
        toBind.getxPosProperty()
                .bind(BaseExpression.tetraCombine(parent.getxPosProperty(),
                        parent.getWidthProperty(), parent.getxTranslateProperty(), toBind.getWidthProperty(),
                        (xPos, width, xTranslate, childWidth) ->
                                xPos + xTranslate + (width / 2 - childWidth / 2) + addX));
        toBind.getyPosProperty()
                .bind(BaseExpression.tetraCombine(parent.getyPosProperty(),
                        parent.getHeightProperty(), parent.getyTranslateProperty(), toBind.getHeightProperty(),
                        (yPos, height, yTranslate, childHeight) ->
                                yPos + yTranslate + (height / 2 - childHeight / 2) + addY));
    }

    public static void bindToRelative(GuiNode toBind, GuiNode parent, float ratioX, float ratioY)
    {
        toBind.getxPosProperty().bind(BaseExpression.tetraCombine(parent.getxPosProperty(), parent.getWidthProperty(),
                parent.getxTranslateProperty(), toBind.getWidthProperty(),
                (xPos, width, xTranslate, childWidth) -> xPos + xTranslate + (width / (1 / ratioX) - childWidth / 2)));

        toBind.getyPosProperty()
                .bind(BaseExpression.tetraCombine(parent.getyPosProperty(), parent.getHeightProperty(),
                        parent.getyTranslateProperty(), toBind.getHeightProperty(),
                        (yPos, height, yTranslate, childHeight) ->
                                yPos + yTranslate + (height / (1 / ratioY) - childHeight / 2)));
    }
}