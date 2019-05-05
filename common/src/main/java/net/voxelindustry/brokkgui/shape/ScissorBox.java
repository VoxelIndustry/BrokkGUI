package net.voxelindustry.brokkgui.shape;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.component.Transform;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Predicate;

public class ScissorBox
{
    private Transform              transform;
    private ObservableValue<Float> startX, startY, endX, endY;
    private Predicate<RenderPass> renderPassPredicate;

    private ScissorBox()
    {
        this.renderPassPredicate = renderPass -> true;
    }

    public static ScissorBox fitNode(Transform transform)
    {
        ScissorBox box = new ScissorBox();
        box.transform = transform;

        box.startX = BaseExpression.biCombine(transform.xPosProperty(), transform.xTranslateProperty(), Float::sum);
        box.startY = BaseExpression.biCombine(transform.yPosProperty(), transform.yTranslateProperty(), Float::sum);

        box.endX = BaseExpression.biCombine(box.startX, transform.widthProperty(), Float::sum);
        box.endY = BaseExpression.biCombine(box.startY, transform.heightProperty(), Float::sum);

        return box;
    }

    public static ScissorBox withRegion(float startX, float startY, float endX, float endY)
    {
        ScissorBox box = new ScissorBox();

        box.startX = new BaseProperty<>(startX, "startXProperty");
        box.startY = new BaseProperty<>(startY, "startYProperty");
        box.endX = new BaseProperty<>(endX, "endXProperty");
        box.endY = new BaseProperty<>(endY, "endYProperty");

        return box;
    }

    public void dispose()
    {
        if (transform == null)
            return;

        ((BaseExpression<Float>) this.startX).unbind(transform.xPosProperty(), transform.xTranslateProperty());
        ((BaseExpression<Float>) this.startY).unbind(transform.yPosProperty(), transform.yTranslateProperty());

        ((BaseExpression<Float>) this.endX).unbind(this.startX, transform.widthProperty());
        ((BaseExpression<Float>) this.endY).unbind(this.startY, transform.heightProperty());
    }

    public void setValidPass(RenderPass... passArray)
    {
        this.renderPassPredicate = pass -> ArrayUtils.contains(passArray, pass);
    }

    public void setRenderPassPredicate(Predicate<RenderPass> renderPassPredicate)
    {
        this.renderPassPredicate = renderPassPredicate;
    }

    public boolean setupAndApply(IGuiRenderer renderer, RenderPass pass)
    {
        if (!renderPassPredicate.test(pass))
            return false;

        renderer.getHelper().beginScissor();
        renderer.getHelper().scissorBox(this.startX.getValue(), this.startY.getValue(), this.endX.getValue(),
                this.endY.getValue());
        return true;
    }

    public void end(IGuiRenderer renderer)
    {
        renderer.getHelper().endScissor();
    }
}
