package org.yggard.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseExpression;
import org.yggard.brokkgui.behavior.GuiBehaviorBase;
import org.yggard.brokkgui.element.GuiProgressBar;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.shape.Rectangle;

public class GuiProgressBarSkin<C extends GuiProgressBar, B extends GuiBehaviorBase<C>> extends GuiLabeledSkinBase<C, B>
{
    private final Rectangle track;

    public GuiProgressBarSkin(final C model, final B behaviour)
    {
        super(model, behaviour);

        this.track = new Rectangle(model.getxPos(), model.getyPos(), model.getWidth(), model.getHeight());
        this.track.getxPosProperty().bind(new BaseExpression<>(() ->
        {
            switch (model.getProgressDirection())
            {
                case CENTER:
                    return model.getxPos() + (model.getWidth() - this.track.getWidth()) / 2;
                case LEFT:
                    return model.getxPos() + model.getWidth() - this.track.getWidth();
                default:
                    return model.getxPos();
            }
        }, model.getxPosProperty(), model.getWidthProperty(), model.getProgressDirectionProperty()));
        this.track.getyPosProperty().bind(model.getyPosProperty());
        this.track.getWidthProperty().bind(new BaseExpression<>(() ->
                model.getWidth() * model.getProgress(), model.getProgressProperty(), model.getWidthProperty()));

        this.getModel().getStyle().registerAlias("track", this.track.getStyle());

        this.getText().getxPosProperty().bind(new BaseExpression<>(() ->
                model.getxPos() + model.getWidth() / 2, model.getxPosProperty(), model.getWidthProperty()));

        this.getText().getyPosProperty().bind(new BaseExpression<>(() ->
                model.getyPos() + model.getHeight() / 2, model.getyPosProperty(), model.getHeightProperty()));

        this.getText().getTextAlignmentProperty().bind(model.getTextAlignmentProperty());
        this.getText().getzLevelProperty().bind(new BaseExpression<>(() ->
                model.getzLevel() + 1, model.getzLevelProperty()));

    }

    @Override
    public void render(final RenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);

        this.getModel().getBackground().renderNode(renderer, pass, mouseX, mouseY);
        this.track.renderNode(renderer, pass, mouseX, mouseY);
    }
}
