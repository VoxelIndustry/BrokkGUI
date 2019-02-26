package net.voxelindustry.brokkgui.wrapper;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.voxelindustry.brokkgui.animation.ITickSender;
import net.voxelindustry.brokkgui.animation.ITicking;

import java.util.ArrayList;
import java.util.List;

public class BrokkGuiTickSender implements ITickSender
{
    private List<ITicking> tickings = new ArrayList<>();
    private List<ITicking> toEvict  = new ArrayList<>();

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END)
            return;

        long millis = System.currentTimeMillis();

        if (!toEvict.isEmpty())
        {
            tickings.removeAll(toEvict);
            toEvict.clear();
        }
        for (ITicking ticking : tickings)
            ticking.tick(millis);
    }

    @Override
    public void addTicking(ITicking toTick)
    {
        this.tickings.add(toTick);
    }

    @Override
    public void removeTicking(ITicking toTick)
    {
        this.toEvict.add(toTick);
    }
}
