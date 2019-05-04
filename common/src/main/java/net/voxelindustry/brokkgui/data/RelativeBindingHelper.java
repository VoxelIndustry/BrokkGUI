package net.voxelindustry.brokkgui.data;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.exp.component.Transform;

/**
 * @author Ourten 9 oct. 2016
 */
public class RelativeBindingHelper
{
    public static void bindSizeRelative(Transform toBind, Transform parent, float widthRatio, float heightRatio)
    {
        RelativeBindingHelper.bindWidthRelative(toBind, parent, widthRatio);
        RelativeBindingHelper.bindHeightRelative(toBind, parent, heightRatio);
    }

    public static void bindWidthRelative(Transform toBind, Transform parent, float widthRatio)
    {
        toBind.widthProperty().bind(
                BaseExpression.constantCombine(parent.widthProperty(), widthRatio, (width, ratio) -> width * ratio));
    }

    public static void bindHeightRelative(Transform toBind, Transform parent, float heightRatio)
    {
        toBind.heightProperty().bind(BaseExpression.constantCombine(parent.heightProperty(), heightRatio,
                (height, ratio) -> height * ratio));
    }

    public static void bindSizeRelative(Transform toBind, Transform parent, ObservableValue<Float> widthRatio,
                                        ObservableValue<Float> heightRatio)
    {
        RelativeBindingHelper.bindWidthRelative(toBind, parent, widthRatio);
        RelativeBindingHelper.bindHeightRelative(toBind, parent, heightRatio);
    }

    public static void bindWidthRelative(Transform toBind, Transform parent, ObservableValue<Float> widthRatio)
    {
        toBind.widthProperty()
                .bind(BaseExpression.biCombine(parent.widthProperty(), widthRatio, (width, ratio) -> width * ratio));
    }

    public static void bindHeightRelative(Transform toBind, Transform parent, ObservableValue<Float> heightRatio)
    {
        toBind.heightProperty().bind(
                BaseExpression.biCombine(parent.heightProperty(), heightRatio, (height, ratio) -> height * ratio));
    }

    public static void bindToPos(Transform toBind, Transform parent, ObservableValue<Float> addX, ObservableValue<Float>
            addY)
    {
        if (addX != null)
            toBind.xPosProperty()
                    .bind(BaseExpression.getExpression(
                            () -> parent.xPos() + parent.xTranslate() + addX.getValue(), parent.xPosProperty(),
                            parent.xTranslateProperty(), addX));
        else
            toBind.xPosProperty().bind(BaseExpression.getExpression(() -> parent.xPos() + parent.xTranslate(),
                    parent.xPosProperty(), parent.xTranslateProperty()));
        if (addY != null)
            toBind.yPosProperty()
                    .bind(BaseExpression.getExpression(
                            () -> parent.getyPos() + parent.yTranslate() + addY.getValue(), parent.yPosProperty(),
                            parent.yTranslateProperty(), addY));
        else
            toBind.yPosProperty().bind(BaseExpression.getExpression(() -> parent.getyPos() + parent.yTranslate(),
                    parent.yPosProperty(), parent.yTranslateProperty()));
    }

    public static void bindToPos(Transform toBind, Transform parent, float addX, float addY)
    {
        toBind.xPosProperty().bind(BaseExpression.biCombine(parent.xPosProperty(), parent.xTranslateProperty(),
                (x, translate) -> x + translate + addX));
        toBind.yPosProperty().bind(BaseExpression.biCombine(parent.yPosProperty(), parent.yTranslateProperty(),
                (y, translate) -> y + translate + addY));
    }

    public static void bindToPos(Transform toBind, Transform parent)
    {
        toBind.xPosProperty().bind(BaseExpression.biCombine(parent.xPosProperty(), parent.xTranslateProperty(),
                (x, translate) -> x + translate));
        toBind.yPosProperty().bind(BaseExpression.biCombine(parent.yPosProperty(), parent.yTranslateProperty(),
                (y, translate) -> y + translate));
    }

    public static void bindToCenter(Transform toBind, Transform parent)
    {
        toBind.xPosProperty()
                .bind(BaseExpression.tetraCombine(parent.xPosProperty(),
                        parent.widthProperty(), parent.xTranslateProperty(), toBind.widthProperty(),
                        (xPos, width, xTranslate, childWidth) -> xPos + xTranslate + (width / 2 - childWidth / 2)));
        toBind.yPosProperty()
                .bind(BaseExpression.tetraCombine(parent.yPosProperty(),
                        parent.heightProperty(), parent.yTranslateProperty(), toBind.heightProperty(),
                        (yPos, height, yTranslate, childHeight) -> yPos + yTranslate + (height / 2 - childHeight / 2)));
    }

    public static void bindToCenter(Transform toBind, Transform parent, float addX, float addY)
    {
        toBind.xPosProperty()
                .bind(BaseExpression.tetraCombine(parent.xPosProperty(),
                        parent.widthProperty(), parent.xTranslateProperty(), toBind.widthProperty(),
                        (xPos, width, xTranslate, childWidth) ->
                                xPos + xTranslate + (width / 2 - childWidth / 2) + addX));
        toBind.yPosProperty()
                .bind(BaseExpression.tetraCombine(parent.yPosProperty(),
                        parent.heightProperty(), parent.yTranslateProperty(), toBind.heightProperty(),
                        (yPos, height, yTranslate, childHeight) ->
                                yPos + yTranslate + (height / 2 - childHeight / 2) + addY));
    }

    public static void bindToRelative(Transform toBind, Transform parent, float ratioX, float ratioY)
    {
        toBind.xPosProperty().bind(BaseExpression.tetraCombine(parent.xPosProperty(), parent.widthProperty(),
                parent.xTranslateProperty(), toBind.widthProperty(),
                (xPos, width, xTranslate, childWidth) -> xPos + xTranslate + (width / (1 / ratioX) - childWidth / 2)));

        toBind.yPosProperty()
                .bind(BaseExpression.tetraCombine(parent.yPosProperty(), parent.heightProperty(),
                        parent.yTranslateProperty(), toBind.heightProperty(),
                        (yPos, height, yTranslate, childHeight) ->
                                yPos + yTranslate + (height / (1 / ratioY) - childHeight / 2)));
    }
}