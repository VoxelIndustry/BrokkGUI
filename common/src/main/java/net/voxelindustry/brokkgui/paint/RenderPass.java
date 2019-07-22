package net.voxelindustry.brokkgui.paint;

import java.util.Objects;

public class RenderPass
{
    public static final RenderPass BACKGROUND = new RenderPass("background", 0);
    public static final RenderPass MAIN       = new RenderPass("main", 1);
    public static final RenderPass FOREGROUND = new RenderPass("foreground", 2);
    public static final RenderPass HOVER      = new RenderPass("hover", 3);

    /**
     * Create a new RenderPass according to parameters.
     * Should only be used in wrappers and impl of the api.
     *
     * @param name     of the pass
     * @param priority of the pass in render
     * @return the created RenderPass
     */
    public static RenderPass create(String name, int priority)
    {
        return new RenderPass(name, priority);
    }

    private String name;
    private int    priority;

    private RenderPass(String name, int priority)
    {
        this.name = name;
        this.priority = priority;
    }

    public String getName()
    {
        return name;
    }

    public int getPriority()
    {
        return priority;
    }

    @Override
    public String toString()
    {
        return "RenderPass{" +
                "name='" + name + '\'' +
                ", priority=" + priority +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenderPass that = (RenderPass) o;
        return priority == that.priority &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, priority);
    }
}
