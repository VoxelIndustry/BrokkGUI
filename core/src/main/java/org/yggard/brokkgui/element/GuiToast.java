package org.yggard.brokkgui.element;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.component.IGuiPopup;
import org.yggard.brokkgui.control.GuiFather;
import org.yggard.brokkgui.data.RelativeBindingHelper;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.panel.GuiAbsolutePane;
import org.yggard.brokkgui.panel.GuiPane;

public class GuiToast extends GuiFather implements IGuiPopup
{
    private final BaseProperty<GuiPane> contentPanelProperty;
    private final BaseProperty<Float>   lifeTimeProperty;
    private final BaseProperty<Float>   currentTimeProperty;

    private float millisStart;

    public GuiToast(GuiPane contentPanel, float lifeTime)
    {
        super("toast");

        this.contentPanelProperty = new BaseProperty<>(null, "contentPanelProperty");

        this.contentPanelProperty.addListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                this.removeChild(oldValue);
                oldValue.getWidthProperty().unbind();
                oldValue.getHeightProperty().unbind();
                oldValue.getxPosProperty().unbind();
                oldValue.getyPosProperty().unbind();
            }

            if (newValue != null)
            {
                this.addChild(newValue);
                RelativeBindingHelper.bindToPos(newValue, this);
                newValue.getWidthProperty().bind(this.getWidthProperty());
                newValue.getHeightProperty().bind(this.getHeightProperty());
            }
        });

        this.contentPanelProperty.setValue(contentPanel);

        this.lifeTimeProperty = new BaseProperty<>(lifeTime, "lifeTimeProperty");
        this.currentTimeProperty = new BaseProperty<>(0F, "currentTimeProperty");
    }

    public GuiToast(float lifeTime)
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
            currentTimeProperty.setValue(System.currentTimeMillis() - millisStart);
        }
    }

    public BaseProperty<GuiPane> getContentPanelProperty()
    {
        return contentPanelProperty;
    }

    public BaseProperty<Float> getLifeTimeProperty()
    {
        return lifeTimeProperty;
    }

    public BaseProperty<Float> getCurrentTimeProperty()
    {
        return currentTimeProperty;
    }

    public GuiPane getContentPanel()
    {
        return this.getContentPanelProperty().getValue();
    }

    public void setContentPanel(GuiPane contentPane)
    {
        this.getContentPanelProperty().setValue(contentPane);
    }

    public float getLifeTime()
    {
        return this.getLifeTimeProperty().getValue();
    }

    public void setLifeTime(float lifeTime)
    {
        this.getLifeTimeProperty().setValue(lifeTime);
    }

    public float getCurrentTime()
    {
        return this.getCurrentTimeProperty().getValue();
    }

    public void setCurrentTime(float currentTime)
    {
        this.getCurrentTimeProperty().setValue(currentTime);
    }

    public void addCurrentTime(float currentLife)
    {
        this.getCurrentTimeProperty().setValue(this.getCurrentTime() + currentLife);
    }
}
