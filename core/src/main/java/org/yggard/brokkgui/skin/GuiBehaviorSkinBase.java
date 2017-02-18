package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.behavior.GuiBehaviorBase;
import org.yggard.brokkgui.control.GuiControl;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Background;
import org.yggard.brokkgui.paint.EGuiRenderPass;

import fr.ourten.teabeans.value.BaseProperty;

public class GuiBehaviorSkinBase<C extends GuiControl, B extends GuiBehaviorBase<C>> extends GuiSkinBase<C>
{
    private final BaseProperty<Background> backgroundProperty, backgroundHoveredProperty, backgroundDisabledProperty;

    private final B                        behavior;

    public GuiBehaviorSkinBase(final C model, final B behavior)
    {
        super(model);

        this.behavior = behavior;

        this.backgroundProperty = new BaseProperty<>(new Background(), "backgroundProperty");
        this.backgroundHoveredProperty = new BaseProperty<>(new Background(), "backgroundHoveredProperty");
        this.backgroundDisabledProperty = new BaseProperty<>(new Background(), "backgroundDisabledProperty");

        this.getBackground().attach(model);
        this.getHoveredBackground().attach(model);
        this.getDisabledBackground().attach(model);
    }

    @Override
    public void render(final EGuiRenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        if (pass == EGuiRenderPass.MAIN)
            if (this.getModel().isDisabled())
                this.getDisabledBackground().renderNode(renderer, pass, mouseX, mouseY);
            else if (this.getModel().isHovered())
                this.getHoveredBackground().renderNode(renderer, pass, mouseX, mouseY);
            else
                this.getBackground().renderNode(renderer, pass, mouseX, mouseY);
        super.render(pass, renderer, mouseX, mouseY);
    }

    public B getBehavior()
    {
        return this.behavior;
    }

    ///////////////////////
    // GETTERS / SETTERS //
    ///////////////////////

    public BaseProperty<Background> getBackgroundProperty()
    {
        return this.backgroundProperty;
    }

    public BaseProperty<Background> getBackgroundHoveredProperty()
    {
        return this.backgroundHoveredProperty;
    }

    public BaseProperty<Background> getBackgroundDisabledProperty()
    {
        return this.backgroundDisabledProperty;
    }

    public Background getBackground()
    {
        return this.getBackgroundProperty().getValue();
    }

    public void setBackground(final Background background)
    {
        this.getBackgroundProperty().setValue(background);
    }

    public Background getHoveredBackground()
    {
        return this.getBackgroundHoveredProperty().getValue();
    }

    public void setHoveredBackground(final Background background)
    {
        this.getBackgroundHoveredProperty().setValue(background);
    }

    public Background getDisabledBackground()
    {
        return this.getBackgroundDisabledProperty().getValue();
    }

    public void setDisabledBackground(final Background background)
    {
        this.getBackgroundDisabledProperty().setValue(background);
    }
}