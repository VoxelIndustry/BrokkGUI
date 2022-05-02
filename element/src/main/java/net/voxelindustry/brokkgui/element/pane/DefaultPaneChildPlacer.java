package net.voxelindustry.brokkgui.element.pane;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;

public class DefaultPaneChildPlacer<T extends GuiElement> implements PaneChildPlacer
{
    private final T element;

    private Transform currentChild;

    private SecondStepChildPlacer<T> secondStepChildPlacer;

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

    public SecondStepChildPlacer<T> left()
    {
        return absolute(0);
    }

    public SecondStepChildPlacer<T> right()
    {
        RelativeBindingHelper.bindXToPos(currentChild,
                element.transform(),
                element.transform().widthProperty().subtract(currentChild.widthProperty()));
        return getOrCreateSecondChildPlacer();
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

    public T absolute(float x, float y, RectAlignment alignment)
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

    public T relative(float ratioX, float ratioY)
    {
        RelativeBindingHelper.bindToRelative(currentChild,
                element.transform(),
                ratioX,
                ratioY);
        return element;
    }

    public T relativeWithOffset(float ratioX, float ratioY, float offsetX, float offsetY)
    {
        RelativeBindingHelper.bindToRelative(currentChild,
                element.transform(),
                ratioX,
                ratioY,
                offsetX,
                offsetY);
        return element;
    }

    public SecondStepChildPlacer<T> absolute(float x)
    {
        RelativeBindingHelper.bindXToPos(currentChild, element.transform(), x);
        return getOrCreateSecondChildPlacer();
    }

    public SecondStepChildPlacer<T> relative(float x)
    {
        RelativeBindingHelper.bindXToRelative(currentChild, element.transform(), x);
        return getOrCreateSecondChildPlacer();
    }

    private SecondStepChildPlacer<T> getOrCreateSecondChildPlacer()
    {
        if (secondStepChildPlacer == null)
            secondStepChildPlacer = new SecondStepChildPlacer<>(this);
        return secondStepChildPlacer;
    }

    public static record SecondStepChildPlacer<T extends GuiElement>(DefaultPaneChildPlacer<T> parentPlacer)
    {
        public T absolute(float y)
        {
            RelativeBindingHelper.bindYToPos(parentPlacer.currentChild, parentPlacer.element.transform(), y);
            return parentPlacer.element;
        }

        public T relative(float y)
        {
            RelativeBindingHelper.bindYToRelative(parentPlacer.currentChild, parentPlacer.element.transform(), y);
            return parentPlacer.element;
        }

        public T top()
        {
            return absolute(0);
        }

        public T right()
        {
            RelativeBindingHelper.bindXToPos(parentPlacer.currentChild,
                    parentPlacer.element.transform(),
                    parentPlacer.element.transform().widthProperty().subtract(parentPlacer.currentChild.widthProperty()));
            return parentPlacer.element;
        }
    }
}
