package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public class DefaultPaneChildPlacer<T extends GuiElement> implements PaneChildPlacer
{
    private final T element;

    private Transform currentChild;

    public DefaultPaneChildPlacer(T element)
    {
        this.element = element;
    }

    protected void currentChild(Transform child)
    {
        currentChild = child;
    }

    public T leftTop()
    {
        RelativeBindingHelper.bindToPos(currentChild, element.transform());
        return element;
    }

    public T rightTop()
    {
        RelativeBindingHelper.bindToPos(currentChild,
                element.transform(),
                element.transform().widthProperty().subtract(currentChild.widthProperty()),
                null);
        return element;
    }

    public T leftBottom()
    {
        RelativeBindingHelper.bindToPos(currentChild,
                element.transform(),
                null,
                element.transform().heightProperty().subtract(currentChild.heightProperty()));
        return element;
    }

    public T rightBottom()
    {
        RelativeBindingHelper.bindToPos(currentChild,
                element.transform(),
                element.transform().widthProperty().subtract(currentChild.widthProperty()),
                element.transform().heightProperty().subtract(currentChild.heightProperty()));
        return element;
    }

    public T centered()
    {
        RelativeBindingHelper.bindToCenter(currentChild, element.transform());
        return element;
    }

    public T centered(float offsetX, float offsetY)
    {
        RelativeBindingHelper.bindToCenter(currentChild, element.transform(), offsetX, offsetY);
        return element;
    }

    public T absolute(float x, float y)
    {
        RelativeBindingHelper.bindToPos(currentChild,
                element.transform(),
                x,
                y);
        return element;
    }

    public T relative(float ratioX, float ratioY)
    {
        RelativeBindingHelper.bindToRelative(currentChild,
                element.transform(),
                ratioX,
                ratioY);
        return element;
    }
}
