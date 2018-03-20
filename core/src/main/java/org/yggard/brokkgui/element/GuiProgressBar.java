package org.yggard.brokkgui.element;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.behavior.GuiBehaviorBase;
import org.yggard.brokkgui.control.GuiLabeled;
import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.data.EHAlignment;
import org.yggard.brokkgui.skin.GuiProgressBarSkin;
import org.yggard.brokkgui.skin.GuiSkinBase;

/**
 * @author Phenix246
 */
public class GuiProgressBar extends GuiLabeled
{
    private final BaseProperty<Float>       progressBarProgressProperty;
    private final BaseProperty<EHAlignment> progressBarProgressDirection;

    public GuiProgressBar(final String text, final float progressRatio)
    {
        super("progressbar", text);
        this.progressBarProgressProperty = new BaseProperty<>(0f, "progressProperty");
        this.progressBarProgressDirection = new BaseProperty<>(EHAlignment.RIGHT, "progressDirectionProperty");

        this.progressBarProgressProperty.setChecker((old, set) ->
        {
            if (set < 0 || set > 1)
                return old;
            return set;
        });

        this.progressBarProgressProperty.setValue(progressRatio);
        this.getTextAlignmentProperty().setValue(EAlignment.MIDDLE_CENTER);
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

    public BaseProperty<EHAlignment> getProgressDirectionProperty()
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

    public EHAlignment getProgressDirection()
    {
        return this.getProgressDirectionProperty().getValue();
    }

    public void setProgressDirection(EHAlignment direction)
    {
        this.getProgressDirectionProperty().setValue(direction);
    }

}
