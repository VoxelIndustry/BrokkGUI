package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.animation.Animation;
import net.voxelindustry.brokkgui.animation.Interpolators;
import net.voxelindustry.brokkgui.component.GuiElement;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SequentialTransition extends Transition
{
    private List<Animation> animations;

    private Animation current;

    public SequentialTransition(GuiElement node, Animation... animations)
    {
        super(node, 0, TimeUnit.MILLISECONDS);

        setInterpolator(Interpolators.LINEAR);

        this.animations = Arrays.asList(animations);
        duration(this.animations.stream()
                .mapToLong(animation -> animation.duration() * animation.maxCycles()).sum());
        this.animations.forEach(animation -> animation.parent(this));
    }

    @Override
    protected void apply(float interpolated)
    {
        Animation animation = null;

        float currentPart = 0;
        for (Animation anim : animations)
        {
            float part = ((float) anim.duration() * anim.maxCycles()) / duration();

            if (currentPart + part > interpolated)
            {
                animation = anim;
                break;
            }
            currentPart += part;
        }

        if (animation == null)
            return;

        if (current != animation)
        {
            if (current != null)
                current.complete();

            current = animation;
            current.restart();
        }

        animation.setCurrentProgress(interpolated - currentPart);
    }
}
