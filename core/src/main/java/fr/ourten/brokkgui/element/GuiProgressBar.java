package fr.ourten.brokkgui.element;

import fr.ourten.brokkgui.behavior.GuiBehaviorBase;
import fr.ourten.brokkgui.control.GuiLabeled;
import fr.ourten.brokkgui.data.EHAlignment;
import fr.ourten.brokkgui.paint.Color;
import fr.ourten.brokkgui.skin.GuiProgressBarSkin;
import fr.ourten.teabeans.value.BaseProperty;

/**
 * @author Phenix246
 */
public class GuiProgressBar extends GuiLabeled
{
    private final BaseProperty<Float> progressBarProgressProperty;
    private final BaseProperty<Color> progressBarColorProperty;
    private final BaseProperty<EHAlignment> progressBarProgressDirection;
    
    public GuiProgressBar(final String text, final float progressRatio, final EHAlignment progressDirection)
    {
        super(text);
        this.progressBarProgressProperty = new BaseProperty<>(0f, "progressBarProgressProperty");
        this.progressBarColorProperty = new BaseProperty<Color>(Color.WHITE, "progressBarColorProperty");
        this.progressBarProgressDirection = new BaseProperty<EHAlignment>(progressDirection, "progressBarProgressDirection");
        
        this.progressBarProgressProperty.setChecker((old, set) -> {
            if(set < 0 || set > 1)
                return old;
            return set;
        });
        
        this.progressBarProgressProperty.setValue(progressRatio);
    }
    
    public GuiProgressBar(final float progressRatio, final EHAlignment progressDirection)
    {
        this("", progressRatio, progressDirection);
    }
    
    public GuiProgressBar(final String text, final float progressRatio)
    {
        this(text, progressRatio, EHAlignment.RIGHT);
    }
    
    public GuiProgressBar(final float progressRatio)
    {
        this("", progressRatio, EHAlignment.RIGHT);
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
    
    public BaseProperty<Color> getColorProperty()
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
    
    public Color getColor()
    {
        return this.getColorProperty().getValue();
    }
    
    public EHAlignment getProgressDirection()
    {
        return this.getProgressDirectionProperty().getValue();
    }
    
}
