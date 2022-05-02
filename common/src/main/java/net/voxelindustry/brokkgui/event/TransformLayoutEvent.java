package net.voxelindustry.brokkgui.event;

import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;
import net.voxelindustry.hermod.IEventEmitter;

public class TransformLayoutEvent extends HermodEvent
{
    public static final EventType<TransformLayoutEvent> TYPE = new EventType<>("TRANSFORM_LAYOUT_EVENT");

    private final Transform child;

    public TransformLayoutEvent(Transform source, Transform child)
    {
        super(source);

        this.child = child;
    }

    @Override
    public TransformLayoutEvent copy(IEventEmitter source)
    {
        return new TransformLayoutEvent((Transform) source, child);
    }

    @Override
    public Transform getTarget()
    {
        return (Transform) super.getTarget();
    }

    public Transform child()
    {
        return child;
    }
}
