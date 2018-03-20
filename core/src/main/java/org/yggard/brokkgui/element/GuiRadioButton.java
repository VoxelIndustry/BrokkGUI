package org.yggard.brokkgui.element;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.behavior.GuiTogglableButtonBehavior;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.control.GuiFather;
import org.yggard.brokkgui.control.GuiToggleButton;
import org.yggard.brokkgui.data.ESide;
import org.yggard.brokkgui.data.RectOffset;
import org.yggard.brokkgui.data.RelativeBindingHelper;
import org.yggard.brokkgui.shape.Rectangle;
import org.yggard.brokkgui.skin.GuiRadioButtonSkin;
import org.yggard.brokkgui.skin.GuiSkinBase;

public class GuiRadioButton extends GuiToggleButton
{
    private final BaseProperty<ESide>   buttonSideProperty;
    private final BaseProperty<GuiNode> buttonNodeProperty;

    public GuiRadioButton(String text)
    {
        super("radio-button", text);

        this.buttonSideProperty = new BaseProperty<>(ESide.LEFT, "buttonSideProperty");
        this.buttonNodeProperty = new BaseProperty<>(new RadioButtonContent(this), "buttonNodeProperty");

        this.setExpandToLabel(true);
        this.getLabel().setTextPadding(new RectOffset(0, 2, 0, 0));
    }

    public GuiRadioButton()
    {
        this("");
    }

    public BaseProperty<ESide> getButtonSideProperty()
    {
        return buttonSideProperty;
    }

    public BaseProperty<GuiNode> getButtonNodeProperty()
    {
        return buttonNodeProperty;
    }

    public void setButtonSide(ESide side)
    {
        this.getButtonSideProperty().setValue(side);
    }

    public ESide getButtonSide()
    {
        return this.getButtonSideProperty().getValue();
    }

    public void setButtonNode(GuiNode buttonNode)
    {
        this.getButtonNodeProperty().setValue(buttonNode);
    }

    public GuiNode getButtonNode()
    {
        return this.getButtonNodeProperty().getValue();
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiRadioButtonSkin(this, new GuiTogglableButtonBehavior<>(this));
    }

    public static class RadioButtonContent extends GuiFather
    {
        private Rectangle box;
        private Rectangle mark;

        public RadioButtonContent(GuiRadioButton parent)
        {
            super("radiobutton-content");

            this.setHeightRatio(1);
            this.getWidthProperty().bind(this.getHeightProperty());

            this.box = new Rectangle();
            this.mark = new Rectangle();

            this.box.getStyleClass().add("box");
            this.mark.getStyleClass().add("mark");

            this.addChild(this.box);
            RelativeBindingHelper.bindToCenter(this.box, this);

            this.addChild(this.mark);
            RelativeBindingHelper.bindToCenter(this.mark, this);

            this.box.getWidthProperty().bind(this.box.getHeightProperty());
            this.box.getHeightProperty().bind(this.getHeightProperty());

            this.mark.getWidthProperty().bind(this.mark.getHeightProperty());
            this.mark.getHeightProperty().bind(BaseExpression.transform(this.box.getHeightProperty(),
                    height -> height - 4));

            this.mark.getVisibleProperty().bind(parent.getSelectedProperty());
        }

        public GuiNode getBox()
        {
            return this.box;
        }

        public GuiNode getMark()
        {
            return this.mark;
        }
    }
}