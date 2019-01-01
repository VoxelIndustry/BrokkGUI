package net.voxelindustry.brokkgui.wrapper.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.wrapper.GuiRenderer;
import net.voxelindustry.brokkgui.wrapper.hud.HUDGuiPolicy;

import java.util.ArrayList;
import java.util.List;

public class GlobalHudImpl
{
    private final Minecraft   mc;
    private final GuiRenderer renderer;

    private final BiMap<ResourceLocation, HudWrapper> openHUDs, closedHUDs;

    private ScaledResolution resolution;

    public GlobalHudImpl(Minecraft mc)
    {
        this.mc = mc;
        this.renderer = new GuiRenderer(Tessellator.getInstance());

        this.openHUDs = HashBiMap.create();
        this.closedHUDs = HashBiMap.create();
    }

    public int getScreenWidth()
    {
        if (getResolution() != null)
            return getResolution().getScaledWidth();
        return 0;
    }

    public int getScreenHeight()
    {
        if (getResolution() != null)
            return getResolution().getScaledHeight();
        return 0;
    }

    public IGuiRenderer getRenderer()
    {
        return this.renderer;
    }

    public ScaledResolution getResolution()
    {
        return resolution;
    }

    public void setResolution(ScaledResolution resolution)
    {
        this.resolution = resolution;

        openHUDs.forEach((rsl, hud) ->
        {
            hud.getScreen().getScreenWidthProperty().setValue(getScreenWidth());
            hud.getScreen().getScreenHeightProperty().setValue(getScreenHeight());
        });
    }

    public void askOpen(HudWrapper impl)
    {
        if (!closedHUDs.containsValue(impl))
            return;

        ResourceLocation rsl = this.closedHUDs.inverse().get(impl);

        this.closedHUDs.remove(rsl);
        this.openHUDs.put(rsl, impl);
        impl.getScreen().getScreenWidthProperty().setValue(getScreenWidth());
        impl.getScreen().getScreenHeightProperty().setValue(getScreenHeight());
    }

    public void askClose(HudWrapper impl)
    {
        ResourceLocation rsl = this.openHUDs.inverse().get(impl);

        this.openHUDs.remove(rsl);
        this.closedHUDs.put(rsl, impl);
    }

    public void attach(ResourceLocation rsl, HudWrapper impl)
    {
        if (impl.isOpenByDefault())
            this.openHUDs.put(rsl, impl);
        else
            this.closedHUDs.put(rsl, impl);
    }

    public void render(RenderGameOverlayEvent.ElementType type)
    {
        this.openHUDs.forEach((rsl, hud) ->
        {
            if (hud.getRenderType() != type)
                return;

            hud.render();
        });
    }

    public void onGuiChange(GuiScreen gui)
    {
        if (gui == null)
        {
            this.openHUDs.putAll(this.closedHUDs);
            this.closedHUDs.clear();
        }
        else
        {
            List<ResourceLocation> removed = new ArrayList<>();
            this.openHUDs.forEach((rsl, hud) ->
            {
                if (hud.getConfig().getHudGuiPolicy() == HUDGuiPolicy.CLOSE)
                    removed.add(rsl);
                else if (hud.getConfig().getHudGuiPolicy() == HUDGuiPolicy.HIDE)
                {
                    removed.add(rsl);
                    closedHUDs.put(rsl, hud);
                }
            });

            removed.forEach(openHUDs::remove);
        }
    }

    public HudWrapper getHUD(ResourceLocation rsl)
    {
        return this.openHUDs.getOrDefault(rsl, this.closedHUDs.get(rsl));
    }
}
