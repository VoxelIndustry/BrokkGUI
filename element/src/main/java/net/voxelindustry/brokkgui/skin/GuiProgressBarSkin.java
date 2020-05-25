package net.voxelindustry.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseExpression;
import net.voxelindustry.brokkgui.behavior.GuiBehaviorBase;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.element.GuiProgressBar;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.style.StyleComponent;

public class GuiProgressBarSkin<C extends GuiProgressBar, B extends GuiBehaviorBase<C>> extends GuiLabeledSkin<C, B>
{
    private final Rectangle track;

    public GuiProgressBarSkin(C model, B behaviour)
    {
        super(model, behaviour);

        track = new Rectangle(model.transform().xPos(), model.transform().yPos(), model.width(), model.height());
        track.transform().xPosProperty().bind(new BaseExpression<>(() ->
        {
            if (model.getProgressDirection() == RectSide.LEFT)
                return model.transform().xPos() + model.width() - track.width();
            return model.transform().xPos();
        }, model.transform().xPosProperty(), model.transform().widthProperty(), model.getProgressDirectionProperty()));
        track.transform().yPosProperty().bind(model.transform().yPosProperty());
        track.transform().widthProperty().bind(new BaseExpression<>(() ->
                model.width() * model.getProgress(), model.getProgressProperty(), model.transform().widthProperty()));

        track.get(StyleComponent.class).styleClass().add("track");

        getModel().addChild(track);
    }
}
