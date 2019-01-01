package net.voxelindustry.brokkgui.wrapper.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.voxelindustry.brokkgui.demo.WorldGuiDemo;
import net.voxelindustry.brokkgui.wrapper.impl.WorldScreenWrapper;
import org.lwjgl.opengl.GL11;

public class WrapperEventHandler
{
    private ScaledResolution cachedResolution;

    public WrapperEventHandler()
    {
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {

    }

    @SubscribeEvent
    public void onGameOverlay(RenderGameOverlayEvent.Post e)
    {
        if (e.getResolution() != cachedResolution)
        {
            this.cachedResolution = e.getResolution();
            HUDManager.getInstance().getGlobalHud().setResolution(cachedResolution);
        }

        HUDManager.getInstance().getGlobalHud().render(e.getType());
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent e)
    {
        HUDManager.getInstance().getGlobalHud().onGuiChange(e.getGui());
    }

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent e)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        double playerX = player.prevPosX + (player.posX - player.prevPosX) * e.getPartialTicks();
        double playerY = player.prevPosY + (player.posY - player.prevPosY) * e.getPartialTicks();
        double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * e.getPartialTicks();

        GlStateManager.pushAttrib();
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.enableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.colorMask(true, true, true, true);

        GlStateManager.pushMatrix();
        GlStateManager.translate(-playerX, -playerY, -playerZ);

        BlockPos.getAllInBoxMutable(player.getPosition().add(-8, -4, -8), player.getPosition().add(8, 4, 8))
                .forEach(pos ->
                {
                    TileEntity tile = player.getEntityWorld().getTileEntity(pos);

                    if (!(tile instanceof TileEntityFurnace))
                        return;

                    BlockPos offset = tile.getPos().up();

                    GlStateManager.pushMatrix();
                    GlStateManager.translate(offset.getX() + 0.5, offset.getY() + 0.5, offset.getZ() + 0.5);
                    GlStateManager.rotate(-180, 0, 0, 1);

                    GlStateManager.scale(0.0625f / 16, 0.0625f / 16, 0.0625f / 16);
                    WorldScreenWrapper screen = new WorldScreenWrapper(new WorldGuiDemo(), 256, 256);

                    screen.render();

                    GlStateManager.popMatrix();
                });

        GlStateManager.disableBlend();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}
