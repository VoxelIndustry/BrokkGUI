package org.yggard.brokkgui.wrapper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.data.Vector2i;
import org.yggard.brokkgui.internal.EGuiRenderMode;
import org.yggard.brokkgui.internal.IGuiHelper;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.Texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiHelper implements IGuiHelper
{
    private final RenderItem itemRender;
    private final Minecraft  mc;

    public GuiHelper()
    {
        this.mc = Minecraft.getMinecraft();
        this.itemRender = this.mc.getRenderItem();
    }

    @Override
    public final void bindTexture(final Texture texture)
    {
        this.mc.renderEngine.bindTexture(new ResourceLocation(texture.getResource()));
    }

    @Override
    public final void scissorBox(final float f, final float g, final float h, final float i)
    {
        final int width = (int) (h - f);
        final int height = (int) (i - g);
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final int factor = sr.getScaleFactor();
        final GuiScreen currentScreen = this.mc.currentScreen;
        if (currentScreen != null)
        {
            final int bottomY = (int) (currentScreen.height - i);
            GL11.glScissor((int) (f * factor), bottomY * factor, width * factor, height * factor);
        }
    }

    // TODO: Handle UP/DOWN/LEFT/RIGHTS alignments combinaisons
    @Override
    public final void drawString(final String string, final int x, final int y, final float zLevel, final Color color,
            final EAlignment alignment, final boolean shadow)
    {
        GlStateManager.enableBlend();
        // This is a very, very, very ugly hack but I've not seen any other way
        // to fix this minecraft bug. @see GlStateManager color(float colorRed,
        // float colorGreen, float colorBlue, float colorAlpha) L 675
        GlStateManager.color(0, 0, 0, 0);
        if (zLevel != 0)
        {
            GL11.glPushMatrix();
            GL11.glTranslated(0, 0, zLevel);
        }
        if (!shadow)
            this.mc.fontRendererObj.drawString(string,
                    x - (alignment.isHorizontalCentered() ? this.mc.fontRendererObj.getStringWidth(string) / 2 : 0),
                    y - (alignment.isVerticalCentered() ? this.mc.fontRendererObj.FONT_HEIGHT / 2 : 0), color.toInt());
        else
            this.mc.fontRendererObj.drawStringWithShadow(string,
                    x - (alignment.isHorizontalCentered() ? this.mc.fontRendererObj.getStringWidth(string) / 2 : 0),
                    y - (alignment.isVerticalCentered() ? this.mc.fontRendererObj.FONT_HEIGHT / 2 : 0), color.toInt());
        if (zLevel != 0)
            GL11.glPopMatrix();
        GlStateManager.disableBlend();
    }

    @Override
    public final void drawString(final String string, final double x, final double y, final float zLevel,
            final Color color, final boolean shadow)
    {
        this.drawString(string, (int) x, (int) y, zLevel, color, EAlignment.LEFT_UP, shadow);
    }

    @Override
    public final void drawString(final String string, final double x, final double y, final float zLevel,
            final Color color, final EAlignment alignment)
    {
        this.drawString(string, (int) x, (int) y, zLevel, color, alignment, true);
    }

    @Override
    public final void drawString(final String string, final double x, final double y, final float zLevel,
            final Color color)
    {
        this.drawString(string, (int) x, (int) y, zLevel, color, EAlignment.LEFT_UP, true);
    }

    @Override
    public final void drawTexturedModalRect(final IGuiRenderer renderer, final double xStart, final double yStart,
            final double uMin, final double vMin, final double uMax, final double vMax, final double width,
            final double height, final float zLevel)
    {
        renderer.beginDrawingQuads();
        renderer.addVertexWithUV(xStart + 0, yStart + height, zLevel, uMin, vMax);
        renderer.addVertexWithUV(xStart + width, yStart + height, zLevel, uMax, vMax);
        renderer.addVertexWithUV(xStart + width, yStart + 0, zLevel, uMax, vMin);
        renderer.addVertexWithUV(xStart + 0, yStart + 0, zLevel, uMin, vMin);
        renderer.endDrawing();
    }

    @Override
    public final void drawTexturedModalRect(final IGuiRenderer renderer, final double xStart, final double yStart,
            final double uMin, final double vMin, final double width, final double height, final float zLevel)
    {
        this.drawTexturedModalRect(renderer, xStart, yStart, uMin, vMin, 1, 1, width, height, zLevel);
    }

    @Override
    public final void drawColoredEmptyRect(final IGuiRenderer renderer, final float startX, final float startY,
            final float width, final float height, final float zLevel, final Color c, final float thin)
    {
        this.drawColoredRect(renderer, startX, startY, width - thin, thin, zLevel, c);
        this.drawColoredRect(renderer, startX + width - thin, startY, thin, height - thin, zLevel, c);
        this.drawColoredRect(renderer, startX + thin, startY + height - thin, width - thin, thin, zLevel, c);
        this.drawColoredRect(renderer, startX, startY + thin, thin, height - thin, zLevel, c);
    }

    @Override
    public final void drawColoredRect(final IGuiRenderer renderer, final float startX, final float startY,
            final float width, final float height, final float zLevel, final Color c)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        renderer.beginDrawingQuads();
        GL11.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        renderer.addVertex(startX, startY, zLevel);
        renderer.addVertex(startX, startY + height, zLevel);
        renderer.addVertex(startX + width, startY + height, zLevel);
        renderer.addVertex(startX + width, startY, zLevel);
        renderer.endDrawing();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public final void drawColoredEmptyCircle(final IGuiRenderer renderer, final float startX, final float startY,
            final float radius, final float zLevel, final Color c, final float thin)
    {
        if (thin > 0)
        {
            float x = radius;
            float y = 0;
            float err = 0;

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
            renderer.beginDrawing(EGuiRenderMode.POINTS);
            while (x >= y)
            {
                renderer.addVertex(startX + x, startY + y, zLevel);
                renderer.addVertex(startX + y, startY + x, zLevel);

                renderer.addVertex(startX - y, startY + x, zLevel);
                renderer.addVertex(startX - x, startY + y, zLevel);

                renderer.addVertex(startX - x, startY - y, zLevel);
                renderer.addVertex(startX - y, startY - x, zLevel);

                renderer.addVertex(startX + y, startY - x, zLevel);
                renderer.addVertex(startX + x, startY - y, zLevel);

                y += 1;
                err += 1 + 2 * y;
                if (2 * (err - x) + 1 > 0)
                {
                    x -= 1;
                    err += 1 - 2 * x;
                }
            }
            renderer.endDrawing();
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        }
        if (thin > 1)
            this.drawColoredEmptyCircle(renderer, startX + 1, startY + 1, radius - 1, zLevel, c, thin - 1);
    }

    @Override
    public final void drawColoredCircle(final IGuiRenderer renderer, final float startX, final float startY,
            final float radius, final float zLevel, final Color c)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        renderer.beginDrawing(EGuiRenderMode.POINTS);
        final float r2 = radius * radius;
        final float area = r2 * 4;
        final float rr = radius * 2;

        for (int i = 0; i < area; i++)
        {
            final float tx = i % rr - radius;
            final float ty = i / rr - radius;

            if (tx * tx + ty * ty <= r2)
                renderer.addVertex(startX + tx, startY + ty, zLevel);
        }
        renderer.endDrawing();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public final void drawColoredLine(final IGuiRenderer renderer, final float startX, final float startY,
            final float endX, final float endY, final float lineWeight, final float zLevel, final Color c)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());

        renderer.beginDrawing(EGuiRenderMode.LINE);
        GL11.glLineWidth(lineWeight);

        renderer.addVertex(startX, startY, zLevel);
        renderer.addVertex(endX, endY, zLevel);

        renderer.endDrawing();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public final void drawItemStack(final IGuiRenderer renderer, final float startX, final float startY,
            final float width, final float height, final float zLevel, final ItemStack stack)
    {
        this.drawItemStack(renderer, startX, startY, width, height, zLevel, stack, null);
    }

    public final void drawItemStack(final IGuiRenderer renderer, final float startX, final float startY,
            final float width, final float height, final float zLevel, final ItemStack stack,
            final String displayString)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(-9, -9, 0);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        final short short1 = 240;
        final short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1 / 1.0F, short2 / 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        final float oldItemZLevel = this.itemRender.zLevel;
        this.itemRender.zLevel = 200.0F;
        FontRenderer font = null;
        if (stack != null)
            font = stack.getItem().getFontRenderer(stack);
        if (font == null)
            font = this.mc.fontRendererObj;
        this.itemRender.renderItemAndEffectIntoGUI(stack, (int) startX, (int) startY);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, (int) startX, (int) startY, displayString);
        this.itemRender.zLevel = oldItemZLevel;
        GL11.glPopMatrix();
    }

    @Override
    public final void translateVecToScreenSpace(final Vector2i vec)
    {
        final GuiScreen currentScreen = this.mc.currentScreen;
        if (currentScreen != null)
        {
            vec.setX((int) (vec.getX() / ((float) currentScreen.width / this.mc.displayWidth)));
            vec.setY((int) ((currentScreen.height - vec.getY()) / ((float) currentScreen.height / this.mc.displayHeight)
                    - 1));
        }
    }

    @Override
    public final String trimStringToPixelWidth(final String str, final int pixelWidth)
    {
        return this.mc.fontRendererObj.trimStringToWidth(str, pixelWidth);
    }

    @Override
    public final float getStringWidth(final String str)
    {
        return this.mc.fontRendererObj.getStringWidth(str);
    }

    @Override
    public final float getStringHeight()
    {
        return this.mc.fontRendererObj.FONT_HEIGHT;
    }

    @Override
    public void beginScissor()
    {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void endScissor()
    {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}