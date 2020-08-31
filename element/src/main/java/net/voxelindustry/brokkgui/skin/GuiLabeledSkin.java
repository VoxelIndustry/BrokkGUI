package net.voxelindustry.brokkgui.skin;

import fr.ourten.teabeans.binding.Binding;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.behavior.GuiBehaviorBase;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiLabeled;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.shape.TextComponent;

/**
 * @param <C> the labeled gui control this skin must render
 * @param <B> an overly simplified behaviour only here for architecture
 *            purpose
 * @author Ourten
 */
public class GuiLabeledSkin<C extends GuiLabeled, B extends GuiBehaviorBase<C>> extends GuiBehaviorSkinBase<C, B>
{
    private final Property<String> ellipsedTextProperty;
    private       Binding<RectBox> paddingBinding;

    private final TextComponent text;

    public GuiLabeledSkin(C model, B behaviour)
    {
        super(model, behaviour);

        text = model.textComponent();

        ellipsedTextProperty = new Property<>("");

        // Bindings
        //   bindEllipsed();

        style().styleClass().add("text");

        getModel().getIconProperty().addListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                getModel().removeChild(oldValue);
                if (newValue == null)
                    bindText();
            }
            if (newValue != null)
            {
                getModel().style().styleClass().add("icon");
                bindIcon(newValue);
                bindTextWithIcon(newValue);
            }
        });
        if (model.getIconProperty().isPresent())
        {
            bindIcon(model.getIcon());
            bindTextWithIcon(model.getIcon());
        }
        else
            bindText();
    }

    public Property<String> getEllipsedTextProperty()
    {
        return ellipsedTextProperty;
    }

    public String getEllipsedText()
    {
        return ellipsedTextProperty.getValue();
    }

    private void bindText()
    {
        text.removeTextPaddingProperty(paddingBinding);
        if (paddingBinding != null)
        {
            paddingBinding.unbindAll();
            paddingBinding = null;
        }
    }

    private void bindTextWithIcon(GuiElement icon)
    {
        Binding<RectBox> previousBinding = paddingBinding;
        if (paddingBinding != null)
            paddingBinding.unbindAll();

        paddingBinding = new Binding<RectBox>()
        {
            {
                bind(
                        getModel().getIconPaddingProperty(),
                        getModel().getIconSideProperty(),
                        icon.transform().widthProperty(),
                        icon.transform().heightProperty()
                );
            }

            @Override
            public RectBox computeValue()
            {
                float left = 0;
                float top = 0;
                float right = 0;
                float bottom = 0;

                if (getModel().getIconSide() == RectSide.LEFT)
                    left += getModel().getIcon().width() + getModel().getIconPadding();
                else if (getModel().getIconSide() == RectSide.RIGHT)
                    right += getModel().getIcon().width() + getModel().getIconPadding();
                else if (getModel().getIconSide() == RectSide.UP)
                    top += getModel().getIcon().height() + getModel().getIconPadding();
                else if (getModel().getIconSide() == RectSide.DOWN)
                    bottom += getModel().getIcon().height() + getModel().getIconPadding();

                return RectBox.build()
                        .left(left)
                        .right(right)
                        .top(top)
                        .bottom(bottom)
                        .create();
            }
        };
        text.replaceTextPaddingProperty(previousBinding, paddingBinding);
    }

    private void bindEllipsed()
    {
        ellipsedTextProperty.bindProperty(new Binding<String>()
        {
            {
                super.bind(getModel().getTextProperty(),
                        getModel().getExpandToTextProperty(),
                        transform().widthProperty(),
                        getModel().getEllipsisProperty(),
                        text.computedTextPaddingValue(),
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
                        + text.computedTextPadding().getHorizontal()
                        + getModel().getIcon().width() + getModel().getIconPadding();
            else
                return Math.max(BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getModel().getText()),
                        getModel().getIcon().width())
                        + text.computedTextPadding().getHorizontal();
        }
        return BrokkGuiPlatform.getInstance().getGuiHelper().getStringWidth(getModel().getText())
                + text.computedTextPadding().getHorizontal();
    }

    private float getAvailableTextWidth()
    {
        if (getModel().getIconProperty().isPresent() && getModel().getIconSide().isHorizontal())
        {
            return getModel().width()
                    - text.computedTextPadding().getHorizontal()
                    - getModel().getIcon().width() - getModel().getIconPadding();
        }
        return getModel().width()
                - text.computedTextPadding().getHorizontal();
    }

    private void bindIcon(GuiElement icon)
    {
        getModel().addChild(icon);

        icon.transform().xPosProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(getModel().getIconSideProperty(),
                        transform().xPosProperty(),
                        transform().xTranslateProperty(),
                        text.computedTextPaddingValue(),
                        transform().widthProperty(),
                        icon.transform().widthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getIconSide() == RectSide.LEFT)
                    return getModel().getLeftPos()
                            + text.computedTextPadding().getLeft();
                if (getModel().getIconSide() == RectSide.RIGHT)
                    return getModel().getRightPos()
                            - text.computedTextPadding().getRight()
                            - icon.width();
                return getModel().getLeftPos()
                        + getModel().width() / 2 - icon.width() / 2
                        + text.computedTextPadding().getLeft()
                        - text.computedTextPadding().getRight();
            }
        });

        icon.transform().yPosProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(getModel().getIconSideProperty(),
                        transform().yPosProperty(),
                        transform().yTranslateProperty(),
                        text.computedTextPaddingValue(),
                        transform().heightProperty(),
                        icon.transform().heightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getIconSide() == RectSide.UP)
                    return getModel().getTopPos()
                            + text.computedTextPadding().getTop();
                if (getModel().getIconSide() == RectSide.DOWN)
                    return getModel().getBottomPos()
                            - text.computedTextPadding().getBottom()
                            - icon.height();
                return getModel().getTopPos()
                        + getModel().height() / 2 - icon.height() / 2
                        + text.computedTextPadding().getTop()
                        - text.computedTextPadding().getBottom();
            }
        });
    }
}