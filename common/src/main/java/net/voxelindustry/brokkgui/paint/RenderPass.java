package net.voxelindustry.brokkgui.paint;

public class RenderPass
{
    public static final RenderPass BACKGROUND = new RenderPass("background", 0);
    public static final RenderPass BORDER     = new RenderPass("border", 1);
    public static final RenderPass MAIN       = new RenderPass("main", 2);
    public static final RenderPass FOREGROUND = new RenderPass("foreground", 3);
    public static final RenderPass HOVER      = new RenderPass("hover", 4);

    public static final RenderPass[] VALUES = new RenderPass[]{BACKGROUND, BORDER, MAIN, FOREGROUND, HOVER};

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

    private final String name;
    private final int    priority;

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
