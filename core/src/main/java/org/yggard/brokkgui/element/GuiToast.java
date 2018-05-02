package org.yggard.brokkgui.element;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.component.IGuiPopup;
import org.yggard.brokkgui.control.GuiFather;
import org.yggard.brokkgui.data.RelativeBindingHelper;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.panel.GuiAbsolutePane;

public class GuiToast extends GuiFather implements IGuiPopup
{
    private final BaseProperty<GuiNode> contentProperty;
    private final BaseProperty<Long>    lifeTimeProperty;
    private final BaseProperty<Long>    currentTimeProperty;

    private long millisStart;

    public GuiToast(GuiNode content, long lifeTime)
    {
        super("toast");

        this.contentProperty = new BaseProperty<>(null, "contentProperty");

        this.contentProperty.addListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                this.removeChild(oldValue);
                this.getWidthProperty().unbind();
                this.getHeightProperty().unbind();
                oldValue.getxPosProperty().unbind();
                oldValue.getyPosProperty().unbind();
            }

            if (newValue != null)
            {
                this.addChild(newValue);
                RelativeBindingHelper.bindToPos(newValue, this);
                this.getWidthProperty().bind(newValue.getWidthProperty());
                this.getHeightProperty().bind(newValue.getHeightProperty());
            }
        });

        this.contentProperty.setValue(content);

        this.lifeTimeProperty = new BaseProperty<>(lifeTime, "lifeTimeProperty");
        this.currentTimeProperty = new BaseProperty<>(0L, "currentTimeProperty");
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

    public BaseProperty<GuiNode> getContentProperty()
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

    public GuiNode getContent()
    {
        return this.getContentProperty().getValue();
    }

    public void setContent(GuiNode content)
    {
        this.getContentProperty().setValue(content);
    }

    public long getLifeTime()
    {
        return this.getLifeTimeProperty().getValue();
    }

    public void setLifeTime(long lifeTime)
    {
        this.getLifeTimeProperty().setValue(lifeTime);
    }

    public long getCurrentTime()
    {
        return this.getCurrentTimeProperty().getValue();
    }

    public void resetCurrentTime()
    {
        this.getCurrentTimeProperty().setValue(0L);
        this.millisStart = 0;
    }

    private void addCurrentTime(long currentLife)
    {
        this.getCurrentTimeProperty().setValue(this.getCurrentTime() + currentLife);
    }
}
