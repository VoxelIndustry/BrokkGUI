package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public class HorizontalPaneChildPlacer<T extends GuiElement> implements PaneChildPlacer
{
    private final T element;

    private Transform currentChild;

    public HorizontalPaneChildPlacer(T element)
    {
        this.element = element;
    }

    protected void currentChild(Transform child)
    {
        currentChild = child;
    }

    public HorizontalPaneChildPlacer<T> marginLeft(float left)
    {
        return margin(left, 0);
    }

    public HorizontalPaneChildPlacer<T> marginRight(float right)
    {
        return margin(0, right);
    }

    public HorizontalPaneChildPlacer<T> margin(float left, float right)
    {
        currentChild.margin(RectBox.build()
                .left(left)
                .right(right)
                .create());
        return this;
    }

    public HorizontalPaneChildPlacer<T> margin(RectBox margin)
    {
        currentChild.margin(margin);
        return this;
    }

    public HorizontalPaneChildPlacer<T> noMargin()
    {
        currentChild.margin(RectBox.EMPTY);
        return this;
    }


    public T top()
    {
        return absolute(0);
    }

    public T bottom()
    {
        RelativeBindingHelper.bindXToPos(currentChild,
                element.transform(),
                element.transform().heightProperty().subtract(currentChild.heightProperty()));
        return element;
    }

    public T centered()
    {
        RelativeBindingHelper.bindYToCenter(currentChild, element.transform());
        return element;
    }

    public T centered(float offsetY)
    {
        RelativeBindingHelper.bindYToCenter(currentChild, element.transform(), offsetY);
        return element;
    }

    public T absolute(float y, RectAlignment alignment)
    {
        switch (alignment)
        {
            case LEFT_CENTER, RIGHT_CENTER, MIDDLE_CENTER ->
            {
                RelativeBindingHelper.bindYToPos(currentChild,
                        element.transform(),
                        currentChild.heightProperty().map(height -> y - height.floatValue() / 2));
            }
            case LEFT_UP, MIDDLE_UP, RIGHT_UP ->
            {
                RelativeBindingHelper.bindYToPos(currentChild,
                        element.transform(),
                        y);
            }
            case LEFT_DOWN, MIDDLE_DOWN, RIGHT_DOWN ->
            {
                RelativeBindingHelper.bindYToPos(currentChild,
                        element.transform(),
                        currentChild.heightProperty().map(height -> y - height.floatValue()));
            }
        }

        return element;
    }

    public T relativeWithOffset(float ratioY, float offsetY)
    {
        RelativeBindingHelper.bindYToRelative(
                currentChild,
                element.transform(),
                ratioY,
                offsetY
        );
        return element;
    }

    public T absolute(float y)
    {
        RelativeBindingHelper.bindYToPos(currentChild, element.transform(), y);
        return element;
    }

    public T relative(float y)
    {
        RelativeBindingHelper.bindYToRelative(currentChild, element.transform(), y);
        return element;
    }
}
