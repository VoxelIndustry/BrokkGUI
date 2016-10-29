package fr.ourten.brokkgui.element;

import fr.ourten.brokkgui.behavior.GuiBehaviorProgressBar;
import fr.ourten.brokkgui.control.GuiLabeled;
import fr.ourten.brokkgui.paint.Color;
import fr.ourten.brokkgui.skin.GuiProgressBarSkin;
import fr.ourten.teabeans.value.BaseProperty;

/**
 * @author Phenix246
 */
public class GuiProgressBar extends GuiLabeled
{
    private final BaseProperty<Integer> progressBarMinValueProperty;
    private final BaseProperty<Integer> progressBarMaxValueProperty;
    private final BaseProperty<Float> progressBarProgressValueProperty;
    private final BaseProperty<Color> progressBarColorProperty;
    
    public GuiProgressBar(final String text, final int minValue, final int maxValue, final float progressValue)
    {
        super(text);
        this.progressBarMinValueProperty = new BaseProperty<>(0, "progressBarMinValueProperty");
        this.progressBarMaxValueProperty = new BaseProperty<>(0, "progressBarMaxValueProperty");
        this.progressBarProgressValueProperty = new BaseProperty<>(0f, "progressBarProgressValueProperty");
        this.progressBarColorProperty = new BaseProperty<Color>(Color.WHITE, "progressBarColorProperty");
        
        this.progressBarMaxValueProperty.setValue(maxValue);
        
        this.progressBarMinValueProperty.setChecker((old, set) -> {
            if(progressBarMaxValueProperty.getValue() < set)
                return progressBarMaxValueProperty.getValue();
            return set;
        });
        
        this.progressBarMaxValueProperty.setChecker((old, set) -> {
            if(progressBarMinValueProperty.getValue() > set)
                return progressBarMinValueProperty.getValue();
            return set;
        });
        
        this.progressBarMaxValueProperty.setChecker((old, set) -> {
            if(progressBarMaxValueProperty.getValue() < set)
                return progressBarMaxValueProperty.getValue();
            if(progressBarMinValueProperty.getValue() > set)
                return progressBarMinValueProperty.getValue();
            return set;
        });
        
        this.progressBarMinValueProperty.setValue(minValue);
        this.progressBarProgressValueProperty.setValue(progressValue);
    }
    
    public GuiProgressBar(final int minValue, final int maxValue, final int progressValue)
    {
        this("", minValue, maxValue, progressValue);
    }
    
    public GuiProgressBar(final String text, final int minValue, final int maxValue)
    {
        this(text, minValue, maxValue, minValue);
    }
    
    public GuiProgressBar(final int minValue, final int maxValue)
    {
        this("", minValue, maxValue, minValue);
    }
    
    @Override
    protected GuiProgressBarSkin<GuiProgressBar, GuiBehaviorProgressBar<GuiProgressBar>> makeDefaultSkin()
    {
        return new GuiProgressBarSkin<>(this, new GuiBehaviorProgressBar<>(this));
    }
    
    public void setProgress(float progress)
    {
        this.getProgressProperty().setValue(progress);
    }
    
    public BaseProperty<Float> getProgressProperty()
    {
        return this.progressBarProgressValueProperty;
    }
    
    public BaseProperty<Integer> getMinValueProperty()
    {
        return this.progressBarMinValueProperty;
    }
    
    public BaseProperty<Integer> getMaxValueProperty()
    {
        return this.progressBarMaxValueProperty;
    }
    
    public BaseProperty<Color> getColorProperty()
    {
        return this.progressBarColorProperty;
    }
    
    public float getProgressValue()
    {
        return this.getProgressProperty().getValue();
    }
    
    public int getMinValue()
    {
        return this.getMinValueProperty().getValue();
    }
    
    public int getMaxValue()
    {
        return this.getMaxValueProperty().getValue();
    }
    
    public Color getColor()
    {
        return this.getColorProperty().getValue();
    }
    
    public float getProgressRatio()
    {
        return (this.getProgressValue() - this.getMinValue()) / this.getMaxValue();
    }
}
