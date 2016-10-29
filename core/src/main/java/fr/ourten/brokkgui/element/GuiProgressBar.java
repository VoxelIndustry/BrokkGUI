package fr.ourten.brokkgui.element;

import fr.ourten.brokkgui.behavior.GuiBehaviorBase;
import fr.ourten.brokkgui.control.GuiLabeled;
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
    
    public GuiProgressBar(final String text, final float progressRatio)
    {
        super(text);
        this.progressBarProgressProperty = new BaseProperty<>(0f, "progressBarProgressProperty");
        this.progressBarColorProperty = new BaseProperty<Color>(Color.WHITE, "progressBarColorProperty");
        
        this.progressBarProgressProperty.setChecker((old, set) -> {
            if(set < 0 || set > 1)
                return old;
            return set;
        });
        
        this.progressBarProgressProperty.setValue(progressRatio);
    }
    
    public GuiProgressBar(final float progressRatio)
    {
        this("", progressRatio);
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
    
    public float getProgress()
    {
        return this.getProgressProperty().getValue();
    }
    
    public Color getColor()
    {
        return this.getColorProperty().getValue();
    }
    
}
