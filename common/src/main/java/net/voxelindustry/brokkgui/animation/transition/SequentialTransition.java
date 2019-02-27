package net.voxelindustry.brokkgui.animation.transition;

import net.voxelindustry.brokkgui.animation.Animation;
import net.voxelindustry.brokkgui.animation.Interpolators;
import net.voxelindustry.brokkgui.component.GuiNode;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SequentialTransition extends Transition
{
    private List<Animation> animations;

    public SequentialTransition(GuiNode node, Animation... animations)
    {
        super(node, 0, TimeUnit.MILLISECONDS);

        this.setInterpolator(Interpolators.LINEAR);

        this.animations = Arrays.asList(animations);
        this.setDuration(this.animations.stream()
                .mapToLong(animation -> animation.getDuration() * animation.getMaxCycles()).sum());
        this.animations.forEach(animation -> animation.setParent(this));
    }

    @Override
    protected void apply(float interpolated)
    {
        Animation animation = null;

        float currentPart = 0;
        for (Animation anim : animations)
        {
            float part = ((float) anim.getDuration() * anim.getMaxCycles()) / this.getDuration();

            if (currentPart + part > interpolated)
            {
                animation = anim;
                break;
            }
            currentPart += part;
        }

        if (animation == null)
            return;

        animation.setCurrentProgress(interpolated - currentPart);
    }
}
