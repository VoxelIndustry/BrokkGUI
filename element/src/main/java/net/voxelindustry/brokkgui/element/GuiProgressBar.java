package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.behavior.GuiBehaviorBase;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.skin.GuiProgressBarSkin;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

/**
 * @author Phenix246
 */
public class GuiProgressBar extends GuiLabeled
{
    private final BaseProperty<Float>    progressBarProgressProperty;
    private final BaseProperty<RectSide> progressBarProgressDirection;

    public GuiProgressBar(final String text, final float progressRatio)
    {
        super("progressbar", text);
        this.progressBarProgressProperty = new BaseProperty<>(0f, "progressProperty");
        this.progressBarProgressDirection = new BaseProperty<>(RectSide.RIGHT, "progressDirectionProperty");

        this.progressBarProgressProperty.setChecker((old, set) ->
        {
            if (set < 0 || set > 1)
                return old;
            return set;
        });

        this.progressBarProgressProperty.setValue(progressRatio);
        this.getTextAlignmentProperty().setValue(RectAlignment.MIDDLE_CENTER);
    }

    public GuiProgressBar(final String text)
    {
        this(text, 0);
    }

    public GuiProgressBar(final float progressRatio)
    {
        this("", progressRatio);
    }

    public GuiProgressBar()
    {
        this("");
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiProgressBarSkin<>(this, new GuiBehaviorBase<>(this));
    }

    public BaseProperty<Float> getProgressProperty()
    {
        return this.progressBarProgressProperty;
    }

    public BaseProperty<RectSide> getProgressDirectionProperty()
    {
        return this.progressBarProgressDirection;
    }

    public float getProgress()
    {
        return this.getProgressProperty().getValue();
    }

    public void setProgress(float progress)
    {
        this.getProgressProperty().setValue(progress);
    }

    public RectSide getProgressDirection()
    {
        return this.getProgressDirectionProperty().getValue();
    }

    public void setProgressDirection(RectSide direction)
    {
        this.getProgressDirectionProperty().setValue(direction);
    }

}
