package org.yggard.brokkgui.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.data.Vector2i;
import org.yggard.brokkgui.internal.EGuiRenderMode;
import org.yggard.brokkgui.internal.IGuiHelper;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.paint.Texture;

import java.util.ArrayList;
import java.util.List;

public class GuiHelper implements IGuiHelper
{
    public static RenderPass ITEM_MAIN  = RenderPass.create("item_main", 0);
    public static RenderPass ITEM_HOVER = RenderPass.create("item_hover", 1);

    private       RenderItem          itemRender;
    private final Minecraft           mc;
    private final GuiRenderItemHelper itemHelper;

    private double alphaMask;

    public GuiHelper()
    {
        this.mc = Minecraft.getMinecraft();
        this.itemRender = RenderItem.getInstance();

        this.itemHelper = new GuiRenderItemHelper();

        this.alphaMask = 1;
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
        final ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        final int factor = sr.getScaleFactor();
        final GuiScreen currentScreen = this.mc.currentScreen;
        if (currentScreen != null)
        {
            final int bottomY = (int) (currentScreen.height - i);
            GL11.glScissor((int) (f * factor), bottomY * factor, width * factor, height * factor);
        }
    }

    @Override
    public final void drawString(String string, int x, int y, float zLevel, Color textColor, Color shadowColor,
                                 EAlignment alignment)
    {
        GL11.glEnable(GL11.GL_BLEND);
        if (zLevel != 0)
        {
            GL11.glPushMatrix();
            GL11.glTranslated(0, 0, zLevel);
        }

        if (alignment.isHorizontalCentered())
            x -= this.mc.fontRenderer.getStringWidth(string) / 2;
        else if (alignment.isRight())
            x -= this.mc.fontRenderer.getStringWidth(string);

        if (alignment.isVerticalCentered())
            y -= this.mc.fontRenderer.FONT_HEIGHT / 2;
        else if (alignment.isDown())
            y -= this.mc.fontRenderer.FONT_HEIGHT;

        if (shadowColor.getAlpha() != 0)
            this.mc.fontRenderer.drawString(string, x + 1, y + 1, this.applyAlphaMask(shadowColor).toRGBAInt(), false);
        this.mc.fontRenderer.drawString(string, x, y, this.applyAlphaMask(textColor).toRGBAInt(), false);
        if (zLevel != 0)
            GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void drawString(String string, double x, double y, float zLevel, Color textColor, Color shadowColor)
    {
        this.drawString(string, (int) x, (int) y, zLevel, textColor, shadowColor, EAlignment.LEFT_UP);
    }

    @Override
    public void drawString(String string, double x, double y, float zLevel, Color textColor, EAlignment alignment)
    {
        this.drawString(string, (int) x, (int) y, zLevel, textColor, Color.ALPHA, alignment);
    }

    @Override
    public void drawString(String string, double x, double y, float zLevel, Color textColor)
    {
        this.drawString(string, (int) x, (int) y, zLevel, textColor, Color.ALPHA, EAlignment.LEFT_UP);
    }

    @Override
    public final void drawTexturedRect(final IGuiRenderer renderer, final float xStart, final float yStart,
                                       final float uMin, final float vMin, final float uMax, final float vMax, final
                                       float width,
                                       final float height, final float zLevel)
    {
        this.enableAlpha();
        GL11.glColor4f(1, 1, 1, (float) (1 * this.alphaMask));
        renderer.beginDrawingQuads(true);
        renderer.addVertexWithUV(Math.floor(xStart), Math.floor(yStart + height), zLevel, uMin, vMax);
        renderer.addVertexWithUV(Math.floor(xStart + width), Math.floor(yStart + height), zLevel, uMax, vMax);
        renderer.addVertexWithUV(Math.floor(xStart + width), Math.floor(yStart), zLevel, uMax, vMin);
        renderer.addVertexWithUV(Math.floor(xStart), Math.floor(yStart), zLevel, uMin, vMin);
        renderer.endDrawing();
        this.disableAlpha();
    }

    @Override
    public final void drawTexturedRect(final IGuiRenderer renderer, final float xStart, final float yStart,
                                       final float uMin, final float vMin, final float width, final float height,
                                       final float zLevel)
    {
        this.drawTexturedRect(renderer, xStart, yStart, uMin, vMin, 1, 1, width, height, zLevel);
    }

    @Override
    public final void drawColoredEmptyRect(final IGuiRenderer renderer, final float startX, final float startY,
                                           final float width, final float height, final float zLevel, final Color c,
                                           final float thin)
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
        this.enableAlpha();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), (float) (c.getAlpha() * alphaMask));
        renderer.beginDrawingQuads(false);
        renderer.addVertex(Math.floor(startX), Math.floor(startY), zLevel);
        renderer.addVertex(Math.floor(startX), Math.floor(startY + height), zLevel);
        renderer.addVertex(Math.floor(startX + width), Math.floor(startY + height), zLevel);
        renderer.addVertex(Math.floor(startX + width), Math.floor(startY), zLevel);
        renderer.endDrawing();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.disableAlpha();
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

            this.enableAlpha();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), (float) (c.getAlpha() * alphaMask));
            renderer.beginDrawing(EGuiRenderMode.POINTS, false);
            while (x >= y)
            {
                renderer.addVertex(Math.floor(startX + x), Math.floor(startY + y), zLevel);
                renderer.addVertex(Math.floor(startX + y), Math.floor(startY + x), zLevel);

                renderer.addVertex(Math.floor(startX - y), Math.floor(startY + x), zLevel);
                renderer.addVertex(Math.floor(startX - x), Math.floor(startY + y), zLevel);

                renderer.addVertex(Math.floor(startX - x), Math.floor(startY - y), zLevel);
                renderer.addVertex(Math.floor(startX - y), Math.floor(startY - x), zLevel);

                renderer.addVertex(Math.floor(startX + y), Math.floor(startY - x), zLevel);
                renderer.addVertex(Math.floor(startX + x), Math.floor(startY - y), zLevel);

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
            this.disableAlpha();
        }
        if (thin > 1)
            this.drawColoredEmptyCircle(renderer, startX + 1, startY + 1, radius - 1, zLevel, c, thin - 1);
    }

    @Override
    public final void drawColoredCircle(final IGuiRenderer renderer, final float startX, final float startY,
                                        final float radius, final float zLevel, final Color c)
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        this.enableAlpha();
        GL11.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), (float) (c.getAlpha() * alphaMask));
        renderer.beginDrawing(EGuiRenderMode.POINTS, false);
        final float r2 = radius * radius;
        final float area = r2 * 4;
        final float rr = radius * 2;

        for (int i = 0; i < area; i++)
        {
            final float tx = i % rr - radius;
            final float ty = i / rr - radius;

            if (tx * tx + ty * ty <= r2)
                renderer.addVertex(Math.floor(startX + tx), Math.floor(startY + ty), zLevel);
        }
        renderer.endDrawing();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.disableAlpha();
    }

    @Override
    public void drawTexturedCircle(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin,
                                   float uMax, float vMax, float radius, float zLevel)
    {
        this.enableAlpha();
        GL11.glColor4f(1, 1, 1, (float) (1 * alphaMask));
        renderer.beginDrawing(EGuiRenderMode.POINTS, true);
        final float r2 = radius * radius;
        final float area = r2 * 4;
        final float rr = radius * 2;

        for (int i = 0; i < area; i++)
        {
            final float tx = i % rr - radius;
            final float ty = i / rr - radius;

            if (tx * tx + ty * ty <= r2)
                renderer.addVertexWithUV(Math.floor(xStart + tx), Math.floor(yStart + ty), zLevel,
                        uMin + tx / rr * (uMax - uMin), vMin + ty / rr * (vMax - vMin));
        }
        renderer.endDrawing();
        this.disableAlpha();
    }

    @Override
    public void drawTexturedCircle(final IGuiRenderer renderer, final float xStart, final float yStart,
                                   final float uMin, final float vMin, final float radius, final float zLevel)
    {
        this.drawTexturedCircle(renderer, xStart, yStart, uMin, vMin, 1, 1, radius, zLevel);
    }

    @Override
    public final void drawColoredLine(final IGuiRenderer renderer, final float startX, final float startY,
                                      final float endX, final float endY, final float lineWeight, final float zLevel,
                                      final Color c)
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        this.enableAlpha();
        GL11.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), (float) (c.getAlpha() * alphaMask));

        renderer.beginDrawing(EGuiRenderMode.LINE, false);
        GL11.glLineWidth(lineWeight);

        renderer.addVertex(Math.floor(startX), Math.floor(startY), zLevel);
        renderer.addVertex(Math.floor(endX), Math.floor(endY), zLevel);

        renderer.endDrawing();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.disableAlpha();
    }

    public final void drawItemStack(final IGuiRenderer renderer, final float startX, final float startY,
                                    final float width, final float height, final float zLevel, final ItemStack stack,
                                    Color color)
    {
        this.drawItemStack(renderer, startX, startY, width, height, zLevel, stack, null, color);
    }

    public final void drawItemStack(final IGuiRenderer renderer, final float startX, final float startY,
                                    final float width, final float height, final float zLevel, final ItemStack stack,
                                    final String displayString, Color color)
    {
        GL11.glColor3f(1, 1, 1);

        if (stack != null)
        {
            final float scaleX = width / 18;
            final float scaleY = height / 18;
            GL11.glPushMatrix();
            GL11.glTranslated(-(startX * (scaleX - 1)) - 8 * scaleX, -(startY * (scaleY - 1)) - 8 * scaleY, 0);
            GL11.glScalef(scaleX, scaleY, 1);
            FontRenderer font = stack.getItem().getFontRenderer(stack);
            if (font == null)
                font = this.mc.fontRenderer;

            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, 32);

            GL11.glEnable(32826);
            GL11.glEnable(GL11.GL_LIGHTING);
            final short short1 = 240;
            final short short2 = 240;
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1 / 1.0F, short2 / 1.0F);

            this.itemHelper.renderItemStack(stack, (int) startX, (int) startY, zLevel);
            this.itemRender.renderItemOverlayIntoGUI(font, this.mc.renderEngine, stack, (int) startX, (int) startY,
                    displayString);
            GL11.glPopMatrix();
            GL11.glDisable(32826);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }

    public void drawItemStackTooltip(final IGuiRenderer renderer, final int mouseX, final int mouseY,
                                     final ItemStack stack)
    {
        List<String> list = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);

        for (int i = 0; i < list.size(); ++i)
        {
            if (i == 0)
                list.set(i, stack.getRarity().rarityColor + list.get(i));
            else
                list.set(i, EnumChatFormatting.GRAY + list.get(i));
        }

        if (!list.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            int tooltipTextWidth = 0;

            for (String textLine : list)
            {
                int textLineWidth = this.mc.fontRenderer.getStringWidth(textLine);

                if (textLineWidth > tooltipTextWidth)
                {
                    tooltipTextWidth = textLineWidth;
                }
            }

            boolean needsWrap = false;

            int titleLinesCount = 1;
            int tooltipX = mouseX + 12;
            if (tooltipX + tooltipTextWidth + 4 > this.mc.displayWidth)
            {
                tooltipX = mouseX - 16 - tooltipTextWidth;
                if (tooltipX < 4) // if the tooltip doesn't fit on the screen
                {
                    if (mouseX > this.mc.displayWidth / 2)
                    {
                        tooltipTextWidth = mouseX - 12 - 8;
                    }
                    else
                    {
                        tooltipTextWidth = this.mc.displayWidth - 16 - mouseX;
                    }
                    needsWrap = true;
                }
            }

            if (this.mc.displayWidth > 0 && tooltipTextWidth > this.mc.displayWidth)
            {
                tooltipTextWidth = this.mc.displayWidth;
                needsWrap = true;
            }

            if (needsWrap)
            {
                int wrappedTooltipWidth = 0;
                List<String> wrappedlist = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++)
                {
                    String textLine = list.get(i);
                    List<String> wrappedLine = this.mc.fontRenderer.listFormattedStringToWidth(textLine,
                            tooltipTextWidth);
                    if (i == 0)
                    {
                        titleLinesCount = wrappedLine.size();
                    }

                    for (String line : wrappedLine)
                    {
                        int lineWidth = this.mc.fontRenderer.getStringWidth(line);
                        if (lineWidth > wrappedTooltipWidth)
                        {
                            wrappedTooltipWidth = lineWidth;
                        }
                        wrappedlist.add(line);
                    }
                }
                tooltipTextWidth = wrappedTooltipWidth;
                list = wrappedlist;

                if (mouseX > this.mc.displayWidth / 2)
                {
                    tooltipX = mouseX - 16 - tooltipTextWidth;
                }
                else
                {
                    tooltipX = mouseX + 12;
                }
            }

            int tooltipY = mouseY - 12;
            int tooltipHeight = 8;

            if (list.size() > 1)
            {
                tooltipHeight += (list.size() - 1) * 10;
                if (list.size() > titleLinesCount)
                {
                    tooltipHeight += 2; // gap between title lines and next lines
                }
            }

            if (tooltipY < 4)
            {
                tooltipY = 4;
            }
            else if (tooltipY + tooltipHeight + 4 > this.mc.displayHeight)
            {
                tooltipY = this.mc.displayHeight - tooltipHeight - 4;
            }

            final int zLevel = 300;
            final int backgroundColor = 0xF0100010;
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3,
                    backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3,
                    tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY +
                    tooltipHeight + 3, backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3,
                    backgroundColor, backgroundColor);
            drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4,
                    tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            final int borderColorStart = 0x505000FF;
            final int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 -
                    1, borderColorStart, borderColorEnd);
            drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth +
                    3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1,
                    borderColorStart, borderColorStart);
            drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3,
                    tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

            for (int lineNumber = 0; lineNumber < list.size(); ++lineNumber)
            {
                String line = list.get(lineNumber);
                this.mc.fontRenderer.drawStringWithShadow(line, tooltipX, tooltipY, -1);

                if (lineNumber + 1 == titleLinesCount)
                {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    public void drawGradientRect(int zLevel, int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float startAlpha = (float) (startColor >> 24 & 255) / 255.0F;
        float startRed = (float) (startColor >> 16 & 255) / 255.0F;
        float startGreen = (float) (startColor >> 8 & 255) / 255.0F;
        float startBlue = (float) (startColor & 255) / 255.0F;
        float endAlpha = (float) (endColor >> 24 & 255) / 255.0F;
        float endRed = (float) (endColor >> 16 & 255) / 255.0F;
        float endGreen = (float) (endColor >> 8 & 255) / 255.0F;
        float endBlue = (float) (endColor & 255) / 255.0F;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(startRed, startGreen, startBlue, startAlpha);
        tessellator.addVertex(right, top, zLevel);
        tessellator.addVertex(left, top, zLevel);
        tessellator.setColorRGBA_F(endRed, endGreen, endBlue, endAlpha);
        tessellator.addVertex(left, bottom, zLevel);
        tessellator.addVertex(right, bottom, zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
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
        return this.mc.fontRenderer.trimStringToWidth(str, pixelWidth);
    }

    @Override
    public final float getStringWidth(final String str)
    {
        return this.mc.fontRenderer.getStringWidth(str);
    }

    @Override
    public final float getStringHeight()
    {
        return this.mc.fontRenderer.FONT_HEIGHT;
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

    @Override
    public void startAlphaMask(double opacity)
    {
        this.alphaMask = opacity;
        this.enableAlpha();
    }

    @Override
    public void closeAlphaMask()
    {
        this.alphaMask = 1;
        this.disableAlpha();
    }

    private Color applyAlphaMask(Color src)
    {
        if (this.alphaMask == 1)
            return src;
        Color result = Color.from(src);

        result.setAlpha((float) (src.getAlpha() * this.alphaMask));
        return result;
    }

    public void enableAlpha()
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
    }

    public void disableAlpha()
    {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }
}