package net.voxelindustry.brokkgui.wrapper.hud;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.wrapper.impl.GlobalHudImpl;
import net.voxelindustry.brokkgui.wrapper.impl.HudImpl;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;

public class HUDManager
{
    private static final ListMultimap<RenderGameOverlayEvent.ElementType, Pair<Supplier<BrokkGuiScreen>, HUDConfig>> HUDs =
            MultimapBuilder.hashKeys().arrayListValues().build();

    private GlobalHudImpl globalHud;

    public static void addHUD(Supplier<BrokkGuiScreen> screenSupplier)
    {
        addHUD(screenSupplier, true);
    }

    public static void addHUD(Supplier<BrokkGuiScreen> screenSupplier, boolean openByDefault)
    {
        addHUD(screenSupplier, openByDefault, RenderGameOverlayEvent.ElementType.EXPERIENCE);
    }

    public static void addHUD(Supplier<BrokkGuiScreen> screenSupplier, boolean openByDefault,
                              RenderGameOverlayEvent.ElementType type)
    {
        addHUD(screenSupplier, HUDConfig.build().setOpenByDefault(openByDefault).setType(type).create());
    }

    public static void addHUD(Supplier<BrokkGuiScreen> screenSupplier, HUDConfig config)
    {
        HUDs.put(config.getType(), Pair.of(screenSupplier, config));
    }

    private GlobalHudImpl getGlobalHud()
    {
        if (this.globalHud == null)
        {
            this.globalHud = new GlobalHudImpl(Minecraft.getMinecraft());

            HUDs.forEach((hudCreator, config) ->
            {
                HudImpl hud = new HudImpl(globalHud, hudCreator.get());
                hud.setOpenByDefault(config);
                globalHud.attach(hud);
            });
        }
        return this.globalHud;
    }
}
