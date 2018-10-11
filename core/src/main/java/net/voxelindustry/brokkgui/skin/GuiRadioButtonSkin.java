package net.voxelindustry.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.binding.BaseExpression;
import net.voxelindustry.brokkgui.behavior.GuiButtonBehavior;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.element.GuiRadioButton;

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
                oldValue.getxPosProperty().unbind();
                oldValue.getyPosProperty().unbind();
            }
            if (newValue != null)
                this.bindButton(newValue);
        });

        this.bindButton(this.getModel().getButtonNode());
    }

    private void bindButton(GuiNode node)
    {
        this.getModel().addChild(node);

        node.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getButtonSideProperty(),
                        getModel().getxPosProperty(),
                        getModel().getxTranslateProperty(),
                        getModel().getLabel().getWidthProperty(),
                        getModel().getWidthProperty(),
                        node.getWidthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getButtonSide() == RectSide.LEFT)
                    return getModel().getxPos() + getModel().getxTranslate();
                if (getModel().getButtonSide() == RectSide.RIGHT)
                    return getModel().getxPos() + getModel().getxTranslate() + getModel().getWidth()
                            - node.getWidth();
                return getModel().getxPos() + getModel().getxTranslate() + getModel().getWidth() / 2
                        - node.getWidth() / 2;
            }
        });

        node.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getButtonSideProperty(),
                        getModel().getyPosProperty(),
                        getModel().getyTranslateProperty(),
                        getModel().getLabel().getHeightProperty(),
                        getModel().getHeightProperty(),
                        node.getHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getButtonSide() == RectSide.UP)
                    return getModel().getyPos() + getModel().getyTranslate();
                if (getModel().getButtonSide() == RectSide.DOWN)
                    return getModel().getyPos() + getModel().getyTranslate() + getModel().getHeight()
                            - getModel().getLabel().getHeight();
                return getModel().getyPos() + getModel().getyTranslate() + getModel().getHeight() / 2
                        - node.getHeight() / 2;
            }
        });
    }

    @Override
    protected void bindLabel()
    {
        if (!getModel().expandToLabel())
            getModel().getLabel().getWidthProperty().bind(getModel().getWidthProperty());
        else
        {
            getModel().getLabel().setExpandToText(true);
            getModel().getWidthProperty().bind(BaseExpression.biCombine(getModel().getButtonNode().getWidthProperty(),
                    getModel().getLabel().getWidthProperty(),
                    (buttonWidth, labelWidth) -> buttonWidth + labelWidth));
        }

        getModel().getLabel().getHeightProperty().bind(getModel().getHeightProperty());

        getModel().getLabel().getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getButtonSideProperty(),
                        getModel().getxPosProperty(),
                        getModel().getxTranslateProperty(),
                        getModel().getLabel().getWidthProperty(),
                        getModel().getWidthProperty(),
                        getModel().getButtonNode().getWidthProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getButtonSide() == RectSide.LEFT)
                    return getModel().getxPos() + getModel().getxTranslate() + getModel().getButtonNode().getWidth();
                if (getModel().getButtonSide() == RectSide.RIGHT)
                    return getModel().getxPos() + getModel().getxTranslate();
                return getModel().getxPos() + getModel().getxTranslate() + getModel().getWidth() / 2
                        - getModel().getLabel().getWidth() / 2;
            }
        });

        getModel().getLabel().getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getButtonSideProperty(),
                        getModel().getyPosProperty(),
                        getModel().getyTranslateProperty(),
                        getModel().getLabel().getHeightProperty(),
                        getModel().getHeightProperty(),
                        getModel().getButtonNode().getHeightProperty());
            }

            @Override
            public Float computeValue()
            {
                if (getModel().getButtonSide() == RectSide.UP)
                    return getModel().getyPos() + getModel().getyTranslate() + getModel().getButtonNode().getHeight();
                if (getModel().getButtonSide() == RectSide.DOWN)
                    return getModel().getyPos() + getModel().getyTranslate();
                return getModel().getyPos() + getModel().getyTranslate() + getModel().getHeight() / 2
                        - getModel().getLabel().getHeight() / 2;
            }
        });

        getModel().getExpandToLabelProperty().addListener(obs ->
        {
            getModel().getLabel().getWidthProperty().unbind();
            getModel().getLabel().setExpandToText(true);
            getModel().getWidthProperty().bind(BaseExpression.biCombine(getModel().getButtonNode().getWidthProperty(),
                    getModel().getLabel().getWidthProperty(),
                    (buttonWidth, labelWidth) -> buttonWidth + labelWidth));
        });
    }
}