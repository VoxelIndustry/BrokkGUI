package net.voxelindustry.brokkgui.event;

import net.voxelindustry.hermod.HermodEvent;
import net.voxelindustry.hermod.IEventEmitter;

public class AnimationFinishEvent extends HermodEvent
{
    public AnimationFinishEvent(IEventEmitter source)
    {
        super(source);
    }

    @Override
    public AnimationFinishEvent copy(IEventEmitter source)
    {
        return new AnimationFinishEvent(source);
    }
}
