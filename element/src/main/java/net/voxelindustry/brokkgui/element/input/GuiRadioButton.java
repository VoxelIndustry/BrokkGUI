package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.behavior.GuiTogglableButtonBehavior;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.skin.GuiRadioButtonSkin;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

public class GuiRadioButton extends GuiToggleButton
{
    private final BaseProperty<RectSide> buttonSideProperty;
    private final BaseProperty<GuiNode>  buttonNodeProperty;

    public GuiRadioButton(String text)
    {
        super("radio-button", text);

        this.buttonSideProperty = new BaseProperty<>(RectSide.LEFT, "buttonSideProperty");
        this.buttonNodeProperty = new BaseProperty<>(new RadioButtonContent(this), "buttonNodeProperty");

        this.setExpandToLabel(true);
        this.getLabel().setTextPadding(new RectBox(0, 2, 0, 0));
    }

    public GuiRadioButton()
    {
        this("");
    }

    public BaseProperty<RectSide> getButtonSideProperty()
    {
        return buttonSideProperty;
    }

    public BaseProperty<GuiNode> getButtonNodeProperty()
    {
        return buttonNodeProperty;
    }

    public void setButtonSide(RectSide side)
    {
        this.getButtonSideProperty().setValue(side);
    }

    public RectSide getButtonSide()
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