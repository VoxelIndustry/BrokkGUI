package fr.ourten.brokkgui.skin;

import fr.ourten.brokkgui.behavior.GuiBehaviorBase;
import fr.ourten.brokkgui.element.GuiProgressBar;
import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.Color;
import fr.ourten.brokkgui.paint.EGuiRenderPass;
import fr.ourten.brokkgui.shape.Rectangle;
import fr.ourten.teabeans.binding.BaseBinding;

public class GuiProgressBarSkin<C extends GuiProgressBar, B extends GuiBehaviorBase<C>> extends GuiLabeledSkinBase<C, B>
{
    private final Rectangle progressBar;

    public GuiProgressBarSkin(C model, B behaviour)
    {
        super(model, behaviour);

        this.progressBar = new Rectangle(model.getxPos(), model.getyPos(), model.getWidth(), model.getHeight());
        this.progressBar.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getxPosProperty());
                super.bind(model.getWidthProperty());
                super.bind(model.getProgressDirectionProperty());
            }

            @Override
            public Float computeValue()
            {
                switch (model.getProgressDirection())
                {
                    case CENTER:
                        return model.getxPos() + ((model.getWidth() - progressBar.getWidth()) / 2);
                    case LEFT:
                        return model.getxPos() + model.getWidth() - progressBar.getWidth();
                    default:
                        return model.getxPos();
                }
            }
        });
        this.progressBar.getyPosProperty().bind(model.getyPosProperty());
        this.progressBar.getWidthProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getProgressProperty());
                super.bind(model.getWidthProperty());
            }

            @Override
            public Float computeValue()
            {
                return model.getWidth() * model.getProgress();
            }
        });
        this.progressBar.getColorProperty().bind(new BaseBinding<Color>()
        {
            {
                super.bind(model.getBackgroundProperty());
            }

            @Override
            public Color computeValue()
            {
                return model.getBackgroundProperty().getValue().getColor();
            }
        });
        this.getText().getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getxPosProperty());
                super.bind(model.getWidthProperty());
            }

            @Override
            public Float computeValue()
            {
                return model.getxPos() + (model.getWidth() / 2);
            }

        });
        this.getText().getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getyPosProperty());
                super.bind(model.getHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                return model.getyPos() + (model.getHeight() / 2);
            }

        });
        this.getText().getTextAlignmentProperty().bind(model.getTextAlignmentProperty());
        this.getText().getShadowProperty().setValue(false);
        this.getText().getzLevelProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getzLevelProperty());
            }

            @Override
            public Float computeValue()
            {
                return model.getzLevel() + 1;
            }
        });
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
