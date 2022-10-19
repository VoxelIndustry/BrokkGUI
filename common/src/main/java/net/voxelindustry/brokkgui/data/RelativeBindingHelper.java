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
        toBind.widthProperty().bindProperty(parent.widthProperty().map(width -> width.floatValue() * widthRatio));
    }

    public static void bindHeightRelative(Transform toBind,
                                          Transform parent,
                                          float heightRatio)
    {
        toBind.heightProperty().bindProperty(parent.heightProperty().map(height -> height.floatValue() * heightRatio));
    }

    public static void bindSizeRelative(Transform toBind,
                                        Transform parent,
                                        ObservableValue<Number> widthRatio,
                                        ObservableValue<Number> heightRatio)
    {
        RelativeBindingHelper.bindWidthRelative(toBind, parent, widthRatio);
        RelativeBindingHelper.bindHeightRelative(toBind, parent, heightRatio);
    }

    public static void bindWidthRelative(Transform toBind,
                                         Transform parent,
                                         ObservableValue<Number> widthRatio)
    {
        toBind.widthProperty().bindProperty(parent.widthProperty().multiply(widthRatio));
    }

    public static void bindHeightRelative(Transform toBind,
                                          Transform parent,
                                          ObservableValue<Number> heightRatio)
    {
        toBind.heightProperty().bindProperty(parent.heightProperty().multiply(heightRatio));
    }

    public static void bindToPos(Transform toBind,
                                 Transform parent,
                                 @Nullable ObservableValue<Number> addX,
                                 @Nullable ObservableValue<Number> addY)
    {
        bindXToPos(toBind, parent, addX);
        bindYToPos(toBind, parent, addY);
    }

    public static void bindXToPos(Transform toBind,
                                  Transform parent,
                                  @Nullable ObservableValue<Number> addX)
    {
        if (addX != null)
            toBind.xPosProperty()
                    .bindProperty(Expression.getExpression(
                            () -> parent.xPos() + parent.xTranslate() + parent.xOffsetProperty().get() + addX.getValue().floatValue(), parent.xPosProperty(),
                            parent.xTranslateProperty(), parent.xOffsetProperty(), addX));
        else
            toBind.xPosProperty().bindProperty(Expression.getExpression(() -> parent.xPos() + parent.xTranslate() + parent.xOffsetProperty().get(),
                    parent.xPosProperty(), parent.xTranslateProperty(), parent.xOffsetProperty()));
    }

    public static void bindYToPos(Transform toBind,
                                  Transform parent,
                                  @Nullable ObservableValue<Number> addY)
    {
        if (addY != null)
            toBind.yPosProperty()
                    .bindProperty(Expression.getExpression(
                            () -> parent.yPos() + parent.yTranslate() + parent.yOffsetProperty().get() + addY.getValue().floatValue(), parent.yPosProperty(),
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
        bindXToPos(toBind, parent, addX);
        bindYToPos(toBind, parent, addY);
    }

    public static void bindXToPos(Transform toBind,
                                  Transform parent,
                                  float addX)
    {
        toBind.xPosProperty().bindProperty(parent.xPosProperty().combine(
                parent.xTranslateProperty(),
                parent.xOffsetProperty(),
                (x, translate, xOffset) -> x.floatValue() + translate.floatValue() + xOffset.floatValue() + addX));
    }

    public static void bindYToPos(Transform toBind,
                                  Transform parent,
                                  float addY)
    {
        toBind.yPosProperty().bindProperty(parent.yPosProperty().combine(
                parent.yTranslateProperty(),
                parent.yOffsetProperty(),
                (y, translate, yOffset) -> y.floatValue() + translate.floatValue() + yOffset.floatValue() + addY));
    }

    public static void bindToPos(Transform toBind, Transform parent)
    {
        toBind.xPosProperty().bindProperty(parent.xPosProperty().combine(
                parent.xTranslateProperty(),
                parent.xOffsetProperty(),
                (xPos, xTranslate, xOffset) -> xPos.floatValue() + xTranslate.floatValue() + xOffset.floatValue()));

        toBind.yPosProperty().bindProperty(parent.yPosProperty().combine(
                parent.yTranslateProperty(),
                parent.yOffsetProperty(),
                (yPos, yTranslate, yOffset) -> yPos.floatValue() + yTranslate.floatValue() + yOffset.floatValue()));
    }

    public static void bindXToCenter(Transform toBind, Transform parent)
    {
        toBind.xPosProperty()
                .bindProperty(parent.xPosProperty().combine(
                        parent.widthProperty(),
                        parent.xTranslateProperty(),
                        parent.xOffsetProperty(),
                        toBind.widthProperty(),
                        (xPos, width, xTranslate, xOffset, childWidth) -> xPos.floatValue() + xTranslate.floatValue() + xOffset.floatValue() + (width.floatValue() / 2 - childWidth.floatValue() / 2)));
    }

    public static void bindYToCenter(Transform toBind, Transform parent)
    {
        toBind.yPosProperty()
                .bindProperty(parent.yPosProperty().combine(
                        parent.heightProperty(),
                        parent.yTranslateProperty(),
                        parent.yOffsetProperty(),
                        toBind.heightProperty(),
                        (yPos, height, yTranslate, yOffset, childHeight) -> yPos.floatValue() + yTranslate.floatValue() + yOffset.floatValue() + (height.floatValue() / 2 - childHeight.floatValue() / 2)));
    }

    public static void bindToCenter(Transform toBind, Transform parent)
    {
        toBind.xPosProperty()
                .bindProperty(parent.xPosProperty().combine(
                        parent.widthProperty(),
                        parent.xTranslateProperty(),
                        parent.xOffsetProperty(),
                        toBind.widthProperty(),
                        (xPos, width, xTranslate, xOffset, childWidth) -> xPos.floatValue() + xTranslate.floatValue() + xOffset.floatValue() + (width.floatValue() / 2 - childWidth.floatValue() / 2)));

        toBind.yPosProperty()
                .bindProperty(parent.yPosProperty().combine(
                        parent.heightProperty(),
                        parent.yTranslateProperty(),
                        parent.yOffsetProperty(),
                        toBind.heightProperty(),
                        (yPos, height, yTranslate, yOffset, childHeight) -> yPos.floatValue() + yTranslate.floatValue() + yOffset.floatValue() + (height.floatValue() / 2 - childHeight.floatValue() / 2)));
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
                                xPos.floatValue() + xTranslate.floatValue() + xOffset.floatValue() + (width.floatValue() / 2 - childWidth.floatValue() / 2) + addX));
        toBind.yPosProperty()
                .bindProperty(parent.yPosProperty().combine(
                        parent.heightProperty(),
                        parent.yTranslateProperty(),
                        parent.yOffsetProperty(),
                        toBind.heightProperty(),
                        (yPos, height, yTranslate, yOffset, childHeight) ->
                                yPos.floatValue() + yTranslate.floatValue() + yOffset.floatValue() + (height.floatValue() / 2 - childHeight.floatValue() / 2) + addY));
    }

    public static void bindToRelative(Transform toBind, Transform parent, float ratioX, float ratioY)
    {
        bindToRelative(toBind, parent, ratioX, ratioY, 0, 0);
    }

    public static void bindToRelative(Transform toBind, Transform parent, float ratioX, float ratioY, float addX, float addY)
    {
        bindXToRelative(toBind, parent, ratioX, addX);
        bindYToRelative(toBind, parent, ratioY, addY);
    }

    public static void bindXToRelative(Transform toBind, Transform parent, float ratioX)
    {
        bindXToRelative(toBind, parent, ratioX, 0);
    }

    public static void bindXToRelative(Transform toBind, Transform parent, float ratioX, float addX)
    {
        toBind.xPosProperty().bindProperty(parent.xPosProperty().combine(
                parent.widthProperty(),
                parent.xTranslateProperty(),
                parent.xOffsetProperty(),
                toBind.widthProperty(),
                (xPos, width, xTranslate, xOffset, childWidth) ->
                        xPos.floatValue() + xTranslate.floatValue() + xOffset.floatValue() + addX + (width.floatValue() / (1 / ratioX) - childWidth.floatValue() / 2)));
    }

    public static void bindYToRelative(Transform toBind, Transform parent, float ratioY)
    {
        bindYToRelative(toBind, parent, ratioY, 0);
    }

    public static void bindYToRelative(Transform toBind, Transform parent, float ratioY, float addY)
    {
        toBind.yPosProperty()
                .bindProperty(parent.yPosProperty().combine(
                        parent.heightProperty(),
                        parent.yTranslateProperty(),
                        parent.yOffsetProperty(),
                        toBind.heightProperty(),
                        (yPos, height, yTranslate, yOffset, childHeight) ->
                                yPos.floatValue() + yTranslate.floatValue() + yOffset.floatValue() + addY + (height.floatValue() / (1 / ratioY) - childHeight.floatValue() / 2)));
    }
}