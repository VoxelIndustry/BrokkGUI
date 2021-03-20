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
                            () -> parent.xPos() + parent.xTranslate() + parent.xOffsetProperty().get() + addX.getValue(), parent.xPosProperty(),
                            parent.xTranslateProperty(), parent.xOffsetProperty(), addX));
        else
            toBind.xPosProperty().bindProperty(Expression.getExpression(() -> parent.xPos() + parent.xTranslate() + parent.xOffsetProperty().get(),
                    parent.xPosProperty(), parent.xTranslateProperty(), parent.xOffsetProperty()));
        if (addY != null)
            toBind.yPosProperty()
                    .bindProperty(Expression.getExpression(
                            () -> parent.yPos() + parent.yTranslate() + parent.yOffsetProperty().get() + addY.getValue(), parent.yPosProperty(),
                            parent.yTranslateProperty(), parent.yOffsetProperty(), addY));
        else
            toBind.yPosProperty().bindProperty(Expression.getExpression(() -> parent.yPos() + parent.yTranslate() + parent.yOffsetProperty().get(),
                    parent.yPosProperty(), parent.yTranslateProperty(), parent.yOffsetProperty()));
    }

    public static void bindToPos(Transform toBind,
                                 Transform parent,
                                 float addX,
                                 float addY)
    {
        toBind.xPosProperty().bindProperty(parent.xPosProperty().combine(
                parent.xTranslateProperty(),
                parent.xOffsetProperty(),
                (x, translate, xOffset) -> x + translate + xOffset + addX));

        toBind.yPosProperty().bindProperty(parent.yPosProperty().combine(
                parent.yTranslateProperty(),
                parent.yOffsetProperty(),
                (y, translate, yOffset) -> y + translate + yOffset + addY));
    }

    public static void bindToPos(Transform toBind, Transform parent)
    {
        toBind.xPosProperty().bindProperty(parent.xPosProperty().combine(
                parent.xTranslateProperty(),
                parent.xOffsetProperty(),
                (xPos, xTranslate, xOffset) -> xPos + xTranslate + xOffset));

        toBind.yPosProperty().bindProperty(parent.yPosProperty().combine(
                parent.yTranslateProperty(),
                parent.yOffsetProperty(),
                (yPos, yTranslate, yOffset) -> yPos + yTranslate + yOffset));
    }

    public static void bindToCenter(Transform toBind, Transform parent)
    {
        toBind.xPosProperty()
                .bindProperty(parent.xPosProperty().combine(
                        parent.widthProperty(),
                        parent.xTranslateProperty(),
                        parent.xOffsetProperty(),
                        toBind.widthProperty(),
                        (xPos, width, xTranslate, xOffset, childWidth) -> xPos + xTranslate + xOffset + (width / 2 - childWidth / 2)));

        toBind.yPosProperty()
                .bindProperty(parent.yPosProperty().combine(
                        parent.heightProperty(),
                        parent.yTranslateProperty(),
                        parent.yOffsetProperty(),
                        toBind.heightProperty(),
                        (yPos, height, yTranslate, yOffset, childHeight) -> yPos + yTranslate + yOffset + (height / 2 - childHeight / 2)));
    }

    public static void bindToCenter(Transform toBind, Transform parent, float addX, float addY)
    {
        toBind.xPosProperty()
                .bindProperty(parent.xPosProperty().combine(
                        parent.widthProperty(),
                        parent.xTranslateProperty(),
                        parent.xOffsetProperty(),
                        toBind.widthProperty(),
                        (xPos, width, xTranslate, xOffset, childWidth) ->
                                xPos + xTranslate + xOffset + (width / 2 - childWidth / 2) + addX));
        toBind.yPosProperty()
                .bindProperty(parent.yPosProperty().combine(
                        parent.heightProperty(),
                        parent.yTranslateProperty(),
                        parent.yOffsetProperty(),
                        toBind.heightProperty(),
                        (yPos, height, yTranslate, yOffset, childHeight) ->
                                yPos + yTranslate + yOffset + (height / 2 - childHeight / 2) + addY));
    }

    public static void bindToRelative(Transform toBind, Transform parent, float ratioX, float ratioY)
    {
        toBind.xPosProperty().bindProperty(parent.xPosProperty().combine(
                parent.widthProperty(),
                parent.xTranslateProperty(),
                parent.xOffsetProperty(),
                toBind.widthProperty(),
                (xPos, width, xTranslate, xOffset, childWidth) -> xPos + xTranslate + xOffset + (width / (1 / ratioX) - childWidth / 2)));

        toBind.yPosProperty()
                .bindProperty(parent.yPosProperty().combine(
                        parent.heightProperty(),
                        parent.yTranslateProperty(),
                        parent.yOffsetProperty(),
                        toBind.heightProperty(),
                        (yPos, height, yTranslate, yOffset, childHeight) ->
                                yPos + yTranslate + yOffset + (height / (1 / ratioY) - childHeight / 2)));
    }
}