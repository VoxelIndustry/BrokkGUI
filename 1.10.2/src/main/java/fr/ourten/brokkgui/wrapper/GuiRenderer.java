package fr.ourten.brokkgui.wrapper;

import org.lwjgl.opengl.GL11;

import fr.ourten.brokkgui.BrokkGuiPlatform;
import fr.ourten.brokkgui.internal.EGuiRenderMode;
import fr.ourten.brokkgui.internal.IGuiHelper;
import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.EGuiRenderPass;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiRenderer implements IGuiRenderer
{
    private final Tessellator  t;
    private final VertexBuffer vertexBuffer; // Added
    private final GuiHelper    helper;

    public GuiRenderer(final Tessellator t)
    {
        this.t = t;
        this.vertexBuffer = Tessellator.getInstance().getBuffer();
        this.helper = (GuiHelper) BrokkGuiPlatform.getInstance().getGuiHelper();
    }

    @Override
    public void beginDrawing(final EGuiRenderMode mode)
    {
        final int op = this.getGLOpFromMode(mode);
        this.t.getBuffer().begin(op, DefaultVertexFormats.POSITION);
    }

    @Override
    public void beginDrawingQuads()
    {
        this.vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
    }

    @Override
    public void endDrawing()
    {
        this.t.draw();
    }

    @Override
    public void addVertex(final double x, final double y, final double z)
    {
        this.vertexBuffer.pos(x, y, z).endVertex();
    }

    @Override
    public void addVertexWithUV(final double x, final double y, final double z, final double u, final double v)
    {
        this.vertexBuffer.pos(x, y, z).tex(u, v).endVertex();
    }

    @Override
    public IGuiHelper getHelper()
    {
        return this.helper;
    }

    @Override
    public void beginPass(final EGuiRenderPass pass)
    {

    }

    @Override
    public void endPass(final EGuiRenderPass pass)
    {

    }

    private int getGLOpFromMode(final EGuiRenderMode mode)
    {
        switch (mode)
        {
            case QUADS:
                return GL11.GL_QUADS;
            case LINE:
                return GL11.GL_LINE;
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