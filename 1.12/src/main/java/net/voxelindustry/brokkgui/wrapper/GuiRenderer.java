package net.voxelindustry.brokkgui.wrapper;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.internal.EGuiRenderMode;
import net.voxelindustry.brokkgui.internal.IGuiHelper;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import org.lwjgl.opengl.GL11;

public class GuiRenderer implements IGuiRenderer
{
    private final Tessellator t;
    private final GuiHelper   helper;

    public GuiRenderer(final Tessellator t)
    {
        this.t = t;
        this.helper = (GuiHelper) BrokkGuiPlatform.getInstance().getGuiHelper();
    }

    @Override
    public void beginDrawing(final EGuiRenderMode mode, final boolean texture)
    {
        final int op = this.getGLOpFromMode(mode);
        if (texture)
            this.t.getBuffer().begin(op, DefaultVertexFormats.POSITION_TEX);
        else
            this.t.getBuffer().begin(op, DefaultVertexFormats.POSITION);
    }

    @Override
    public void endDrawing()
    {
        this.t.draw();
    }

    @Override
    public void addVertex(final double x, final double y, final double z)
    {
        this.t.getBuffer().pos(x, y, z).endVertex();
    }

    @Override
    public void addVertexWithUV(final double x, final double y, final double z, final double u, final double v)
    {
        this.t.getBuffer().pos(x, y, z).tex(u, v).endVertex();
    }

    @Override
    public IGuiHelper getHelper()
    {
        return this.helper;
    }

    @Override
    public void beginMatrix()
    {
        GlStateManager.pushMatrix();
    }

    @Override
    public void endMatrix()
    {
        GlStateManager.popMatrix();
    }

    @Override
    public void translateMatrix(float posX, float posY, float posZ)
    {
        GlStateManager.translate(posX, posY, posZ);
    }

    @Override
    public void rotateMatrix(float rotation, float x, float y, float z)
    {
        GlStateManager.rotate(rotation, x, y, z);
    }

    @Override
    public void scaleMatrix(float scaleX, float scaleY, float scaleZ)
    {
        GlStateManager.scale(scaleX, scaleY, scaleZ);
    }

    @Override
    public void beginPass(final RenderPass pass)
    {

    }

    @Override
    public void endPass(final RenderPass pass)
    {

    }

    private int getGLOpFromMode(final EGuiRenderMode mode)
    {
        switch (mode)
        {
            case QUADS:
                return GL11.GL_QUADS;
            case LINE_STRIP:
                return GL11.GL_LINE_STRIP;
            case LINES:
                return GL11.GL_LINES;
            case POINTS:
                return GL11.GL_POINTS;
            case TRIANGLES:
                return GL11.GL_TRIANGLES;
            case TRIANGLES_STRIP:
                return GL11.GL_TRIANGLE_STRIP;
            default:
                return GL11.GL_QUADS;
        }
    }
}