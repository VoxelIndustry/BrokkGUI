package org.yggard.brokkgui.paint;

public class RenderPass
{
    public static final RenderPass MAIN    = new RenderPass("main", 0);
    public static final RenderPass HOVER   = new RenderPass("hover", 1);
    public static final RenderPass SPECIAL = new RenderPass("special", 2);

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
}
