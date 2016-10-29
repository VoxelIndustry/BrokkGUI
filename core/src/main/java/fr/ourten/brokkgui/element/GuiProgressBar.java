package fr.ourten.brokkgui.element;

import fr.ourten.brokkgui.behavior.GuiBehaviorBase;
import fr.ourten.brokkgui.control.GuiLabeled;
import fr.ourten.brokkgui.data.EHAlignment;
import fr.ourten.brokkgui.paint.Background;
import fr.ourten.brokkgui.paint.Color;
import fr.ourten.brokkgui.skin.GuiProgressBarSkin;
import fr.ourten.teabeans.value.BaseProperty;

/**
 * @author Phenix246
 */
public class GuiProgressBar extends GuiLabeled
{
    private final BaseProperty<Float>       progressBarProgressProperty;
    private final BaseProperty<Background>  progressBarColorProperty;
    private final BaseProperty<EHAlignment> progressBarProgressDirection;

    public GuiProgressBar(final String text, final Background background, final float progressRatio,
            final EHAlignment progressDirection)
    {
        super(text);
        this.progressBarProgressProperty = new BaseProperty<>(0f, "progressBarProgressProperty");
        this.progressBarColorProperty = new BaseProperty<Background>(background, "progressBarColorProperty");
        this.progressBarProgressDirection = new BaseProperty<EHAlignment>(progressDirection,
                "progressBarProgressDirection");

        this.progressBarProgressProperty.setChecker((old, set) ->
        {
            if (set < 0 || set > 1)
                return old;
            return set;
        });

        this.progressBarProgressProperty.setValue(progressRatio);

    }

    public GuiProgressBar(final String text, final Background background, final float progressRatio)
    {
        this(text, background, progressRatio, EHAlignment.RIGHT);
    }

    public GuiProgressBar(final Background background, final float progressRatio, final EHAlignment progressDirection)
    {
        this("", background, progressRatio, progressDirection);
    }

    public GuiProgressBar(final Background background, final float progressRatio)
    {
        this(background, progressRatio, EHAlignment.RIGHT);
    }

    public GuiProgressBar(final String text, final Background background)
    {
        this(text, background, 0);
    }

    public GuiProgressBar(final float progressRatio, final EHAlignment progressDirection)
    {
        this(new Background(Color.WHITE), progressRatio, progressDirection);
    }

    public GuiProgressBar(final Background background)
    {
        this(background, 0);
    }

    public GuiProgressBar(final String text)
    {
        this(text, new Background(Color.WHITE));
    }

    public GuiProgressBar()
    {
        this(new Background(Color.WHITE));
    }

    @Override
    protected GuiProgressBarSkin<GuiProgressBar, GuiBehaviorBase<GuiProgressBar>> makeDefaultSkin()
    {
        return new GuiProgressBarSkin<>(this, new GuiBehaviorBase<>(this));
    }

    public void setProgress(float progress)
    {
        this.getProgressProperty().setValue(progress);
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

    public Background getBackground()
    {
        return this.getBackgroundProperty().getValue();
    }

    public EHAlignment getProgressDirection()
    {
        return this.getProgressDirectionProperty().getValue();
    }

}
