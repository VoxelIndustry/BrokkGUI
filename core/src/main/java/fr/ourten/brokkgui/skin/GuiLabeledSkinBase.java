package fr.ourten.brokkgui.skin;

import fr.ourten.brokkgui.BrokkGuiPlatform;
import fr.ourten.brokkgui.behavior.GuiBehaviorBase;
import fr.ourten.brokkgui.control.GuiLabeled;
import fr.ourten.brokkgui.internal.IGuiRenderer;
import fr.ourten.brokkgui.paint.EGuiRenderPass;
import fr.ourten.brokkgui.shape.Text;
import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;

/**
 *
 * @author Ourten
 *
 * @param <C>
 *            the labeled gui control this skin must render
 * @param <B>
 *            an overly simplified behaviour only here for architecture
 *            cleanlyness purpose
 */
public class GuiLabeledSkinBase<C extends GuiLabeled, B extends GuiBehaviorBase<C>> extends GuiBehaviorSkinBase<C, B>
{
    private final Text                 text;

    private final BaseProperty<String> ellipsedTextProperty;

    public GuiLabeledSkinBase(final C model, final B behaviour)
    {
        super(model, behaviour);

        this.text = new Text(model.getText());

        this.ellipsedTextProperty = new BaseProperty<>("", "ellipsedTextProperty");

        // Bindings

        this.text.getColorProperty().bind(model.getTextColorProperty());

        this.text.getTextProperty().bind(this.ellipsedTextProperty);

        this.text.getzLevelProperty().bind(model.getzLevelProperty());

        this.text.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getTextAlignmentProperty(), model.getxPosProperty(), model.getxTranslateProperty(),
                        model.getWidthProperty(), GuiLabeledSkinBase.this.ellipsedTextProperty);
            }

            @Override
            public Float computeValue()
            {
                if (model.getTextAlignment().isLeft())
                    return model.getxPos() + model.getxTranslate();
                else if (model.getTextAlignment().isRight())
                    return model.getxPos() + model.getxTranslate() + model.getWidth() - BrokkGuiPlatform.getInstance()
                            .getGuiHelper().getStringWidth(GuiLabeledSkinBase.this.text.getText());
                else
                    return model.getxPos() + model.getxTranslate() + model.getWidth() / 2;
            }
        });
        this.text.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getTextAlignmentProperty(), model.getyPosProperty(), model.getyTranslateProperty(),
                        model.getHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (model.getTextAlignment().isUp())
                    return model.getyPos() + model.getyTranslate();
                else if (model.getTextAlignment().isDown())
                    return model.getyPos() + model.getyTranslate() + model.getHeight()
                            - BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight();
                else
                    return model.getyPos() + model.getyTranslate() + model.getHeight() / 2;
            }
        });

        this.bindEllipsed();
    }

    @Override
    public void render(final EGuiRenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);

        this.text.renderNode(renderer, pass, mouseX, mouseY);
    }

    /**
     * @return the Text shape used to represent the control
     */
    public Text getText()
    {
        return this.text;
    }

    private void bindEllipsed()
    {
        this.ellipsedTextProperty.bind(new BaseBinding<String>()
        {
            {
                super.bind(GuiLabeledSkinBase.this.getModel().getTextProperty(),
                        GuiLabeledSkinBase.this.getModel().getExpandToTextProperty(),
                        GuiLabeledSkinBase.this.getModel().getWidthProperty(),
                        GuiLabeledSkinBase.this.getModel().getEllipsisProperty());
            }

            @Override
            public String computeValue()
            {
                if (!GuiLabeledSkinBase.this.getModel().expandToText()
                        && GuiLabeledSkinBase.this.getModel().getWidth() < BrokkGuiPlatform.getInstance().getGuiHelper()
                                .getStringWidth(GuiLabeledSkinBase.this.getModel().getText()))
                {
                    String trimmed = BrokkGuiPlatform.getInstance().getGuiHelper().trimStringToPixelWidth(
                            GuiLabeledSkinBase.this.getModel().getText(),
                            (int) GuiLabeledSkinBase.this.getModel().getWidth());

                    trimmed = trimmed.substring(0,
                            trimmed.length() - GuiLabeledSkinBase.this.getModel().getEllipsis().length());
                    return trimmed + GuiLabeledSkinBase.this.getModel().getEllipsis();
                }
                return GuiLabeledSkinBase.this.getModel().getText();
            }
        });
    }
}