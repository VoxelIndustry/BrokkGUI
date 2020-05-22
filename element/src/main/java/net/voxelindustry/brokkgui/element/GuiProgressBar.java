package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.behavior.GuiBehaviorBase;
import net.voxelindustry.brokkgui.control.GuiLabeled;
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

    public GuiProgressBar(String text, float progressRatio)
    {
        super(text);
        progressBarProgressProperty = new BaseProperty<>(0f, "progressProperty");
        progressBarProgressDirection = new BaseProperty<>(RectSide.RIGHT, "progressDirectionProperty");

        progressBarProgressProperty.setChecker((old, set) ->
        {
            if (set < 0 || set > 1)
                return old;
            return set;
        });

        progressBarProgressProperty.setValue(progressRatio);
        getTextAlignmentProperty().setValue(RectAlignment.MIDDLE_CENTER);
    }

    public GuiProgressBar(String text)
    {
        this(text, 0);
    }

    public GuiProgressBar(float progressRatio)
    {
        this("", progressRatio);
    }

    public GuiProgressBar()
    {
        this("");
    }

    @Override
    public String type()
    {
        return "progress";
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiProgressBarSkin<>(this, new GuiBehaviorBase<>(this));
    }

    public BaseProperty<Float> getProgressProperty()
    {
        return progressBarProgressProperty;
    }

    public BaseProperty<RectSide> getProgressDirectionProperty()
    {
        return progressBarProgressDirection;
    }

    public float getProgress()
    {
        return getProgressProperty().getValue();
    }

    public void setProgress(float progress)
    {
        getProgressProperty().setValue(progress);
    }

    public RectSide getProgressDirection()
    {
        return getProgressDirectionProperty().getValue();
    }

    public void setProgressDirection(RectSide direction)
    {
        getProgressDirectionProperty().setValue(direction);
    }

}
