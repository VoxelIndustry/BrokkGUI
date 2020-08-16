package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.binding.Expression;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.shape.Text;
import net.voxelindustry.brokkgui.skin.GuiCheckboxSkin;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;
import net.voxelindustry.brokkgui.style.StyleComponent;

public class GuiCheckbox extends GuiRadioButton
{
    public GuiCheckbox(String text)
    {
        super(text);

        setButtonNode(new CheckboxButtonContent(this));
    }

    public GuiCheckbox()
    {
        this("");
    }

    @Override
    public String type()
    {
        return "checkbox";
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
            transform().heightRatio(1);
            transform().widthProperty().bind(transform().heightProperty());

            box = new Rectangle();
            mark = new Text("✔");

            box.get(StyleComponent.class).styleClass().add("box");
            mark.get(StyleComponent.class).styleClass().add("mark");

            addChild(mark);
            RelativeBindingHelper.bindToCenter(mark.transform(), transform(), 0, -1);

            addChild(box);
            RelativeBindingHelper.bindToCenter(box.transform(), transform());

            box.transform().widthProperty().bind(box.transform().heightProperty());
            box.transform().heightProperty().bind(transform().heightProperty());

            mark.transform().widthProperty().bind(mark.transform().heightProperty());
            mark.transform().heightProperty().bind(Expression.transform(box.transform().heightProperty(),
                    height -> height - 2));

            mark.visibleProperty().bind(parent.getSelectedProperty());
        }

        @Override
        public String type()
        {
            return "checkbox-button";
        }

        public Rectangle getBox()
        {
            return box;
        }

        public Text getMark()
        {
            return mark;
        }
    }
}