package org.yggard.brokkgui.internal;

import org.yggard.brokkgui.paint.RenderPass;

/**
 * @author Ourten 5 oct. 2016
 */
public interface IGuiRenderer
{
    void beginPass(RenderPass pass);

    void endPass(RenderPass pass);

    void beginDrawing(EGuiRenderMode mode, boolean texture);

    default void beginDrawingQuads(final boolean texture)
    {
        this.beginDrawing(EGuiRenderMode.QUADS, texture);
    }

    void endDrawing();

    void addVertex(final double x, final double y, final double z);

    void addVertexWithUV(final double x, final double y, final double z, final double u, final double v);

    IGuiHelper getHelper();
}