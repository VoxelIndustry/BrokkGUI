package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.behavior.GuiButtonBehavior;
import org.yggard.brokkgui.data.EHAlignment;
import org.yggard.brokkgui.element.GuiRadioButton;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.shape.Rectangle;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.binding.BaseExpression;

/**
 * @author Ourten 18 nov. 2016
 */
public class GuiRadioButtonSkin extends GuiLabeledSkinBase<GuiRadioButton, GuiButtonBehavior<GuiRadioButton>>
{
    private final Rectangle box;
    private final Rectangle fill;

    public GuiRadioButtonSkin(final GuiRadioButton model, final GuiButtonBehavior<GuiRadioButton> behaviour)
    {
        super(model, behaviour);

        this.getTextPaddingProperty().bind(BaseExpression.biCombine(model.getHeightProperty(),
                model.getLabelPaddingProperty(), (height, padding) -> height + padding));
        this.getTextPaddingAlignmentProperty().bind(model.getLabelAlignmentProperty());

        this.box = new Rectangle();
        this.fill = new Rectangle();

        this.box.setColor(Color.ALPHA);
        this.box.setLineWeight(1);
        this.box.setLineColor(Color.BLACK);

        this.fill.setColor(Color.BLACK);

        this.box.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getWidthProperty(), model.getLabelAlignmentProperty(), model.getxPosProperty(),
                        model.getxTranslateProperty(), GuiRadioButtonSkin.this.getTextPaddingProperty(),
                        GuiRadioButtonSkin.this.box.getWidthProperty(),
                        GuiRadioButtonSkin.this.getEllipsedTextProperty());
            }

            @Override
            public Float computeValue()
            {
                if (model.getLabelAlignment().equals(EHAlignment.RIGHT))
                    return model.getxPos() + model.getxTranslate();
                else if (model.getLabelAlignment().equals(EHAlignment.LEFT))
                    return model.getxPos() + model.getxTranslate() + BrokkGuiPlatform.getInstance().getGuiHelper()
                            .getStringWidth(GuiRadioButtonSkin.this.getEllipsedText()) + 2;
                else
                    return model.getxPos() + model.getxTranslate() + model.getWidth() / 2
                            - GuiRadioButtonSkin.this.box.getWidth() / 2;
            }
        });
        this.box.getyPosProperty().bind(BaseExpression.biCombine(model.getyPosProperty(), model.getyTranslateProperty(),
                (yPos, yTranslate) -> yPos + yTranslate));
        this.box.getWidthProperty().bind(this.box.getHeightProperty());
        this.box.getHeightProperty().bind(model.getHeightProperty());

        this.fill.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getWidthProperty(), model.getLabelAlignmentProperty(), model.getxPosProperty(),
                        model.getxTranslateProperty(), GuiRadioButtonSkin.this.getTextPaddingProperty(),
                        GuiRadioButtonSkin.this.fill.getWidthProperty(),
                        GuiRadioButtonSkin.this.getEllipsedTextProperty());
            }

            @Override
            public Float computeValue()
            {
                if (model.getLabelAlignment().equals(EHAlignment.RIGHT))
                    return model.getxPos() + model.getxTranslate() + 2;
                else if (model.getLabelAlignment().equals(EHAlignment.LEFT))
                    return model.getxPos() + model.getxTranslate() + BrokkGuiPlatform.getInstance().getGuiHelper()
                            .getStringWidth(GuiRadioButtonSkin.this.getEllipsedText()) + 4;
                else
                    return model.getxPos() + model.getxTranslate() + model.getWidth() / 2
                            - GuiRadioButtonSkin.this.fill.getWidth() / 2 + 1;
            }
        });
        this.fill.getyPosProperty().bind(BaseExpression.biCombine(model.getyPosProperty(),
                model.getyTranslateProperty(), (yPos, yTranslate) -> yPos + yTranslate + 2));
        this.fill.getWidthProperty().bind(this.fill.getHeightProperty());
        this.fill.getHeightProperty().bind(BaseExpression.transform(model.getHeightProperty(), height -> height - 4));
    }

    @Override
    public void render(final EGuiRenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);

        this.box.renderNode(renderer, pass, mouseX, mouseY);

        if (this.getModel().isSelected())
            this.fill.renderNode(renderer, pass, mouseX, mouseY);
    }
}