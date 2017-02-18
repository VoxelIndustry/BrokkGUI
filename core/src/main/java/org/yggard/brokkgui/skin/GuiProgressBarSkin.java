package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.behavior.GuiBehaviorBase;
import org.yggard.brokkgui.element.GuiProgressBar;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.shape.Rectangle;

import fr.ourten.teabeans.binding.BaseExpression;

public class GuiProgressBarSkin<C extends GuiProgressBar, B extends GuiBehaviorBase<C>> extends GuiLabeledSkinBase<C, B>
{
    private final Rectangle progressBar;

    public GuiProgressBarSkin(final C model, final B behaviour)
    {
        super(model, behaviour);

        this.progressBar = new Rectangle(model.getxPos(), model.getyPos(), model.getWidth(), model.getHeight());
        this.progressBar.getxPosProperty().bind(new BaseExpression<>(() ->
        {
            switch (model.getProgressDirection())
            {
                case CENTER:
                    return model.getxPos() + (model.getWidth() - this.progressBar.getWidth()) / 2;
                case LEFT:
                    return model.getxPos() + model.getWidth() - this.progressBar.getWidth();
                default:
                    return model.getxPos();
            }
        }, model.getxPosProperty(), model.getWidthProperty(), model.getProgressDirectionProperty()));
        this.progressBar.getyPosProperty().bind(model.getyPosProperty());
        this.progressBar.getWidthProperty().bind(new BaseExpression<>(() ->
        {
            return model.getWidth() * model.getProgress();
        }, model.getProgressProperty(), model.getWidthProperty()));
        this.progressBar.getFillProperty().bind(new BaseExpression<>(() ->
        {
            return model.getBackgroundProperty().getValue().getFill();
        }, model.getBackgroundProperty()));

        this.getText().getxPosProperty().bind(new BaseExpression<>(() ->
        {
            return model.getxPos() + model.getWidth() / 2;
        }, model.getxPosProperty(), model.getWidthProperty()));

        this.getText().getyPosProperty().bind(new BaseExpression<>(() ->
        {
            return model.getyPos() + model.getHeight() / 2;
        }, model.getyPosProperty(), model.getHeightProperty()));

        this.getText().getTextAlignmentProperty().bind(model.getTextAlignmentProperty());
        this.getText().getzLevelProperty().bind(new BaseExpression<>(() ->
        {
            return model.getzLevel() + 1;
        }, model.getzLevelProperty()));

    }

    @Override
    public void render(final EGuiRenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);
        this.progressBar.renderNode(renderer, pass, mouseX, mouseY);
    }

    public Rectangle getProgressBar()
    {
        return this.progressBar;
    }
}
