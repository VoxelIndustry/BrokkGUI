package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.behavior.GuiBehaviorBase;
import org.yggard.brokkgui.control.GuiLabeled;
import org.yggard.brokkgui.data.EHAlignment;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.shape.Text;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.binding.BaseExpression;
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
    private final Text                      text;

    private final BaseProperty<Float>       textPaddingProperty;
    private final BaseProperty<EHAlignment> textPaddingAlignmentProperty;
    private final BaseProperty<String>      ellipsedTextProperty;

    public GuiLabeledSkinBase(final C model, final B behaviour)
    {
        super(model, behaviour);

        this.text = new Text(model.getText());

        this.textPaddingProperty = new BaseProperty<>(0f, "textPaddingProperty");
        this.textPaddingAlignmentProperty = new BaseProperty<>(EHAlignment.CENTER, "textPaddingAlignmentProperty");

        this.ellipsedTextProperty = new BaseProperty<>("", "ellipsedTextProperty");

        // Bindings

        this.text.getColorProperty().bind(model.getTextColorProperty());

        this.text.getTextProperty().bind(this.ellipsedTextProperty);

        this.text.getzLevelProperty().bind(model.getzLevelProperty());

        this.text.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getTextAlignmentProperty(), model.getxPosProperty(), model.getxTranslateProperty(),
                        model.getWidthProperty(), GuiLabeledSkinBase.this.ellipsedTextProperty,
                        GuiLabeledSkinBase.this.textPaddingProperty,
                        GuiLabeledSkinBase.this.textPaddingAlignmentProperty);
            }

            @Override
            public Float computeValue()
            {
                final float padding = GuiLabeledSkinBase.this.getTextPaddingAlignment() == EHAlignment.LEFT
                        ? -GuiLabeledSkinBase.this.getTextPadding() + 2
                        : GuiLabeledSkinBase.this.getTextPaddingAlignment() == EHAlignment.RIGHT
                                ? GuiLabeledSkinBase.this.getTextPadding() : 0;

                if (model.getTextAlignment().isLeft())
                    return model.getxPos() + model.getxTranslate() + padding;
                else if (model.getTextAlignment().isRight())
                    return model.getxPos() + model.getxTranslate() + model.getWidth() - BrokkGuiPlatform.getInstance()
                            .getGuiHelper().getStringWidth(GuiLabeledSkinBase.this.ellipsedTextProperty.getValue())
                            + padding;
                else
                    return model.getxPos() + model.getxTranslate() + model.getWidth() / 2 + padding / 2;
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

        this.text.getWidthProperty().bind(BaseExpression.biCombine(model.getWidthProperty(), this.textPaddingProperty,
                (width, padding) -> width - padding));
        this.text.setHeight(BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight());

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

    public BaseProperty<Float> getTextPaddingProperty()
    {
        return this.textPaddingProperty;
    }

    public BaseProperty<EHAlignment> getTextPaddingAlignmentProperty()
    {
        return this.textPaddingAlignmentProperty;
    }

    public BaseProperty<String> getEllipsedTextProperty()
    {
        return this.ellipsedTextProperty;
    }

    public float getTextPadding()
    {
        return this.getTextPaddingProperty().getValue();
    }

    public EHAlignment getTextPaddingAlignment()
    {
        return this.getTextPaddingAlignmentProperty().getValue();
    }

    public String getEllipsedText()
    {
        return this.ellipsedTextProperty.getValue();
    }

    private void bindEllipsed()
    {
        this.ellipsedTextProperty.bind(new BaseBinding<String>()
        {
            {
                super.bind(GuiLabeledSkinBase.this.getModel().getTextProperty(),
                        GuiLabeledSkinBase.this.getModel().getExpandToTextProperty(),
                        GuiLabeledSkinBase.this.getModel().getWidthProperty(),
                        GuiLabeledSkinBase.this.getModel().getEllipsisProperty(),
                        GuiLabeledSkinBase.this.textPaddingProperty);
            }

            @Override
            public String computeValue()
            {
                if (!GuiLabeledSkinBase.this.getModel().expandToText() && GuiLabeledSkinBase.this.getModel().getWidth()
                        - GuiLabeledSkinBase.this.textPaddingProperty.getValue() < BrokkGuiPlatform.getInstance()
                                .getGuiHelper().getStringWidth(GuiLabeledSkinBase.this.getModel().getText()))
                {
                    String trimmed = BrokkGuiPlatform.getInstance().getGuiHelper().trimStringToPixelWidth(
                            GuiLabeledSkinBase.this.getModel().getText(),
                            (int) (GuiLabeledSkinBase.this.getModel().getWidth()
                                    - GuiLabeledSkinBase.this.textPaddingProperty.getValue()));

                    trimmed = trimmed.substring(0,
                            trimmed.length() - GuiLabeledSkinBase.this.getModel().getEllipsis().length());
                    return trimmed + GuiLabeledSkinBase.this.getModel().getEllipsis();
                }
                return GuiLabeledSkinBase.this.getModel().getText();
            }
        });
    }
}