package net.voxelindustry.brokkgui.skin;

import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;

public class GuiButtonSkin<C extends GuiButtonBase, B extends GuiButtonBehavior<C>> extends GuiBehaviorSkinBase<C, B>
{
    public GuiButtonSkin(final C model, final B behaviour)
    {
        super(model, behaviour);

        this.getModel().getLabel().getStyleClass().add("label");
        this.bindLabel();
    }

    protected void bindLabel()
    {
        RelativeBindingHelper.bindToPos(getModel().getLabel(), getModel());

        getModel().getExpandToLabelProperty().addListener(obs -> this.refreshLabelBinding());
        this.refreshLabelBinding();
    }

    private void refreshLabelBinding()
    {
        if (getModel().expandToLabel())
        {
            getModel().getLabel().getWidthProperty().unbind();
            getModel().getLabel().getHeightProperty().unbind();

            getModel().getLabel().setExpandToText(true);

            getModel().getWidthProperty().bind(getModel().getLabel().getWidthProperty());
            getModel().getHeightProperty().bind(getModel().getLabel().getHeightProperty());
        }
        else
        {
            getModel().getLabel().setExpandToText(false);

            getModel().getWidthProperty().unbind();
            getModel().getHeightProperty().unbind();

            getModel().getLabel().getWidthProperty().bind(getModel().getWidthProperty());
            getModel().getLabel().getHeightProperty().bind(getModel().getHeightProperty());
        }
    }

    @Override
    public void render(RenderPass pass, IGuiRenderer renderer, int mouseX, int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);
    }
}