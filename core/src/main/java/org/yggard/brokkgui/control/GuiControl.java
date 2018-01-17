package org.yggard.brokkgui.control;

import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Background;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.skin.GuiSkinBase;
import org.yggard.brokkgui.skin.IGuiSkinnable;

import fr.ourten.teabeans.value.BaseProperty;

public abstract class GuiControl extends GuiFather implements IGuiSkinnable
{
    private final BaseProperty<GuiSkinBase<?>> skinProperty;
    private final BaseProperty<Background>     backgroundProperty;

    public GuiControl(String type)
    {
        super(type);

        this.skinProperty = new BaseProperty<>(null, "skinProperty");

        final Background background = new Background();
        background.attach(this);
        this.backgroundProperty = new BaseProperty<>(background, "backgroundProperty");
        this.backgroundProperty.addListener((property, oldValue, newValue) ->
        {
            if (oldValue != null)
                oldValue.detach(this);
            if (newValue != null)
                newValue.attach(this);
        });
    }

    @Override
    public void renderNode(final IGuiRenderer renderer, final EGuiRenderPass pass, final int mouseX, final int mouseY)
    {
        super.renderNode(renderer, pass, mouseX, mouseY);
        this.getSkin().render(pass, renderer, mouseX, mouseY);
    }

    protected abstract GuiSkinBase<?> makeDefaultSkin();

    @Override
    public GuiSkinBase<?> getSkin()
    {
        if (!this.skinProperty.isPresent())
            this.skinProperty.setValue(this.makeDefaultSkin());
        return this.skinProperty.getValue();
    }

    @Override
    public void setStyle(String style)
    {
        if (!this.skinProperty.isPresent())
            this.skinProperty.setValue(this.makeDefaultSkin());
        super.setStyle(style);
    }

    @Override
    public void refreshStyle()
    {
        if (!this.skinProperty.isPresent())
            this.skinProperty.setValue(this.makeDefaultSkin());
        super.refreshStyle();
    }

    public BaseProperty<Background> getBackgroundProperty()
    {
        return backgroundProperty;
    }

    public Background getBackground()
    {
        return this.getBackgroundProperty().getValue();
    }

    public void setBackground(Background background)
    {
        this.getBackgroundProperty().setValue(background);
    }
}