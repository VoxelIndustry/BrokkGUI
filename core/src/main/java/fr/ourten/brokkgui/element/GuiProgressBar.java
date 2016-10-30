package fr.ourten.brokkgui.element;

import java.util.Objects;

import fr.ourten.brokkgui.behavior.GuiBehaviorBase;
import fr.ourten.brokkgui.control.GuiLabeled;
import fr.ourten.brokkgui.data.EAlignment;
import fr.ourten.brokkgui.data.EHAlignment;
import fr.ourten.brokkgui.paint.Background;
import fr.ourten.brokkgui.paint.Color;
import fr.ourten.brokkgui.skin.GuiProgressBarSkin;
import fr.ourten.brokkgui.skin.GuiSkinBase;
import fr.ourten.teabeans.value.BaseProperty;

/**
 * @author Phenix246
 */
public class GuiProgressBar extends GuiLabeled
{
    private final BaseProperty<Float>       progressBarProgressProperty;
    private final BaseProperty<Background>  progressBarColorProperty;
    private final BaseProperty<EHAlignment> progressBarProgressDirection;

    public GuiProgressBar(final String text, final float progressRatio)
    {
        super(text);
        this.progressBarProgressProperty = new BaseProperty<>(0f, "progressBarProgressProperty");
        this.progressBarColorProperty = new BaseProperty<Background>(new Background(Color.WHITE),
                "progressBarColorProperty");
        this.progressBarProgressDirection = new BaseProperty<EHAlignment>(EHAlignment.RIGHT,
                "progressBarProgressDirection");

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

    public BaseProperty<Background> getBackgroundProperty()
    {
        return this.progressBarColorProperty;
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

    public Background getBackground()
    {
        return this.getBackgroundProperty().getValue();
    }

    public void setBackground(Background background)
    {
        Objects.requireNonNull(background);
        this.getBackgroundProperty().setValue(background);
    }

    public EHAlignment getProgressDirection()
    {
        return this.getProgressDirectionProperty().getValue();
    }

    public void setProgressDirection(EHAlignment direction)
    {
        Objects.requireNonNull(direction);
        this.getProgressDirectionProperty().setValue(direction);
    }

}
