package net.voxelindustry.brokkgui.wrapper;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.util.Identifier;
import net.voxelindustry.brokkgui.data.Vector2i;
import net.voxelindustry.brokkgui.internal.EGuiRenderMode;
import net.voxelindustry.brokkgui.internal.IGuiHelper;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.paint.Texture;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.stream.Collectors;

public class GuiHelper implements IGuiHelper
{
    public static RenderPass ITEM_MAIN  = RenderPass.create("item_main", 1);
    public static RenderPass ITEM_HOVER = RenderPass.create("item_hover", 3);

    private ItemRenderer        itemRender;
    private MinecraftClient     mc;
    private GuiRenderItemHelper itemHelper;

    private double alphaMask;

    public GuiHelper()
    {
        this.mc = MinecraftClient.getInstance();
        this.itemRender = this.mc.getItemRenderer();

        this.itemHelper = new GuiRenderItemHelper();

        this.alphaMask = 1;
    }

    @Override
    public void bindTexture(Texture texture)
    {
        this.mc.getTextureManager().bindTexture(new Identifier(texture.getResource()));
    }

    @Override
    public void scissorBox(float f, float g, float h, float i)
    {
        int width = (int) (h - f);
        int height = (int) (i - g);
        double factor = this.mc.window.getScaleFactor();
        Screen currentScreen = this.mc.currentScreen;
        if (currentScreen != null)
        {
            int bottomY = (int) (currentScreen.height - i);
            GL11.glScissor((int) (f * factor), (int) (bottomY * factor), (int) (width * factor),
                    (int) (height * factor));
        }
    }

    @Override
    public void drawString(String string, float x, float y, float zLevel, Color textColor, Color shadowColor)
    {
        GlStateManager.enableBlend();
        GlStateManager.clearCurrentColor();
        if (zLevel != 0)
        {
            GL11.glPushMatrix();
            GL11.glTranslated(0, 0, zLevel);
        }

        if (shadowColor.getAlpha() != 0)
            this.mc.textRenderer.draw(string, x + 1, y + 1, this.applyAlphaMask(shadowColor).toRGBAInt());
        this.mc.textRenderer.draw(string, x, y, this.applyAlphaMask(textColor).toRGBAInt());
        if (zLevel != 0)
            GL11.glPopMatrix();
        GlStateManager.clearCurrentColor();
        GlStateManager.disableBlend();
    }

    @Override
    public void drawString(String string, float x, float y, float zLevel, Color textColor)
    {
        this.drawString(string, x, y, zLevel, textColor, Color.ALPHA);
    }

    @Override
    public void drawTexturedRect(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin,
                                 float uMax, float vMax, float width, float height, float zLevel)
    {
        this.enableAlpha();
        GlStateManager.color4f(1, 1, 1, (float) (1 * this.alphaMask));
        renderer.beginDrawingQuads(true);
        renderer.addVertexWithUV(xStart, yStart + height, zLevel, uMin, vMax);
        renderer.addVertexWithUV(xStart + width, yStart + height, zLevel, uMax, vMax);
        renderer.addVertexWithUV(xStart + width, yStart, zLevel, uMax, vMin);
        renderer.addVertexWithUV(xStart, yStart, zLevel, uMin, vMin);
        renderer.endDrawing();
        this.disableAlpha();
    }

    @Override
    public void drawTexturedRect(IGuiRenderer renderer, float xStart, float yStart, float uMin, float vMin,
                                 float width, float height, float zLevel)
    {
        this.drawTexturedRect(renderer, xStart, yStart, uMin, vMin, 1, 1, width, height, zLevel);
    }

    @Override
    public void drawColoredEmptyRect(IGuiRenderer renderer, float startX, float startY, float width, float height,
                                     float zLevel, Color c, float thin)
    {
        this.drawColoredRect(renderer, startX, startY, width - thin, thin, zLevel, c);
        this.drawColoredRect(renderer, startX + width - thin, startY, thin, height - thin, zLevel, c);
        this.drawColoredRect(renderer, startX + thin, startY + height - thin, width - thin, thin, zLevel, c);
        this.drawColoredRect(renderer, startX, startY + thin, thin, height - thin, zLevel, c);
    }

    @Override
    public void drawColoredRect(IGuiRenderer renderer, float startX, float startY,
                                float width, float height, float zLevel, Color color)
    {
        this.enableAlpha();
        GlStateManager.disableTexture();
        GlStateManager.color4f(color.getRed(), color.getGreen(), color.getBlue(),
                (float) (color.getAlpha() * alphaMask));
        renderer.beginDrawingQuads(false);
        renderer.addVertex(startX, startY, zLevel);
        renderer.addVertex(startX, startY + height, zLevel);
        renderer.addVertex(startX + width, startY + height, zLevel);
        renderer.addVertex(startX + width, startY, zLevel);
        renderer.endDrawing();
        GlStateManager.clearCurrentColor();
        GlStateManager.enableTexture();
        this.disableAlpha();
    }

    @Override
    public void drawColoredEmptyCircle(IGuiRenderer renderer, float startX, float startY, float radius, float zLevel,
                                       Color color, float thin)
    {
        if (thin > 0)
        {
            float x = radius;
            float y = 0;
            float err = 0;

            this.enableAlpha();
            GlStateManager.disableTexture();
            GlStateManager.color4f(color.getRed(), color.getGreen(), color.getBlue(), (float) (color.getAlpha() *
                    alphaMask));
            renderer.beginDrawing(EGuiRenderMode.POINTS, false);
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
            GlStateManager.clearCurrentColor();
            GlStateManager.enableTexture();
            this.disableAlpha();
        }
        if (thin > 1)
            this.drawColoredEmptyCircle(renderer, startX + 1, startY + 1, radius - 1, zLevel, color, thin - 1);
    }

    @Override
    public void drawColoredCircle(IGuiRenderer renderer, float startX, float startY,
                                  float radius, float zLevel, Color c)
    {
        GlStateManager.disableTexture();
        this.enableAlpha();
        GlStateManager.color4f(c.getRed(), c.getGreen(), c.getBlue(), (float) (c.getAlpha() * alphaMask));
        renderer.beginDrawing(EGuiRenderMode.POINTS, false);
        float r2 = radius * radius;
        float area = r2 * 4;
        float rr = radius * 2;

        for (int i = 0; i < area; i++)
        {
            float tx = i % rr - radius;
            float ty = i / rr - radius;

            if (tx * tx + ty * ty <= r2)
                renderer.addVertex(startX + tx, startY + ty, zLevel);
        }
        renderer.endDrawing();
        GlStateManager.clearCurrentColor();
        GlStateManager.enableTexture();
        this.disableAlpha();
    }

    @Override
    public void drawTexturedCircle(IGuiRenderer renderer, float xStart, float yStart,
                                   float uMin, float vMin, float uMax, float vMax,
                                   float radius, float zLevel)
    {
        this.enableAlpha();
        GlStateManager.color4f(1, 1, 1, (float) alphaMask);
        renderer.beginDrawing(EGuiRenderMode.POINTS, true);
        float r2 = radius * radius;
        float area = r2 * 4;
        float rr = radius * 2;

        for (int i = 0; i < area; i++)
        {
            float tx = i % rr - radius;
            float ty = i / rr - radius;

            if (tx * tx + ty * ty <= r2)
                renderer.addVertexWithUV(xStart + tx, yStart + ty, zLevel,
                        uMin + tx / rr * (uMax - uMin), vMin + ty / rr * (vMax - vMin));
        }
        renderer.endDrawing();
        this.disableAlpha();
    }

    @Override
    public void drawTexturedCircle(IGuiRenderer renderer, float xStart, float yStart,
                                   float uMin, float vMin, float radius, float zLevel)
    {
        this.drawTexturedCircle(renderer, xStart, yStart, uMin, vMin, 1, 1, radius, zLevel);
    }

    @Override
    public void drawColoredLine(IGuiRenderer renderer, float startX, float startY,
                                float endX, float endY, float lineWeight, float zLevel,
                                Color c)
    {
        GlStateManager.disableTexture();
        this.enableAlpha();
        GlStateManager.color4f(c.getRed(), c.getGreen(), c.getBlue(), (float) (c.getAlpha() * alphaMask));

        renderer.beginDrawing(EGuiRenderMode.LINES, false);
        GL11.glLineWidth(lineWeight);

        renderer.addVertex(startX, startY, zLevel);
        renderer.addVertex(endX, endY, zLevel);

        renderer.endDrawing();
        GlStateManager.clearCurrentColor();
        GlStateManager.enableTexture();
        this.disableAlpha();
    }

    public void drawItemStack(IGuiRenderer renderer, float startX, float startY,
                              float width, float height, float zLevel, ItemStack stack,
                              Color color)
    {
        this.drawItemStack(renderer, startX, startY, width, height, zLevel, stack, null, color);
    }

    public void drawItemStack(IGuiRenderer renderer, float startX, float startY,
                              float width, float height, float zLevel, ItemStack stack,
                              String displayString, Color color)
    {
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);

        if (!stack.isEmpty())
        {
            float scaleX = width / 18;
            float scaleY = height / 18;
            GlStateManager.pushMatrix();
            GlStateManager.translatef(-(startX * (scaleX - 1)) - 8 * scaleX, -(startY * (scaleY - 1)) - 8 * scaleY, 0);
            GlStateManager.scalef(scaleX, scaleY, 1);
            TextRenderer font = this.mc.textRenderer;

            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, 0.0F, 32.0F);

            GlStateManager.enableRescaleNormal();
            GlStateManager.enableLighting();
            short short1 = 240;
            short short2 = 240;

            GuiLighting.enableForItems();

            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, short1 / 1.0F, short2 / 1.0F);

            this.itemHelper.renderItemStack(stack, (int) startX, (int) startY, zLevel, applyAlphaMask(color));
            this.getRenderItem().renderGuiItemOverlay(font, stack, (int) startX, (int) startY,
                    displayString);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();

            GuiLighting.enableForItems();
            GlStateManager.popMatrix();
        }
    }

    public void drawItemStackTooltip(IGuiRenderer renderer, int mouseX, int mouseY,
                                     ItemStack stack)
    {
        List<String> tooltipText = stack.getTooltipText(this.mc.player, this.mc.options.advancedItemTooltips ?
                TooltipContext.Default.ADVANCED : TooltipContext.Default.NORMAL).stream()
                .map(TextComponent::getFormattedText).collect(Collectors.toList());

        for (int i = 0; i < tooltipText.size(); ++i)
        {
            if (i == 0)
                tooltipText.set(i, stack.getRarity().formatting.getFormatName() + tooltipText.get(i));
            else
                tooltipText.set(i, TextFormat.GRAY + tooltipText.get(i));
        }

        this.drawTooltip(renderer, mouseX, mouseY, tooltipText);
    }

    public void drawTooltip(IGuiRenderer renderer, int x, int y, List<String> tooltipText)
    {
        if (tooltipText.isEmpty())
            return;

        GlStateManager.disableRescaleNormal();
        GuiLighting.disable();
        GlStateManager.disableLighting();
        GlStateManager.disableDepthTest();
        int maxLineWidth = tooltipText.stream().mapToInt(mc.textRenderer::getStringWidth).sum();

        int xStart = x + 12;
        int yStart = y - 12;
        int var8 = 8;
        if (tooltipText.size() > 1)
        {
            var8 += 2 + (tooltipText.size() - 1) * 10;
        }

        if (xStart + maxLineWidth > this.mc.window.getWidth())
        {
            xStart -= 28 + maxLineWidth;
        }

        if (yStart + var8 + 6 > this.mc.window.getHeight())
        {
            yStart = this.mc.window.getHeight() - var8 - 6;
        }

        int var9 = -267386864;
        this.drawGradientRect(xStart - 3, yStart - 4, xStart + maxLineWidth + 3, yStart - 3, 300, -267386864,
                -267386864);
        this.drawGradientRect(xStart - 3, yStart + var8 + 3, xStart + maxLineWidth + 3, yStart + var8 + 4, 300,
                -267386864,
                -267386864);
        this.drawGradientRect(xStart - 3, yStart - 3, xStart + maxLineWidth + 3, yStart + var8 + 3, 300, -267386864,
                -267386864);
        this.drawGradientRect(xStart - 4, yStart - 3, xStart - 3, yStart + var8 + 3, 300, -267386864, -267386864);
        this.drawGradientRect(xStart + maxLineWidth + 3, yStart - 3, xStart + maxLineWidth + 4, yStart + var8 + 3,
                300, -267386864, -267386864);
        int var10 = 1347420415;
        int var11 = 1344798847;
        this.drawGradientRect(xStart - 3, yStart - 3 + 1, xStart - 3 + 1, yStart + var8 + 3 - 1, 300, 1347420415,
                1344798847);
        this.drawGradientRect(xStart + maxLineWidth + 2, yStart - 3 + 1, xStart + maxLineWidth + 3,
                yStart + var8 + 3 - 1
                , 300, 1347420415, 1344798847);
        this.drawGradientRect(xStart - 3, yStart - 3, xStart + maxLineWidth + 3, yStart - 3 + 1, 300, 1347420415,
                1347420415);
        this.drawGradientRect(xStart - 3, yStart + var8 + 2, xStart + maxLineWidth + 3, yStart + var8 + 3, 300,
                1344798847,
                1344798847);

        for (int index = 0; index < tooltipText.size(); ++index)
        {
            String line = tooltipText.get(index);
            this.mc.textRenderer.drawWithShadow(line, (float) xStart, (float) yStart, -1);
            if (index == 0)
                yStart += 2;
            yStart += 10;
        }

        GlStateManager.enableLighting();
        GlStateManager.enableDepthTest();
        GuiLighting.enableForItems();
        GlStateManager.enableRescaleNormal();
    }

    public void drawGradientRect(int xStart, int yStart, int xEnd, int yEnd, int zLevel, int fromRGBA, int toRGBA)
    {
        float var7 = (float) (fromRGBA >> 24 & 255) / 255.0F;
        float var8 = (float) (fromRGBA >> 16 & 255) / 255.0F;
        float var9 = (float) (fromRGBA >> 8 & 255) / 255.0F;
        float var10 = (float) (fromRGBA & 255) / 255.0F;
        float var11 = (float) (toRGBA >> 24 & 255) / 255.0F;
        float var12 = (float) (toRGBA >> 16 & 255) / 255.0F;
        float var13 = (float) (toRGBA >> 8 & 255) / 255.0F;
        float var14 = (float) (toRGBA & 255) / 255.0F;
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator var15 = Tessellator.getInstance();
        BufferBuilder var16 = var15.getBufferBuilder();
        var16.begin(7, VertexFormats.POSITION_COLOR);
        var16.vertex(xEnd, yStart, zLevel).color(var8, var9, var10, var7).next();
        var16.vertex(xStart, yStart, zLevel).color(var8, var9, var10, var7).next();
        var16.vertex(xStart, yEnd, zLevel).color(var12, var13, var14, var11).next();
        var16.vertex(xEnd, yEnd, zLevel).color(var12, var13, var14, var11).next();
        var15.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableTexture();
    }

    public void drawFluid(IGuiRenderer renderer, float startX, float startY, float width, float height,
                          float zLevel, Fluid fluid, boolean flowing)
    {
        throw new UnsupportedOperationException("Fluid rendering is not available yet!");
    }

    @Override
    public void translateVecToScreenSpace(Vector2i vec)
    {
        Screen currentScreen = this.mc.currentScreen;
        if (currentScreen != null)
        {
            vec.setX((int) (vec.getX() / ((float) currentScreen.width / this.mc.window.getWidth())));
            vec.setY((int) ((currentScreen.height - vec.getY()) / ((float) currentScreen.height / this.mc.window.getHeight())
                    - 1));
        }
    }

    @Override
    public String trimStringToPixelWidth(String str, int pixelWidth)
    {
        return this.mc.textRenderer.wrapStringToWidth(str, pixelWidth);
    }

    @Override
    public float getStringWidth(String str)
    {
        return this.mc.textRenderer.getStringWidth(str);
    }

    @Override
    public float getStringHeight()
    {
        return this.mc.textRenderer.fontHeight;
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

    private void enableAlpha()
    {
        GlStateManager.enableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableDepthTest();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
    }

    private void disableAlpha()
    {
        GlStateManager.disableDepthTest();
        GlStateManager.disableAlphaTest();
        GlStateManager.disableBlend();
    }

    private ItemRenderer getRenderItem()
    {
        if (this.itemRender == null)
            this.itemRender = this.mc.getItemRenderer();
        return this.itemRender;
    }
}
