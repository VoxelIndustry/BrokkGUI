package net.voxelindustry.brokkgui.layout;

import static java.lang.Float.max;
import static java.lang.Float.min;

public class LayoutGroup implements ILayoutBox
{
    private ILayoutBox first;
    private ILayoutBox second;

    private LayoutAlignment firstAlignment;

    // Dimension constraints computed from the children
    private float minWidth;
    private float minHeight;
    private float prefWidth;
    private float prefHeight;
    private float maxWidth;
    private float maxHeight;

    // Constraints imposed by the parent of this Group
    private float layoutWidth;
    private float layoutHeight;

    // Computed actual dimension
    private float effectiveWidth;
    private float effectiveHeight;

    // Computed actual children positions
    private float firstPosX;
    private float firstPosY;
    private float secondPosX;
    private float secondPosY;

    // Computed actual children dimensions
    private float firstWidth;
    private float firstHeight;
    private float secondWidth;
    private float secondHeight;

    private boolean isDirty = true;

    public LayoutGroup(final ILayoutBox first, final ILayoutBox second, final LayoutAlignment firstAlignment)
    {
        this.first = first;
        this.second = second;
        this.firstAlignment = firstAlignment;
    }

    public LayoutGroup()
    {
    }

    private void refreshLayout()
    {
        minWidth = computeWidth(first.minWidth(), second.minWidth());
        minHeight = computeHeight(first.minHeight(), second.minHeight());

        prefWidth = computeWidth(first.prefWidth(), second.prefWidth());
        prefHeight = computeHeight(first.prefHeight(), second.prefHeight());

        maxWidth = computeWidth(first.maxWidth(), second.maxWidth());
        maxHeight = computeHeight(first.maxHeight(), second.maxHeight());

        computeEffectiveSize();
        computeChildrenSize();
        computeChildrenPositions();
    }

    private void computeChildrenPositions()
    {
        if (firstAlignment.isHorizontalCenter())
        {
            if (firstWidth < effectiveWidth)
                firstPosX = effectiveWidth / 2 - firstWidth / 2;
            else
                firstPosX = 0;
            secondPosX = 0;
        }
        else
        {
            if (firstAlignment.isLeft())
            {
                firstPosX = 0;
                secondPosX = firstWidth;
            }
            else if (firstAlignment.isRight())
            {
                firstPosX = secondWidth;
                secondPosX = 0;
            }
        }

        if (firstAlignment.isVerticalCenter())
        {
            if (firstHeight < effectiveHeight)
                firstPosY = effectiveHeight / 2 - firstHeight / 2;
            else
                firstPosY = 0;
            secondPosY = 0;
        }
        else
        {
            if (firstAlignment.isTop())
            {
                firstPosY = 0;
                secondPosY = firstHeight;
            }
            else if (firstAlignment.isBottom())
            {
                firstPosY = secondHeight;
                secondPosY = 0;
            }
        }
    }

    private void computeChildrenSize()
    {
        if (firstAlignment.isHorizontalCenter() && (firstAlignment.isTop() || firstAlignment.isBottom()))
        {
            firstWidth = min(effectiveWidth, first.maxWidth());
            secondWidth = min(effectiveWidth, second.maxWidth());
        }
        else
        {
            if (effectiveWidth < prefWidth)
                firstWidth = min(effectiveWidth - second.minWidth(), first.maxWidth());
            else if (effectiveWidth >= prefWidth)
                firstWidth = min(effectiveWidth - second.prefWidth(), first.maxWidth());
            secondWidth = max(effectiveWidth - firstWidth, second.minWidth());
        }

        if (firstAlignment.isVerticalCenter() && (firstAlignment.isLeft() || firstAlignment.isRight()))
        {
            firstHeight = min(effectiveHeight, first.maxHeight());
            secondHeight = min(effectiveHeight, second.maxHeight());
        }
        else
        {
            if (effectiveHeight < prefHeight)
                firstHeight = min(effectiveHeight - second.minHeight(), first.maxHeight());
            else if (effectiveWidth >= prefWidth)
                firstHeight = min(effectiveHeight - second.prefHeight(), first.maxHeight());
            secondHeight = max(effectiveHeight - firstHeight, second.minHeight());
        }
    }

    private void computeEffectiveSize()
    {
        if (prefWidth >= layoutWidth)
            effectiveWidth = max(minWidth, layoutWidth);
        else
            effectiveWidth = min(maxWidth, layoutWidth);

        if (prefHeight >= layoutHeight)
            effectiveHeight = max(minHeight, layoutHeight);
        else
            effectiveHeight = min(maxHeight, layoutHeight);
    }

    private float computeWidth(final float firstWidth, final float secondWidth)
    {
        if (firstAlignment.isHorizontalCenter() && (firstAlignment.isTop() || firstAlignment.isBottom()))
            return max(firstWidth, secondWidth);
        return firstWidth + secondWidth;
    }

    private float computeHeight(final float firstHeight, final float secondHeight)
    {
        if (firstAlignment.isVerticalCenter() && (firstAlignment.isLeft() || firstAlignment.isRight()))
            return max(firstHeight, secondHeight);
        return firstHeight + secondHeight;
    }

    @Override
    public float minWidth()
    {
        if (isDirty())
            refreshLayout();
        return minWidth;
    }

    @Override
    public float minHeight()
    {
        if (isDirty())
            refreshLayout();
        return minHeight;
    }

    @Override
    public float prefWidth()
    {
        if (isDirty())
            refreshLayout();
        return prefWidth;
    }

    @Override
    public float prefHeight()
    {
        if (isDirty())
            refreshLayout();
        return prefHeight;
    }

    @Override
    public float maxWidth()
    {
        if (isDirty())
            refreshLayout();
        return maxWidth;
    }

    @Override
    public float maxHeight()
    {
        if (isDirty())
            refreshLayout();
        return maxHeight;
    }

    @Override
    public void layoutWidth(final float width)
    {
        layoutWidth = width;
    }

    @Override
    public void layoutHeight(final float height)
    {
        layoutHeight = height;
    }

    @Override
    public float layoutWidth()
    {
        return layoutWidth;
    }

    @Override
    public float layoutHeight()
    {
        return layoutHeight;
    }

    @Override
    public float effectiveWidth()
    {
        if (isDirty())
            refreshLayout();
        return effectiveWidth;
    }

    @Override
    public float effectiveHeight()
    {
        if (isDirty())
            refreshLayout();
        return effectiveHeight;
    }

    public void markDirty()
    {
        isDirty = true;
    }

    public boolean isDirty()
    {
        return isDirty;
    }

    public ILayoutBox first()
    {
        return first;
    }

    public void first(final ILayoutBox first)
    {
        this.first = first;
        markDirty();
    }

    public ILayoutBox second()
    {
        return second;
    }

    public void second(final ILayoutBox second)
    {
        this.second = second;
        markDirty();
    }

    public LayoutAlignment firstAlignment()
    {
        return firstAlignment;
    }

    public void firstAlignment(final LayoutAlignment firstAlignment)
    {
        this.firstAlignment = firstAlignment;
        markDirty();
    }

    public float firstPosX()
    {
        if (isDirty())
            refreshLayout();
        return firstPosX;
    }

    public float firstPosY()
    {
        if (isDirty())
            refreshLayout();
        return firstPosY;
    }

    public float secondPosX()
    {
        if (isDirty())
            refreshLayout();
        return secondPosX;
    }

    public float secondPosY()
    {
        if (isDirty())
            refreshLayout();
        return secondPosY;
    }

    public float firstWidth()
    {
        if (isDirty())
            refreshLayout();
        return firstWidth;
    }

    public float firstHeight()
    {
        if (isDirty())
            refreshLayout();
        return firstHeight;
    }

    public float secondWidth()
    {
        if (isDirty())
            refreshLayout();
        return secondWidth;
    }

    public float secondHeight()
    {
        if (isDirty())
            refreshLayout();
        return secondHeight;
    }
}
