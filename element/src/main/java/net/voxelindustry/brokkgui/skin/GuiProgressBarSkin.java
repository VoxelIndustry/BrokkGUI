package net.voxelindustry.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseExpression;
import net.voxelindustry.brokkgui.behavior.GuiBehaviorBase;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.element.GuiProgressBar;

public class GuiProgressBarSkin<C extends GuiProgressBar, B extends GuiBehaviorBase<C>> extends GuiLabeledSkinBase<C, B>
{
    private final Rectangle track;

    public GuiProgressBarSkin(final C model, final B behaviour)
    {
        super(model, behaviour);

        this.track = new Rectangle(model.getxPos(), model.getyPos(), model.getWidth(), model.getHeight());
        this.track.getxPosProperty().bind(new BaseExpression<>(() ->
        {
            if (model.getProgressDirection() == RectSide.LEFT)
                return model.getxPos() + model.getWidth() - this.track.getWidth();
            return model.getxPos();
        }, model.getxPosProperty(), model.getWidthProperty(), model.getProgressDirectionProperty()));
        this.track.getyPosProperty().bind(model.getyPosProperty());
        this.track.getWidthProperty().bind(new BaseExpression<>(() ->
                model.getWidth() * model.getProgress(), model.getProgressProperty(), model.getWidthProperty()));

        this.track.getStyleClass().add("track");

        this.getText().getxPosProperty().bind(new BaseExpression<>(() ->
                model.getxPos() + model.getWidth() / 2, model.getxPosProperty(), model.getWidthProperty()));

        this.getText().getyPosProperty().bind(new BaseExpression<>(() ->
                model.getyPos() + model.getHeight() / 2, model.getyPosProperty(), model.getHeightProperty()));

        this.getText().getzLevelProperty().bind(new BaseExpression<>(() ->
                model.getzLevel() + 1, model.getzLevelProperty()));

        this.getModel().addChild(track);
    }
}
