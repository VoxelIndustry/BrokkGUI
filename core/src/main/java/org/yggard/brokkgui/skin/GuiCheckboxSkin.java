package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.behavior.GuiButtonBehavior;
import org.yggard.brokkgui.data.EHAlignment;
import org.yggard.brokkgui.element.GuiCheckbox;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.shape.Rectangle;
import org.yggard.brokkgui.shape.Text;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.binding.BaseExpression;

public class GuiCheckboxSkin extends GuiLabeledSkinBase<GuiCheckbox, GuiButtonBehavior<GuiCheckbox>>
{
    private final Rectangle box;
    private final Text      fill;

    public GuiCheckboxSkin(final GuiCheckbox model, final GuiButtonBehavior<GuiCheckbox> behaviour)
    {
        super(model, behaviour);

        this.getTextPaddingProperty().bind(BaseExpression.biCombine(model.getHeightProperty(),
                model.getLabelPaddingProperty(), (height, padding) -> height + padding));
        this.getTextPaddingAlignmentProperty().bind(model.getLabelAlignmentProperty());

        this.box = new Rectangle();
        this.fill = new Text("✔");

        this.box.setFill(Color.ALPHA);
        this.box.setLineWeight(1);
        this.box.setLineColor(Color.BLACK);

        this.fill.setFill(Color.GRAY);

        this.box.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getWidthProperty(), model.getLabelAlignmentProperty(), model.getxPosProperty(),
                        model.getxTranslateProperty(), GuiCheckboxSkin.this.getTextPaddingProperty(),
                        GuiCheckboxSkin.this.box.getWidthProperty(), GuiCheckboxSkin.this.getEllipsedTextProperty());
            }

            @Override
            public Float computeValue()
            {
                if (model.getLabelAlignment().equals(EHAlignment.RIGHT))
                    return model.getxPos() + model.getxTranslate();
                else if (model.getLabelAlignment().equals(EHAlignment.LEFT))
                    return model.getxPos() + model.getxTranslate() + BrokkGuiPlatform.getInstance().getGuiHelper()
                            .getStringWidth(GuiCheckboxSkin.this.getEllipsedText()) + 2;
                else
                    return model.getxPos() + model.getxTranslate() + model.getWidth() / 2
                            - GuiCheckboxSkin.this.box.getWidth() / 2;
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
                        model.getxTranslateProperty(), GuiCheckboxSkin.this.getTextPaddingProperty(),
                        GuiCheckboxSkin.this.fill.getWidthProperty(), GuiCheckboxSkin.this.getEllipsedTextProperty());
            }

            @Override
            public Float computeValue()
            {
                if (model.getLabelAlignment().equals(EHAlignment.RIGHT))
                    return model.getxPos() + model.getxTranslate() + 1 + GuiCheckboxSkin.this.fill.getWidth() / 2;
                else if (model.getLabelAlignment().equals(EHAlignment.LEFT))
                    return model.getxPos() + model.getxTranslate()
                            + BrokkGuiPlatform.getInstance().getGuiHelper()
                                    .getStringWidth(GuiCheckboxSkin.this.getEllipsedText())
                            + 3 + GuiCheckboxSkin.this.fill.getWidth() / 2;
                else
                    return model.getxPos() + model.getxTranslate() + model.getWidth() / 2
                            - GuiCheckboxSkin.this.fill.getWidth() / 2 + GuiCheckboxSkin.this.fill.getWidth() / 2;
            }
        });
        this.fill.getyPosProperty()
                .bind(BaseExpression.biCombine(model.getyPosProperty(), model.getyTranslateProperty(),
                        (yPos, yTranslate) -> yPos + yTranslate
                                + BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight() / 2));
        this.fill.getWidthProperty().bind(BaseExpression.transform(this.fill.getTextProperty(),
                BrokkGuiPlatform.getInstance().getGuiHelper()::getStringWidth));
        this.fill.getHeightProperty().bind(this.fill.getWidthProperty());
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