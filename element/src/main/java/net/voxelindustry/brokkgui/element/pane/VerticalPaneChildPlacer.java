package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public class VerticalPaneChildPlacer<T extends GuiElement> implements PaneChildPlacer
{
    private final T element;

    private Transform currentChild;

    public VerticalPaneChildPlacer(T element)
    {
        this.element = element;
    }

    protected void currentChild(Transform child)
    {
        currentChild = child;
    }

    public VerticalPaneChildPlacer<T> marginTop(float top)
    {
        return margin(top, 0);
    }

    public VerticalPaneChildPlacer<T> marginBottom(float bottom)
    {
        return margin(0, bottom);
    }

    public VerticalPaneChildPlacer<T> margin(float top, float bottom)
    {
        currentChild.margin(RectBox.build()
                .top(top)
                .bottom(bottom)
                .create());
        return this;
    }

    public VerticalPaneChildPlacer<T> margin(RectBox margin)
    {
        currentChild.margin(margin);
        return this;
    }

    public VerticalPaneChildPlacer<T> noMargin()
    {
        currentChild.margin(RectBox.EMPTY);
        return this;
    }


    public T left()
    {
        return absolute(0);
    }

    public T right()
    {
        RelativeBindingHelper.bindXToPos(currentChild,
                element.transform(),
                element.transform().widthProperty().subtract(currentChild.widthProperty()));
        return element;
    }

    public T centered()
    {
        RelativeBindingHelper.bindXToCenter(currentChild, element.transform());
        return element;
    }

    public T centered(float offsetX)
    {
        RelativeBindingHelper.bindXToCenter(currentChild, element.transform(), offsetX);
        return element;
    }

    public T absolute(float x, RectAlignment alignment)
    {
        switch (alignment)
        {
            case RIGHT_CENTER, RIGHT_UP, RIGHT_DOWN ->
            {
                RelativeBindingHelper.bindXToPos(currentChild,
                        element.transform(),
                        currentChild.widthProperty().map(width -> x - width.floatValue()));
            }
            case MIDDLE_CENTER, MIDDLE_UP, MIDDLE_DOWN ->
            {
                RelativeBindingHelper.bindXToPos(currentChild,
                        element.transform(),
                        currentChild.widthProperty().map(width -> x - width.floatValue() / 2));
            }
            case LEFT_CENTER, LEFT_UP, LEFT_DOWN ->
            {
                RelativeBindingHelper.bindXToPos(currentChild,
                        element.transform(),
                        x);
            }
        }

        return element;
    }

    public T relativeWithOffset(float ratioX, float offsetX)
    {
        RelativeBindingHelper.bindXToRelative(
                currentChild,
                element.transform(),
                ratioX,
                offsetX
        );
        return element;
    }

    public T absolute(float x)
    {
        RelativeBindingHelper.bindXToPos(currentChild, element.transform(), x);
        return element;
    }

    public T relative(float x)
    {
        RelativeBindingHelper.bindXToRelative(currentChild, element.transform(), x);
        return element;
    }
}
