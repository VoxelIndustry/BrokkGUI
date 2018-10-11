package net.voxelindustry.brokkgui.data;

import java.util.Objects;

public class RectOffset
{
    public static RectOffset EMPTY = new RectOffset(0);

    private final float top;
    private final float left;
    private final float bottom;
    private final float right;

    public RectOffset(float top, float left, float bottom, float right)
    {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public RectOffset(float all)
    {
        this(all, all, all, all);
    }

    public float getTop()
    {
        return top;
    }

    public float getLeft()
    {
        return left;
    }

    public float getBottom()
    {
        return bottom;
    }

    public float getRight()
    {
        return right;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RectOffset that = (RectOffset) o;
        return Float.compare(that.top, top) == 0 &&
                Float.compare(that.left, left) == 0 &&
                Float.compare(that.bottom, bottom) == 0 &&
                Float.compare(that.right, right) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(top, left, bottom, right);
    }

    @Override
    public String toString()
    {
        return "RectOffset{" +
                "top=" + top +
                ", left=" + left +
                ", bottom=" + bottom +
                ", right=" + right +
                '}';
    }

    public static Builder build()
    {
        return new Builder();
    }

    public static class Builder
    {
        private float top    = -1;
        private float left   = -1;
        private float bottom = -1;
        private float right  = -1;

        public Builder top(float top)
        {
            this.top = top;
            return this;
        }

        public Builder left(float left)
        {
            this.left = left;
            return this;
        }

        public Builder bottom(float bottom)
        {
            this.bottom = bottom;
            return this;
        }

        public Builder right(float right)
        {
            this.right = right;
            return this;
        }

        public Builder all(float all)
        {
            if (this.top == -1)
                this.top = all;
            if (this.left == -1)
                this.left = all;
            if (this.bottom == -1)
                this.bottom = all;
            if (this.right == -1)
                this.right = all;
            return this;
        }

        public RectOffset create()
        {
            return new RectOffset(top == -1 ? 0 : top,
                    left == -1 ? 0 : left,
                    bottom == -1 ? 0 : bottom,
                    right == -1 ? 0 : right);
        }
    }
}
