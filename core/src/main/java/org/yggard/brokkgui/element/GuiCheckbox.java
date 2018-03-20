package org.yggard.brokkgui.element;

import fr.ourten.teabeans.binding.BaseExpression;
import org.yggard.brokkgui.behavior.GuiTogglableButtonBehavior;
import org.yggard.brokkgui.control.GuiFather;
import org.yggard.brokkgui.data.RelativeBindingHelper;
import org.yggard.brokkgui.shape.Rectangle;
import org.yggard.brokkgui.shape.Text;
import org.yggard.brokkgui.skin.GuiCheckboxSkin;
import org.yggard.brokkgui.skin.GuiSkinBase;

public class GuiCheckbox extends GuiRadioButton
{
    public GuiCheckbox(String text)
    {
        super(text);
        this.setType("checkbox");

        this.setButtonNode(new CheckboxButtonContent(this));
    }

    public GuiCheckbox()
    {
        this("");
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiCheckboxSkin(this, new GuiTogglableButtonBehavior<>(this));
    }

    public static class CheckboxButtonContent extends GuiFather
    {
        private Rectangle box;
        private Text      mark;

        public CheckboxButtonContent(GuiCheckbox parent)
        {
            super("checkbox-content");

            this.setHeightRatio(1);
            this.getWidthProperty().bind(this.getHeightProperty());

            this.box = new Rectangle();
            this.mark = new Text("✔");

            this.box.getStyleClass().add("box");
            this.mark.getStyleClass().add("mark");

            this.addChild(this.mark);
            RelativeBindingHelper.bindToCenter(this.mark, this, 0, -1);

            this.addChild(this.box);
            RelativeBindingHelper.bindToCenter(this.box, this);

            this.box.getWidthProperty().bind(this.box.getHeightProperty());
            this.box.getHeightProperty().bind(this.getHeightProperty());

            this.mark.getWidthProperty().bind(this.mark.getHeightProperty());
            this.mark.getHeightProperty().bind(BaseExpression.transform(this.box.getHeightProperty(),
                    height -> height - 2));

            this.mark.getVisibleProperty().bind(parent.getSelectedProperty());
        }

        public Rectangle getBox()
        {
            return this.box;
        }

        public Text getMark()
        {
            return this.mark;
        }
    }
}