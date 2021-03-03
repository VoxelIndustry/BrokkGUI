package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.control.GuiLabeled;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectSide;

/**
 * @author Phenix246
 */
public class GuiProgressBar extends GuiLabeled
{
    private final Property<Float>    progressBarProgressProperty;
    private final Property<RectSide> progressBarProgressDirection;

    public GuiProgressBar(String text, float progressRatio)
    {
        super(text);
        progressBarProgressProperty = new Property<>(0F);
        progressBarProgressDirection = new Property<>(RectSide.RIGHT);

        progressBarProgressProperty.setValue(progressRatio);
        textAlignmentProperty().setValue(RectAlignment.MIDDLE_CENTER);
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

    public Property<Float> getProgressProperty()
    {
        return progressBarProgressProperty;
    }

    public Property<RectSide> getProgressDirectionProperty()
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
