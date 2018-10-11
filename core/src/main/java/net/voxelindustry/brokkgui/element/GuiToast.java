package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.component.IGuiPopup;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.panel.GuiAbsolutePane;

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
