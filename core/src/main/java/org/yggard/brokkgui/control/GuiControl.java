package org.yggard.brokkgui.control;

import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Background;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.skin.GuiSkinBase;
import org.yggard.brokkgui.skin.IGuiSkinnable;
import org.yggard.brokkgui.style.StyleSource;

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
        this.getStyle().registerProperty("-opacity", 1D, Double.class);
    }

    @Override
    public void renderContent(final IGuiRenderer renderer, final RenderPass pass, final int mouseX, final int
            mouseY)
    {
        if (this.getOpacity() != 1)
            renderer.getHelper().startAlphaMask(this.getOpacity());

        super.renderContent(renderer, pass, mouseX, mouseY);
        this.getSkin().render(pass, renderer, mouseX, mouseY);

        if (this.getOpacity() != 1)
            renderer.getHelper().closeAlphaMask();
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

    public BaseProperty<Double> getOpacityProperty()
    {
        return this.getStyle().getStyleProperty("-opacity", Double.class);
    }

    public double getOpacity()
    {
        return this.getOpacityProperty().getValue();
    }

    public void setOpacity(double opacity)
    {
        this.getStyle().getStyleProperty("-opacity", Double.class).setStyle(StyleSource.CODE, 0, opacity);
    }
}