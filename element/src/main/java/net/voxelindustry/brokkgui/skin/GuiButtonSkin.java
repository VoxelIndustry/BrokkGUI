package net.voxelindustry.brokkgui.skin;

import net.voxelindustry.brokkgui.behavior.GuiButtonBehavior;
import net.voxelindustry.brokkgui.control.GuiButtonBase;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.style.StyleComponent;

public class GuiButtonSkin<C extends GuiButtonBase, B extends GuiButtonBehavior<C>> extends GuiBehaviorSkinBase<C, B>
{
    public GuiButtonSkin(C model, B behaviour)
    {
        super(model, behaviour);

        getModel().getLabel().get(StyleComponent.class).styleClass().add("label");
        bindLabel();
    }

    protected void bindLabel()
    {
        RelativeBindingHelper.bindToCenter(getModel().getLabel().transform(), transform());

        getModel().getExpandToLabelProperty().addListener(obs -> refreshLabelBinding());
        refreshLabelBinding();
    }

    private void refreshLabelBinding()
    {
        if (getModel().expandToLabel())
        {
            getModel().getLabel().transform().widthProperty().unbind();
            getModel().getLabel().transform().heightProperty().unbind();

            getModel().getLabel().setExpandToText(true);

            transform().widthProperty().bind(getModel().getLabel().transform().widthProperty());
            transform().heightProperty().bind(getModel().getLabel().transform().heightProperty());
        }
        else
        {
            getModel().getLabel().setExpandToText(false);

            transform().widthProperty().unbind();
            transform().heightProperty().unbind();

            getModel().getLabel().transform().widthProperty().bind(transform().widthProperty());
            getModel().getLabel().transform().heightProperty().bind(transform().heightProperty());
        }
    }

    @Override
    public void render(RenderPass pass, IGuiRenderer renderer, int mouseX, int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);
    }
}