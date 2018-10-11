package net.voxelindustry.brokkgui.internal;

import net.voxelindustry.brokkgui.paint.RenderPass;

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

    void beginMatrix();

    void endMatrix();

    void translateMatrix(float posX, float posY, float posZ);

    void rotateMatrix(float rotation, float x, float y, float z);

    void scaleMatrix(float scaleX, float scaleY, float scaleZ);
}