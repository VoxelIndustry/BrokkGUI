package net.voxelindustry.brokkgui.shape;

import fr.ourten.teabeans.binding.Expression;
import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.component.impl.Transform;
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
        renderPassPredicate = renderPass -> true;
    }

    public static ScissorBox fitNode(Transform transform)
    {
        ScissorBox box = new ScissorBox();
        box.transform = transform;

        box.startX = Expression.biCombine(transform.xPosProperty(), transform.xTranslateProperty(), Float::sum);
        box.startY = Expression.biCombine(transform.yPosProperty(), transform.yTranslateProperty(), Float::sum);

        box.endX = Expression.biCombine(box.startX, transform.widthProperty(), Float::sum);
        box.endY = Expression.biCombine(box.startY, transform.heightProperty(), Float::sum);

        return box;
    }

    public static ScissorBox withRegion(float startX, float startY, float endX, float endY)
    {
        ScissorBox box = new ScissorBox();

        box.startX = new Property<>(startX);
        box.startY = new Property<>(startY);
        box.endX = new Property<>(endX);
        box.endY = new Property<>(endY);

        return box;
    }

    public void dispose()
    {
        if (transform == null)
            return;

        ((Expression<Float>) startX).unbind(transform.xPosProperty(), transform.xTranslateProperty());
        ((Expression<Float>) startY).unbind(transform.yPosProperty(), transform.yTranslateProperty());

        ((Expression<Float>) endX).unbind(startX, transform.widthProperty());
        ((Expression<Float>) endY).unbind(startY, transform.heightProperty());
    }

    public void setValidPass(RenderPass... passArray)
    {
        renderPassPredicate = pass -> ArrayUtils.contains(passArray, pass);
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
        renderer.getHelper().scissorBox(startX.getValue(), startY.getValue(), endX.getValue(),
                endY.getValue());
        return true;
    }

    public void end(IGuiRenderer renderer)
    {
        renderer.getHelper().endScissor();
    }
}
