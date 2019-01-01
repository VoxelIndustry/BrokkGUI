package net.voxelindustry.brokkgui.wrapper.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.wrapper.impl.GlobalHudImpl;
import net.voxelindustry.brokkgui.wrapper.impl.HudWrapper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HUDManager
{
    private static final Map<ResourceLocation, Pair<Supplier<BrokkGuiScreen>, HUDConfig>> HUDs = new HashMap<>();

    public static void addHUD(ResourceLocation rsl, Supplier<BrokkGuiScreen> screenSupplier)
    {
        addHUD(rsl, screenSupplier, true);
    }

    public static void addHUD(ResourceLocation rsl, Supplier<BrokkGuiScreen> screenSupplier, boolean openByDefault)
    {
        addHUD(rsl, screenSupplier, openByDefault, RenderGameOverlayEvent.ElementType.EXPERIENCE);
    }

    public static void addHUD(ResourceLocation rsl, Supplier<BrokkGuiScreen> screenSupplier, boolean openByDefault,
                              RenderGameOverlayEvent.ElementType type)
    {
        addHUD(rsl, screenSupplier, HUDConfig.build().setOpenByDefault(openByDefault).setType(type).create());
    }

    public static void addHUD(ResourceLocation rsl, Supplier<BrokkGuiScreen> screenSupplier, HUDConfig config)
    {
        HUDs.put(rsl, Pair.of(screenSupplier, config));
    }

    public static void openHUD(ResourceLocation rsl)
    {
        getInstance().getGlobalHud().askOpen(getInstance().getGlobalHud().getHUD(rsl));
    }

    public static void closeHUD(ResourceLocation rsl)
    {
        getInstance().getGlobalHud().askClose(getInstance().getGlobalHud().getHUD(rsl));
    }

    private static HUDManager INSTANCE;

    public static HUDManager getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new HUDManager();
        return INSTANCE;
    }

    private GlobalHudImpl globalHud;

    private HUDManager()
    {

    }

    protected GlobalHudImpl getGlobalHud()
    {
        if (this.globalHud == null)
        {
            this.globalHud = new GlobalHudImpl(Minecraft.getMinecraft());

            HUDs.forEach((rsl, hudCreator) ->
            {
                HudWrapper hud = new HudWrapper(globalHud, hudCreator.getKey().get(), hudCreator.getValue());
                globalHud.attach(rsl, hud);
            });
        }
        return this.globalHud;
    }
}
