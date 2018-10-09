package org.yggard.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.behavior.GuiBehaviorBase;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.control.GuiLabeled;
import org.yggard.brokkgui.data.RectSide;
import org.yggard.brokkgui.shape.Text;

/**
 * @param <C> the labeled gui control this skin must render
 * @param <B> an overly simplified behaviour only here for architecture
 *            purpose
 * @author Ourten
 */
public class GuiLabeledSkinBase<C extends GuiLabeled, B extends GuiBehaviorBase<C>> extends GuiBehaviorSkinBase<C, B>
{
    private final Text text;

    private final BaseProperty<String> ellipsedTextProperty;

    public GuiLabeledSkinBase(final C model, final B behaviour)
    {
        super(model, behaviour);

        this.text = new Text(model.getText());

        this.ellipsedTextProperty = new BaseProperty<>("", "ellipsedTextProperty");

        // Bindings
        this.bindEllipsed();

        this.text.getStyleClass().add("text");

        this.getModel().getIconProperty().addListener((obs, oldValue, newValue) ->
        {
            if(oldValue != null)
                getModel().removeChild(oldValue);
            if (newValue != null)
            {
                getModel().getStyleClass().add("icon");
                this.bindIcon(newValue);
            }
        });
        if (model.getIconProperty().isPresent())
            this.bindIcon(model.getIcon());

        this.text.getTextProperty().bind(this.ellipsedTextProperty);
        this.text.getzLevelProperty().bind(model.getzLevelProperty());

        this.text.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getTextAlignmentProperty(),
                        model.getxPosProperty(),
                        model.getxTranslateProperty(),
                        model.getWidthProperty(),
                        model.getIconProperty(),
                        model.getIconSideProperty(),
                        model.getIconPaddingProperty(),
                        model.getTextPaddingProperty(),
                        getEllipsedTextProperty());
            }

            @Override
            public Float computeValue()
            {
                float iconWidth = 0;
                if (model.getIconProperty().isPresent() && model.getIconSide().isHorizontal())
                    iconWidth = model.getIcon().getWidth() + model.getIconPadding();

                if (model.getTextAlignment().isLeft())
                    return model.getxPos() + model.getxTranslate() + model.getTextPadding().getLeft()
                            + (model.getIconSide() == RectSide.LEFT ? iconWidth : 0);
                else if (model.getTextAlignment().isRight())
                    return model.getxPos() + model.getxTranslate()
                            + model.getWidth()
                            - model.getTextPadding().getRight()
                            - BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getEllipsedText())
                            - (model.getIconSide() == RectSide.RIGHT ? iconWidth : 0);
                else
                    return model.getxPos() + model.getxTranslate()
                            + model.getTextPadding().getLeft()
                            + (model.getIconSide() == RectSide.LEFT ? iconWidth : 0)
                            + getAvailableTextWidth() / 2
                            - BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getEllipsedText()) / 2
                            - model.getTextPadding().getRight();
            }
        });
        this.text.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getTextAlignmentProperty(),
                        model.getyPosProperty(),
                        model.getyTranslateProperty(),
                        model.getHeightProperty(),
                        model.getIconProperty(),
                        model.getIconSideProperty(),
                        model.getIconPaddingProperty(),
                        model.getTextPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                float iconHeight = 0;
                if (model.getIconProperty().isPresent() && model.getIconSide().isVertical())
                    iconHeight = model.getIcon().getHeight() + model.getIconPadding();

                if (model.getTextAlignment().isUp())
                    return model.getyPos() + model.getyTranslate() + model.getTextPadding().getTop()
                            + (model.getIconSide() == RectSide.UP ? iconHeight : 0);
                else if (model.getTextAlignment().isDown())
                    return model.getyPos() + model.getyTranslate()
                            + model.getHeight()
                            - model.getTextPadding().getBottom()
                            - BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight()
                            - (model.getIconSide() == RectSide.DOWN ? iconHeight : 0);
                else
                    return model.getyPos() + model.getyTranslate() + model.getHeight() / 2
                            + (model.getIconSide() == RectSide.UP ? iconHeight :
                            (model.getIconSide() == RectSide.DOWN ? -iconHeight : 0)) / 2
                            - BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight() / 2
                            + model.getTextPadding().getTop()
                            - model.getTextPadding().getBottom();
            }
        });

        this.text.getWidthProperty().bind(BaseExpression.transform(this.getEllipsedTextProperty(),
                BrokkGuiPlatform.getInstance().getGuiHelper()::getStringWidth));
        this.text.setHeight(BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight());

        getModel().addChild(text);
    }

    /**
     * @return the Text shape used to represent the control
     */
    public Text getText()
    {
        return this.text;
    }

    public BaseProperty<String> getEllipsedTextProperty()
    {
        return this.ellipsedTextProperty;
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
                super.bind(getModel().getTextProperty(),
                        getModel().getExpandToTextProperty(),
                        getModel().getWidthProperty(),
                        getModel().getEllipsisProperty(),
                        getModel().getTextPaddingProperty(),
                        getModel().getIconPaddingProperty(),
                        getModel().getIconSideProperty(),
                        getModel().getIconProperty());
            }

            @Override
            public String computeValue()
            {
                if (!getModel().expandToText() && getModel().getWidth() < getExpandedWidth())
                {
                    String trimmed = BrokkGuiPlatform.getInstance().getGuiHelper().trimStringToPixelWidth(
                            getModel().getText(), (int) (getAvailableTextWidth()));

                    if (trimmed.length() < getModel().getEllipsis().length())
                        return "";
                    trimmed = trimmed.substring(0, trimmed.length() - getModel().getEllipsis().length());
                    return trimmed + getModel().getEllipsis();
                }
                return getModel().getText();
            }
        });
    }

    private float getExpandedWidth()
    {
        if (getModel().getIconProperty().isPresent())
        {
            if (getModel().getIconSide().isHorizontal())
                return BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getModel().getText())
                        + getModel().getTextPadding().getLeft() + getModel().getTextPadding().getRight()
                        + getModel().getIcon().getWidth() + getModel().getIconPadding();
            else
                return Math.max(BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getModel().getText()),
                        getModel().getIcon().getWidth())
                        + getModel().getTextPadding().getLeft() + getModel().getTextPadding().getRight();
        }
        return BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getModel().getText())
                + getModel().getTextPadding().getLeft() + getModel().getTextPadding().getRight();
    }

    private float getAvailableTextWidth()
    {
        if (getModel().getIconProperty().isPresent() && getModel().getIconSide().isHorizontal())
        {
            return getModel().getWidth()
                    - getModel().getTextPadding().getLeft() - getModel().getTextPadding().getRight()
                    - getModel().getIcon().getWidth() - getModel().getIconPadding();
        }
        return getModel().getWidth()
                - getModel().getTextPadding().getLeft() - getModel().getTextPadding().getRight();
    }

    private void bindIcon(GuiNode icon)
    {
        this.getModel().addChild(icon);

        icon.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getIconSideProperty(),
                        getModel().getxPosProperty(),
                        getModel().getxTranslateProperty(),
                        getModel().getTextPaddingProperty(),
                        getModel().getWidthProperty(),
                        icon.getWidthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getIconSide() == RectSide.LEFT)
                    return getModel().getxPos() + getModel().getxTranslate()
                            + getModel().getTextPadding().getLeft();
                if (getModel().getIconSide() == RectSide.RIGHT)
                    return getModel().getxPos() + getModel().getxTranslate() + getModel().getWidth()
                            - getModel().getTextPadding().getRight()
                            - icon.getWidth();
                return getModel().getxPos() + getModel().getxTranslate()
                        + getModel().getWidth() / 2 - icon.getWidth() / 2
                        + getModel().getTextPadding().getLeft()
                        - getModel().getTextPadding().getRight();
            }
        });

        icon.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getIconSideProperty(),
                        getModel().getyPosProperty(),
                        getModel().getyTranslateProperty(),
                        getModel().getTextPaddingProperty(),
                        getModel().getHeightProperty(),
                        icon.getHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getIconSide() == RectSide.UP)
                    return getModel().getyPos() + getModel().getyTranslate()
                            + getModel().getTextPadding().getTop();
                if (getModel().getIconSide() == RectSide.DOWN)
                    return getModel().getyPos() + getModel().getyTranslate() + getModel().getHeight()
                            - getModel().getTextPadding().getBottom()
                            - icon.getHeight();
                return getModel().getyPos() + getModel().getyTranslate()
                        + getModel().getHeight() / 2 - icon.getHeight() / 2
                        + getModel().getTextPadding().getTop()
                        - getModel().getTextPadding().getBottom();
            }
        });
    }
}