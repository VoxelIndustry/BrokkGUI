package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.behavior.GuiTogglableButtonBehavior;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.skin.GuiRadioButtonSkin;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;
import net.voxelindustry.brokkgui.style.StyleComponent;

public class GuiRadioButton extends GuiToggleButton
{
    private final BaseProperty<RectSide>   buttonSideProperty;
    private final BaseProperty<GuiElement> buttonNodeProperty;

    public GuiRadioButton(String text)
    {
        super(text);

        buttonSideProperty = new BaseProperty<>(RectSide.LEFT, "buttonSideProperty");
        buttonNodeProperty = new BaseProperty<>(new RadioButtonContent(this), "buttonNodeProperty");

        setExpandToLabel(true);
        getLabel().setTextPadding(new RectBox(0, 2, 0, 0));
    }

    public GuiRadioButton()
    {
        this("");
    }

    public BaseProperty<RectSide> getButtonSideProperty()
    {
        return buttonSideProperty;
    }

    public BaseProperty<GuiElement> getButtonNodeProperty()
    {
        return buttonNodeProperty;
    }

    public void setButtonSide(RectSide side)
    {
        getButtonSideProperty().setValue(side);
    }

    public RectSide getButtonSide()
    {
        return getButtonSideProperty().getValue();
    }

    public void setButtonNode(GuiElement buttonNode)
    {
        getButtonNodeProperty().setValue(buttonNode);
    }

    public GuiElement getButtonNode()
    {
        return getButtonNodeProperty().getValue();
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiRadioButtonSkin(this, new GuiTogglableButtonBehavior<>(this));
    }

    @Override
    public String type()
    {
        return "radio-button";
    }

    public static class RadioButtonContent extends GuiFather
    {
        private Rectangle box;
        private Rectangle mark;

        public RadioButtonContent(GuiRadioButton parent)
        {
            transform().heightRatio(1);
            transform().widthProperty().bind(transform().heightProperty());

            box = new Rectangle();
            mark = new Rectangle();

            box.get(StyleComponent.class).styleClass().add("box");
            mark.get(StyleComponent.class).styleClass().add("mark");

            addChild(box);
            RelativeBindingHelper.bindToCenter(box.transform(), transform());

            addChild(mark);
            RelativeBindingHelper.bindToCenter(mark.transform(), transform());

            box.transform().widthProperty().bind(box.transform().heightProperty());
            box.transform().heightProperty().bind(transform().heightProperty());

            mark.transform().widthProperty().bind(mark.transform().heightProperty());
            mark.transform().heightProperty().bind(BaseExpression.transform(box.transform().heightProperty(),
                    height -> height - 4));

            mark.visibleProperty().bind(parent.getSelectedProperty());
        }

        public GuiElement getBox()
        {
            return box;
        }

        public GuiElement getMark()
        {
            return mark;
        }

        @Override
        public String type()
        {
            return "radio-button-content";
        }
    }
}