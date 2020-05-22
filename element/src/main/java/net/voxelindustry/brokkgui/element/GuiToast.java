package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.IGuiPopup;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.element.pane.GuiAbsolutePane;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class GuiToast extends GuiFather implements IGuiPopup
{
    private final BaseProperty<GuiElement> contentProperty;
    private final BaseProperty<Long>       lifeTimeProperty;
    private final BaseProperty<Long>       currentTimeProperty;

    private long millisStart;

    public GuiToast(GuiElement content, long lifeTime)
    {
        contentProperty = new BaseProperty<>(null, "contentProperty");

        contentProperty.addListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                removeChild(oldValue);
                transform().widthProperty().unbind();
                transform().heightProperty().unbind();
                oldValue.transform().xPosProperty().unbind();
                oldValue.transform().yPosProperty().unbind();
            }

            if (newValue != null)
            {
                addChild(newValue);
                RelativeBindingHelper.bindToPos(newValue.transform(), transform());
                transform().widthProperty().bind(newValue.transform().widthProperty());
                transform().heightProperty().bind(newValue.transform().heightProperty());
            }
        });

        contentProperty.setValue(content);

        lifeTimeProperty = new BaseProperty<>(lifeTime, "lifeTimeProperty");
        currentTimeProperty = new BaseProperty<>(0L, "currentTimeProperty");
    }

    public GuiToast(long lifeTime)
    {
        this(new GuiAbsolutePane(), lifeTime);
    }

    @Override
    public void renderContent(IGuiRenderer renderer, RenderPass pass, int mouseX, int mouseY)
    {
        super.renderContent(renderer, pass, mouseX, mouseY);

        if (pass == RenderPass.MAIN)
        {
            if (millisStart == 0)
                millisStart = System.currentTimeMillis();
            currentTimeProperty.setValue((System.currentTimeMillis() - millisStart));
        }
    }

    @Override
    public String type()
    {
        return "toast";
    }

    public BaseProperty<GuiElement> getContentProperty()
    {
        return contentProperty;
    }

    public BaseProperty<Long> getLifeTimeProperty()
    {
        return lifeTimeProperty;
    }

    public BaseProperty<Long> getCurrentTimeProperty()
    {
        return currentTimeProperty;
    }

    public GuiElement getContent()
    {
        return getContentProperty().getValue();
    }

    public void setContent(GuiElement content)
    {
        getContentProperty().setValue(content);
    }

    public long getLifeTime()
    {
        return getLifeTimeProperty().getValue();
    }

    public void setLifeTime(long lifeTime)
    {
        getLifeTimeProperty().setValue(lifeTime);
    }

    public long getCurrentTime()
    {
        return getCurrentTimeProperty().getValue();
    }

    public void resetCurrentTime()
    {
        getCurrentTimeProperty().setValue(0L);
        millisStart = 0;
    }

    private void addCurrentTime(long currentLife)
    {
        getCurrentTimeProperty().setValue(getCurrentTime() + currentLife);
    }
}
