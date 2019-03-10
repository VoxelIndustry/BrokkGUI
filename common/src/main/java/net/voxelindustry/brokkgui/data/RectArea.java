package net.voxelindustry.brokkgui.data;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.component.GuiNode;

public class RectArea
{
    private GuiNode                node;
    private ObservableValue<Float> startX, startY, endX, endY;

    public static RectArea fitNode(GuiNode node)
    {
        return nodePart(node, 1, 1);
    }

    public static RectArea nodePart(GuiNode node, float widthFrac, float heightFrac)
    {
        RectArea rectArea = new RectArea();
        rectArea.node = node;

        rectArea.startX = BaseExpression.biCombine(node.getxPosProperty(), node.getxTranslateProperty(), Float::sum);
        rectArea.startY = BaseExpression.biCombine(node.getyPosProperty(), node.getyTranslateProperty(), Float::sum);

        rectArea.endX = BaseExpression.biCombine(rectArea.startX, node.getWidthProperty(),
                (startX, width) -> startX + width * widthFrac);
        rectArea.endY = BaseExpression.biCombine(rectArea.startY, node.getHeightProperty(),
                (startY, height) -> startY + height * heightFrac);

        return rectArea;
    }

    public static RectArea withRegion(float startX, float startY, float endX, float endY)
    {
        RectArea rectArea = new RectArea();

        rectArea.startX = new BaseProperty<>(startX, "startXProperty");
        rectArea.startY = new BaseProperty<>(startY, "startYProperty");
        rectArea.endX = new BaseProperty<>(endX, "endXProperty");
        rectArea.endY = new BaseProperty<>(endY, "endYProperty");

        return rectArea;
    }

    public void dispose()
    {
        if (node == null)
            return;

        ((BaseExpression<Float>) this.startX).unbind(node.getxPosProperty(), node.getxTranslateProperty());
        ((BaseExpression<Float>) this.startY).unbind(node.getyPosProperty(), node.getyTranslateProperty());

        ((BaseExpression<Float>) this.endX).unbind(this.startX, node.getWidthProperty());
        ((BaseExpression<Float>) this.endY).unbind(this.startY, node.getHeightProperty());
    }

    public boolean isPointInside(float x, float y)
    {
        return x > startX.getValue() && x < endX.getValue() && y > startY.getValue() && y < endY.getValue();
    }
}
