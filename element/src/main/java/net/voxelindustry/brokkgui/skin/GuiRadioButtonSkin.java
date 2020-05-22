package net.voxelindustry.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.binding.BaseExpression;
import net.voxelindustry.brokkgui.behavior.GuiButtonBehavior;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.element.input.GuiRadioButton;

public class GuiRadioButtonSkin extends GuiButtonSkin<GuiRadioButton, GuiButtonBehavior<GuiRadioButton>>
{
    public GuiRadioButtonSkin(final GuiRadioButton model, final GuiButtonBehavior<GuiRadioButton> behaviour)
    {
        super(model, behaviour);

        this.getModel().getButtonNodeProperty().addListener((obs, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                this.getModel().removeChild(oldValue);
                oldValue.transform().xPosProperty().unbind();
                oldValue.transform().yPosProperty().unbind();
            }
            if (newValue != null)
                this.bindButton(newValue);
        });

        this.bindButton(this.getModel().getButtonNode());
    }

    private void bindButton(GuiElement node)
    {
        this.getModel().addChild(node);

        node.transform().xPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getButtonSideProperty(),
                        transform().xPosProperty(),
                        transform().xTranslateProperty(),
                        getModel().getLabel().transform().widthProperty(),
                        transform().widthProperty(),
                        node.transform().widthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getButtonSide() == RectSide.LEFT)
                    return getModel().getLeftPos();
                if (getModel().getButtonSide() == RectSide.RIGHT)
                    return getModel().getRightPos() - node.width();
                return getModel().getLeftPos() + getModel().width() / 2
                        - node.width() / 2;
            }
        });

        node.transform().yPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getButtonSideProperty(),
                        transform().yPosProperty(),
                        transform().yTranslateProperty(),
                        getModel().getLabel().transform().heightProperty(),
                        transform().heightProperty(),
                        node.transform().heightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getButtonSide() == RectSide.UP)
                    return getModel().getTopPos();
                if (getModel().getButtonSide() == RectSide.DOWN)
                    return getModel().getBottomPos() - getModel().getLabel().height();
                return getModel().getTopPos() + getModel().height() / 2 - node.height() / 2;
            }
        });
    }

    @Override
    protected void bindLabel()
    {
        if (!getModel().expandToLabel())
            getModel().getLabel().transform().widthProperty().bind(transform().widthProperty());
        else
        {
            getModel().getLabel().setExpandToText(true);
            transform().widthProperty().bind(BaseExpression.biCombine(
                    getModel().getButtonNode().transform().widthProperty(),
                    getModel().getLabel().transform().widthProperty(),
                    Float::sum));
        }

        getModel().getLabel().transform().heightProperty().bind(transform().heightProperty());

        getModel().getLabel().transform().xPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getButtonSideProperty(),
                        transform().xPosProperty(),
                        transform().xTranslateProperty(),
                        getModel().getLabel().transform().widthProperty(),
                        transform().widthProperty(),
                        getModel().getButtonNode().transform().widthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getButtonSide() == RectSide.LEFT)
                    return getModel().getLeftPos() + getModel().getButtonNode().width();
                if (getModel().getButtonSide() == RectSide.RIGHT)
                    return getModel().getLeftPos();
                return getModel().getLeftPos() + getModel().width() / 2
                        - getModel().getLabel().width() / 2;
            }
        });

        getModel().getLabel().transform().yPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getButtonSideProperty(),
                        transform().yPosProperty(),
                        transform().yTranslateProperty(),
                        getModel().getLabel().transform().heightProperty(),
                        transform().heightProperty(),
                        getModel().getButtonNode().transform().heightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getButtonSide() == RectSide.UP)
                    return getModel().getTopPos() + getModel().getButtonNode().height();
                if (getModel().getButtonSide() == RectSide.DOWN)
                    return getModel().getTopPos();
                return getModel().getTopPos() + getModel().height() / 2
                        - getModel().getLabel().height() / 2;
            }
        });

        getModel().getExpandToLabelProperty().addListener(obs ->
        {
            getModel().getLabel().transform().widthProperty().unbind();
            getModel().getLabel().setExpandToText(true);
            transform().widthProperty().bind(BaseExpression.biCombine(
                    getModel().getButtonNode().transform().widthProperty(),
                    getModel().getLabel().transform().widthProperty(),
                    Float::sum));
        });
    }
}
