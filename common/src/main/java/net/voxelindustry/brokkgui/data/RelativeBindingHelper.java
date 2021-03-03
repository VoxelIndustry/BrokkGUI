package net.voxelindustry.brokkgui.data;

import fr.ourten.teabeans.binding.Expression;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.component.impl.Transform;

import javax.annotation.Nullable;

/**
 * @author Ourten 9 oct. 2016
 */
public class RelativeBindingHelper
{
    public static void bindSizeRelative(Transform toBind,
                                        Transform parent,
                                        float widthRatio,
                                        float heightRatio)
    {
        RelativeBindingHelper.bindWidthRelative(toBind, parent, widthRatio);
        RelativeBindingHelper.bindHeightRelative(toBind, parent, heightRatio);
    }

    public static void bindWidthRelative(Transform toBind,
                                         Transform parent,
                                         float widthRatio)
    {
        toBind.widthProperty().bindProperty(parent.widthProperty().map(width -> width * widthRatio));
    }

    public static void bindHeightRelative(Transform toBind,
                                          Transform parent,
                                          float heightRatio)
    {
        toBind.heightProperty().bindProperty(parent.heightProperty().map(height -> height * heightRatio));
    }

    public static void bindSizeRelative(Transform toBind,
                                        Transform parent,
                                        ObservableValue<Float> widthRatio,
                                        ObservableValue<Float> heightRatio)
    {
        RelativeBindingHelper.bindWidthRelative(toBind, parent, widthRatio);
        RelativeBindingHelper.bindHeightRelative(toBind, parent, heightRatio);
    }

    public static void bindWidthRelative(Transform toBind,
                                         Transform parent,
                                         ObservableValue<Float> widthRatio)
    {
        toBind.widthProperty().bindProperty(parent.widthProperty().combine(widthRatio, (width, ratio) -> width * ratio));
    }

    public static void bindHeightRelative(Transform toBind,
                                          Transform parent,
                                          ObservableValue<Float> heightRatio)
    {
        toBind.heightProperty().bindProperty(parent.heightProperty().combine(heightRatio, (height, ratio) -> height * ratio));
    }

    public static void bindToPos(Transform toBind,
                                 Transform parent,
                                 @Nullable ObservableValue<Float> addX,
                                 @Nullable ObservableValue<Float> addY)
    {
        if (addX != null)
            toBind.xPosProperty()
                    .bindProperty(Expression.getExpression(
                            () -> parent.xPos() + parent.xTranslate() + addX.getValue(), parent.xPosProperty(),
                            parent.xTranslateProperty(), addX));
        else
            toBind.xPosProperty().bindProperty(Expression.getExpression(() -> parent.xPos() + parent.xTranslate(),
                    parent.xPosProperty(), parent.xTranslateProperty()));
        if (addY != null)
            toBind.yPosProperty()
                    .bindProperty(Expression.getExpression(
                            () -> parent.yPos() + parent.yTranslate() + addY.getValue(), parent.yPosProperty(),
                            parent.yTranslateProperty(), addY));
        else
            toBind.yPosProperty().bindProperty(Expression.getExpression(() -> parent.yPos() + parent.yTranslate(),
                    parent.yPosProperty(), parent.yTranslateProperty()));
    }

    public static void bindToPos(Transform toBind,
                                 Transform parent,
                                 float addX,
                                 float addY)
    {
        toBind.xPosProperty().bindProperty(parent.xPosProperty().combine(parent.xTranslateProperty(),
                (x, translate) -> x + translate + addX));
        toBind.yPosProperty().bindProperty(parent.yPosProperty().combine(parent.yTranslateProperty(),
                (y, translate) -> y + translate + addY));
    }

    public static void bindToPos(Transform toBind, Transform parent)
    {
        toBind.xPosProperty().bindProperty(parent.xPosProperty().combine(parent.xTranslateProperty(), Float::sum));
        toBind.yPosProperty().bindProperty(parent.yPosProperty().combine(parent.yTranslateProperty(), Float::sum));
    }

    public static void bindToCenter(Transform toBind, Transform parent)
    {
        toBind.xPosProperty()
                .bindProperty(parent.xPosProperty().combine(
                        parent.widthProperty(), parent.xTranslateProperty(), toBind.widthProperty(),
                        (xPos, width, xTranslate, childWidth) -> xPos + xTranslate + (width / 2 - childWidth / 2)));
        toBind.yPosProperty()
                .bindProperty(parent.yPosProperty().combine(
                        parent.heightProperty(), parent.yTranslateProperty(), toBind.heightProperty(),
                        (yPos, height, yTranslate, childHeight) -> yPos + yTranslate + (height / 2 - childHeight / 2)));
    }

    public static void bindToCenter(Transform toBind, Transform parent, float addX, float addY)
    {
        toBind.xPosProperty()
                .bindProperty(parent.xPosProperty().combine(
                        parent.widthProperty(), parent.xTranslateProperty(), toBind.widthProperty(),
                        (xPos, width, xTranslate, childWidth) ->
                                xPos + xTranslate + (width / 2 - childWidth / 2) + addX));
        toBind.yPosProperty()
                .bindProperty(parent.yPosProperty().combine(
                        parent.heightProperty(), parent.yTranslateProperty(), toBind.heightProperty(),
                        (yPos, height, yTranslate, childHeight) ->
                                yPos + yTranslate + (height / 2 - childHeight / 2) + addY));
    }

    public static void bindToRelative(Transform toBind, Transform parent, float ratioX, float ratioY)
    {
        toBind.xPosProperty().bindProperty(parent.xPosProperty().combine(parent.widthProperty(),
                parent.xTranslateProperty(), toBind.widthProperty(),
                (xPos, width, xTranslate, childWidth) -> xPos + xTranslate + (width / (1 / ratioX) - childWidth / 2)));

        toBind.yPosProperty()
                .bindProperty(parent.yPosProperty().combine(parent.heightProperty(),
                        parent.yTranslateProperty(), toBind.heightProperty(),
                        (yPos, height, yTranslate, childHeight) ->
                                yPos + yTranslate + (height / (1 / ratioY) - childHeight / 2)));
    }
}