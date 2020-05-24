package net.voxelindustry.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.behavior.GuiBehaviorBase;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiLabeled;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.shape.TextComponent;
import net.voxelindustry.brokkgui.style.StyleComponent;

/**
 * @param <C> the labeled gui control this skin must render
 * @param <B> an overly simplified behaviour only here for architecture
 *            purpose
 * @author Ourten
 */
public class GuiLabeledSkinBase<C extends GuiLabeled, B extends GuiBehaviorBase<C>> extends GuiBehaviorSkinBase<C, B>
{
    private final BaseProperty<String> ellipsedTextProperty;

    public GuiLabeledSkinBase(C model, B behaviour)
    {
        super(model, behaviour);

        TextComponent text = model.textComponent();

        ellipsedTextProperty = new BaseProperty<>("", "ellipsedTextProperty");

        // Bindings
        bindEllipsed();

        style().styleClass().add("text");

        getModel().getIconProperty().addListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
                getModel().removeChild(oldValue);
            if (newValue != null)
            {
                getModel().get(StyleComponent.class).styleClass().add("icon");
                bindIcon(newValue);
            }
        });
        if (model.getIconProperty().isPresent())
            bindIcon(model.getIcon());

        text.renderTextProperty().bind(ellipsedTextProperty);

/*        text.transform().xPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getTextAlignmentProperty(),
                        model.transform().xPosProperty(),
                        model.transform().xTranslateProperty(),
                        model.transform().widthProperty(),
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
                    iconWidth = model.getIcon().width() + model.getIconPadding();

                if (model.getTextAlignment().isLeft())
                    return model.transform().xPos() + model.transform().xTranslate() + model.getTextPadding().getLeft()
                            + (model.getIconSide() == RectSide.LEFT ? iconWidth : 0);
                else if (model.getTextAlignment().isRight())
                    return model.transform().xPos() + model.transform().xTranslate()
                            + model.width()
                            - model.getTextPadding().getRight()
                            - BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getEllipsedText())
                            - (model.getIconSide() == RectSide.RIGHT ? iconWidth : 0);
                else
                    return model.transform().xPos() + model.transform().xTranslate()
                            + model.getTextPadding().getLeft()
                            + (model.getIconSide() == RectSide.LEFT ? iconWidth : 0)
                            + getAvailableTextWidth() / 2
                            - BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getEllipsedText()) / 2
                            - model.getTextPadding().getRight();
            }
        });
        text.transform().yPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(model.getTextAlignmentProperty(),
                        model.transform().yPosProperty(),
                        model.transform().yTranslateProperty(),
                        model.transform().heightProperty(),
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
                    iconHeight = model.getIcon().height() + model.getIconPadding();

                if (model.getTextAlignment().isUp())
                    return model.transform().yPos() + model.transform().yTranslate() + model.getTextPadding().getTop()
                            + (model.getIconSide() == RectSide.UP ? iconHeight : 0);
                else if (model.getTextAlignment().isDown())
                    return model.transform().yPos() + model.transform().yTranslate()
                            + model.height()
                            - model.getTextPadding().getBottom()
                            - BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight()
                            - (model.getIconSide() == RectSide.DOWN ? iconHeight : 0);
                else
                    return model.transform().yPos() + model.transform().yTranslate() + model.height() / 2
                            + (model.getIconSide() == RectSide.UP ? iconHeight :
                            (model.getIconSide() == RectSide.DOWN ? -iconHeight : 0)) / 2
                            - BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight() / 2
                            + model.getTextPadding().getTop()
                            - model.getTextPadding().getBottom();
            }
        });*/

/*        text.transform().widthProperty().bind(BaseExpression.transform(getEllipsedTextProperty(),
                BrokkGuiPlatform.getInstance().getGuiHelper()::getStringWidth));*/
        text.transform().height(BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight());
    }

    public BaseProperty<String> getEllipsedTextProperty()
    {
        return ellipsedTextProperty;
    }

    public String getEllipsedText()
    {
        return ellipsedTextProperty.getValue();
    }

    private void bindEllipsed()
    {
        ellipsedTextProperty.bind(new BaseBinding<String>()
        {
            {
                super.bind(getModel().getTextProperty(),
                        getModel().getExpandToTextProperty(),
                        transform().widthProperty(),
                        getModel().getEllipsisProperty(),
                        getModel().getTextPaddingProperty(),
                        getModel().getIconPaddingProperty(),
                        getModel().getIconSideProperty(),
                        getModel().getIconProperty());
            }

            @Override
            public String computeValue()
            {
                if (!getModel().expandToText() && getModel().width() < getExpandedWidth())
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
                        + getModel().getIcon().width() + getModel().getIconPadding();
            else
                return Math.max(BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getModel().getText()),
                        getModel().getIcon().width())
                        + getModel().getTextPadding().getLeft() + getModel().getTextPadding().getRight();
        }
        return BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getModel().getText())
                + getModel().getTextPadding().getLeft() + getModel().getTextPadding().getRight();
    }

    private float getAvailableTextWidth()
    {
        if (getModel().getIconProperty().isPresent() && getModel().getIconSide().isHorizontal())
        {
            return getModel().width()
                    - getModel().getTextPadding().getLeft() - getModel().getTextPadding().getRight()
                    - getModel().getIcon().width() - getModel().getIconPadding();
        }
        return getModel().width()
                - getModel().getTextPadding().getLeft() - getModel().getTextPadding().getRight();
    }

    private void bindIcon(GuiElement icon)
    {
        getModel().addChild(icon);

        icon.transform().xPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getIconSideProperty(),
                        transform().xPosProperty(),
                        transform().xTranslateProperty(),
                        getModel().getTextPaddingProperty(),
                        transform().widthProperty(),
                        icon.transform().widthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getIconSide() == RectSide.LEFT)
                    return getModel().getLeftPos()
                            + getModel().getTextPadding().getLeft();
                if (getModel().getIconSide() == RectSide.RIGHT)
                    return getModel().getRightPos()
                            - getModel().getTextPadding().getRight()
                            - icon.width();
                return getModel().getLeftPos()
                        + getModel().width() / 2 - icon.width() / 2
                        + getModel().getTextPadding().getLeft()
                        - getModel().getTextPadding().getRight();
            }
        });

        icon.transform().yPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getIconSideProperty(),
                        transform().yPosProperty(),
                        transform().yTranslateProperty(),
                        getModel().getTextPaddingProperty(),
                        transform().heightProperty(),
                        icon.transform().heightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getIconSide() == RectSide.UP)
                    return getModel().getTopPos()
                            + getModel().getTextPadding().getTop();
                if (getModel().getIconSide() == RectSide.DOWN)
                    return getModel().getBottomPos()
                            - getModel().getTextPadding().getBottom()
                            - icon.height();
                return getModel().getTopPos()
                        + getModel().height() / 2 - icon.height() / 2
                        + getModel().getTextPadding().getTop()
                        - getModel().getTextPadding().getBottom();
            }
        });
    }
}