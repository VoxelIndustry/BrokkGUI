package net.voxelindustry.brokkgui.wrapper;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemDynamicRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.voxelindustry.brokkgui.paint.Color;

import java.util.List;
import java.util.Random;

public class GuiRenderItemHelper
{
    private static final Identifier RES_ITEM_GLINT = new Identifier("textures/misc/enchanted_item_glint.png");

    private       ItemRenderer    itemRender;
    private final MinecraftClient mc;
    private final Random          rand = new Random();

    GuiRenderItemHelper()
    {
        this.mc = MinecraftClient.getInstance();
        this.itemRender = this.mc.getItemRenderer();
    }

    public void renderItemStack(ItemStack stack, int x, int y, float zLevel, Color color)
    {
        GlStateManager.translatef(0.0F, 0.0F, 32.0F);

        BakedModel model = this.getRenderItem().getModels().getModel(stack);
        model = model.getItemPropertyOverrides().apply(model, stack, null, MinecraftClient.getInstance().player);

        GlStateManager.pushMatrix();
        this.mc.getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
        this.mc.getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).pushFilter(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlphaTest();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.setupGuiTransform(x, y, model.hasDepthInGui(), (int) zLevel + 50);

        if (!stack.isEmpty())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(-0.5F, -0.5F, -0.5F);

            if (model.isBuiltin())
            {
                GlStateManager.color4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                GlStateManager.enableRescaleNormal();

                ItemDynamicRenderer.INSTANCE.render(stack);
            }
            else
            {
                this.renderModel(model, color.toRGBAInt(), stack);

                if (stack.hasEnchantmentGlint())
                    this.renderEffect(model);
            }

            GlStateManager.popMatrix();
        }
        GlStateManager.disableAlphaTest();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
        this.mc.getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).popFilter();
    }

    private void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d, int zLevel)
    {
        GlStateManager.translatef((float) xPosition, (float) yPosition, 100.0F + zLevel);
        GlStateManager.translatef(8.0F, 8.0F, 0.0F);
        GlStateManager.scalef(1.0F, -1.0F, 1.0F);
        GlStateManager.scalef(16.0F, 16.0F, 16.0F);

        if (isGui3d)
            GlStateManager.enableLighting();
        else
            GlStateManager.disableLighting();
    }

    private void renderModel(BakedModel model, int color, ItemStack stack)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBufferBuilder();

        buffer.begin(7, VertexFormats.POSITION_COLOR_UV_NORMAL);

        for (Direction facing : Direction.values())
            this.renderQuads(buffer, model.getQuads(null, facing, rand), color, stack);

        this.renderQuads(buffer, model.getQuads(null, null, rand), color, stack);
        tessellator.draw();
    }

    private void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack)
    {
        boolean flag = color == -1 && !stack.isEmpty();
        int i = 0;

        for (int j = quads.size(); i < j; ++i)
        {
            BakedQuad bakedquad = quads.get(i);
            int quadColor = color;

            if (flag && bakedquad.hasColor())
            {
                quadColor = ((IAccessibleItemColorMap) this.mc).getItemColorMap().getRenderColor(stack,
                        bakedquad.getColorIndex());

                quadColor |= -16777216;
            }

            renderer.putVertexData(bakedquad.getVertexData());
            renderer.setQuadColor(quadColor);

            Vec3i var3 = bakedquad.getFace().getVector();
            renderer.postNormal((float) var3.getX(), (float) var3.getY(), (float) var3.getZ());
        }
    }

    private void renderEffect(BakedModel model)
    {
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
        this.mc.getTextureManager().bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scalef(8.0F, 8.0F, 8.0F);
        float f = (float) (SystemUtil.getMeasuringTimeMs() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translatef(f, 0.0F, 0.0F);
        GlStateManager.rotatef(-50.0F, 0.0F, 0.0F, 1.0F);
        this.renderModel(model, -8372020, ItemStack.EMPTY);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scalef(8.0F, 8.0F, 8.0F);
        float f1 = (float) (SystemUtil.getMeasuringTimeMs() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translatef(-f1, 0.0F, 0.0F);
        GlStateManager.rotatef(10.0F, 0.0F, 0.0F, 1.0F);
        this.renderModel(model, -8372020, ItemStack.EMPTY);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        this.mc.getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
    }

    private ItemRenderer getRenderItem()
    {
        if (this.itemRender == null)
            this.itemRender = this.mc.getItemRenderer();
        return this.itemRender;
    }
}
