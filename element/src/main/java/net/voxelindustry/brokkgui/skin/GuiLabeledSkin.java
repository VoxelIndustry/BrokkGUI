package net.voxelindustry.brokkgui.skin;

import fr.ourten.teabeans.binding.Binding;
import fr.ourten.teabeans.binding.Expression;
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
    private final Property<String>    ellipsedTextProperty;
    private       Expression<RectBox> paddingForIconBinding;

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
                    bindText(oldValue);
            }
            if (newValue != null)
            {
                getModel().style().styleClass().add("icon");
                bindTextWithIcon(oldValue, newValue);
                bindIcon(newValue);
            }
        });
        if (model.getIconProperty().isPresent())
        {
            bindTextWithIcon(null, model.getIcon());
            bindIcon(model.getIcon());
        }
        else
            bindText(null);
    }

    public Property<String> getEllipsedTextProperty()
    {
        return ellipsedTextProperty;
    }

    public String getEllipsedText()
    {
        return ellipsedTextProperty.getValue();
    }

    private void bindText(GuiElement previousIcon)
    {
        if (paddingForIconBinding == null)
        {
            paddingForIconBinding = new Expression<>(() -> RectBox.EMPTY,
                    getModel().getIconSideProperty(),
                    getModel().getIconPaddingProperty());
            text.addTextPaddingProperty(paddingForIconBinding);
            return;
        }

        paddingForIconBinding.setClosure(() -> RectBox.EMPTY);
        if (previousIcon != null)
        {
            paddingForIconBinding.unbind(previousIcon.transform().widthProperty());
            paddingForIconBinding.unbind(previousIcon.transform().heightProperty());
        }
    }

    private void bindTextWithIcon(GuiElement previousIcon, GuiElement icon)
    {
        if (paddingForIconBinding == null)
        {
            paddingForIconBinding = new Expression<>(this::getTextPaddingForIcon,
                    getModel().getIconSideProperty(),
                    getModel().getIconPaddingProperty(),
                    icon.transform().widthProperty(),
                    icon.transform().heightProperty());
            text.addTextPaddingProperty(paddingForIconBinding);
            return;
        }

        if (previousIcon != null)
        {
            paddingForIconBinding.unbind(previousIcon.transform().widthProperty());
            paddingForIconBinding.unbind(previousIcon.transform().heightProperty());
        }

        paddingForIconBinding.bind(icon.transform().widthProperty());
        paddingForIconBinding.bind(icon.transform().heightProperty());

        paddingForIconBinding.setClosure(this::getTextPaddingForIcon);
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
                text.updateTextSettings();
                if (!getModel().expandToText() && getModel().width() < getExpandedWidth())
                {
                    String trimmed = BrokkGuiPlatform.getInstance().getTextHelper().trimStringToPixelWidth(
                            getModel().getText(), (int) (getAvailableTextWidth()), text.textSettings());

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
                return BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth(getModel().getText(), text.textSettings())
                        + text.computedTextPadding().getHorizontal()
                        + getModel().getIcon().width() + getModel().getIconPadding();
            else
                return Math.max(BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth(getModel().getText(), text.textSettings()),
                        getModel().getIcon().width())
                        + text.computedTextPadding().getHorizontal();
        }
        return BrokkGuiPlatform.getInstance().getTextHelper().getStringWidth(getModel().getText(), text.textSettings())
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
                        paddingForIconBinding,
                        transform().widthProperty(),
                        icon.transform().widthProperty());
            }

            @Override
            public Float computeValue()
            {
                RectBox paddingForIcon = paddingForIconBinding.getValue();

                if (getModel().getIconSide() == RectSide.LEFT)
                {
                    return transform().leftPos()
                            + text.computedTextPadding().getLeft() - paddingForIcon.getLeft();
                }
                if (getModel().getIconSide() == RectSide.RIGHT)
                    return transform().rightPos()
                            - text.computedTextPadding().getRight() + paddingForIcon.getRight()
                            - icon.width();
                return transform().leftPos()
                        + getModel().width() / 2 - icon.width() / 2
                        + text.computedTextPadding().getLeft() - paddingForIcon.getLeft()
                        - text.computedTextPadding().getRight() + paddingForIcon.getRight();
            }
        });

        icon.transform().yPosProperty().bindProperty(new Binding<Float>()
        {
            {
                super.bind(getModel().getIconSideProperty(),
                        transform().yPosProperty(),
                        transform().yTranslateProperty(),
                        text.computedTextPaddingValue(),
                        paddingForIconBinding,
                        transform().heightProperty(),
                        icon.transform().heightProperty());
            }

            @Override
            public Float computeValue()
            {
                RectBox paddingForIcon = paddingForIconBinding.getValue();

                if (getModel().getIconSide() == RectSide.UP)
                    return transform().topPos()
                            + text.computedTextPadding().getTop() - paddingForIcon.getTop();
                if (getModel().getIconSide() == RectSide.DOWN)
                    return transform().bottomPos()
                            - text.computedTextPadding().getBottom() + paddingForIcon.getBottom()
                            - icon.height();
                return transform().topPos()
                        + getModel().height() / 2 - icon.height() / 2
                        + text.computedTextPadding().getTop() - paddingForIcon.getTop()
                        - text.computedTextPadding().getBottom() + paddingForIcon.getBottom();
            }
        });
    }

    private RectBox getTextPaddingForIcon()
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
}