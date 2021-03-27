package net.voxelindustry.brokkgui.element.input;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.RenderComponent;
import net.voxelindustry.brokkgui.component.impl.BooleanFormFieldComponent;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.internal.IRenderCommandReceiver;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.style.StyleComponent;

public class GuiCheckbox extends GuiRadioButton
{
    public GuiCheckbox(String text)
    {
        super(text);

        buttonNode(new CheckboxButtonContent(this, booleanFormFieldComponent()));
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

    public static class CheckboxButtonContent extends Rectangle
    {
        private final GuiElement mark = GuiElement.createInline("checkmark", CheckboxTickMark.class);

        public CheckboxButtonContent(GuiCheckbox parent, BooleanFormFieldComponent booleanFormFieldComponent)
        {
            transform().heightProperty().bindProperty(booleanFormFieldComponent.buttonSizeProperty());
            transform().widthProperty().bindProperty(transform().heightProperty());

            get(StyleComponent.class).styleClass().add("box");
            mark.get(StyleComponent.class).styleClass().add("mark");

            transform().addChild(mark.transform());
            RelativeBindingHelper.bindToCenter(mark.transform(), transform());

            mark.transform().widthProperty().bindProperty(mark.transform().heightProperty());
            mark.transform().heightProperty().bindProperty(transform().heightProperty().subtract(2));

            mark.visibleProperty().bindProperty(parent.getSelectedProperty());
        }

        @Override
        public String type()
        {
            return "checkbox-button";
        }

        public GuiElement mark()
        {
            return mark;
        }
    }

    public static class CheckboxTickMark extends GuiComponent implements RenderComponent
    {
        private StyleComponent style;

        @Override
        public void attach(GuiElement element)
        {
            super.attach(element);

            style = element.get(StyleComponent.class);
            style.registerProperty("color", Color.BLACK, Color.class);
        }

        @Override
        public void renderContent(IRenderCommandReceiver renderer, float mouseX, float mouseY)
        {
            float startX = transform().leftPos();
            float startY = transform().topPos();

            renderer.drawColoredTriangles(
                    transform().zLevel(),
                    color(),
                    RenderPass.BACKGROUND,
                    // Left rectangle upper triangle
                    (3 / 16F * transform().width()) + startX,
                    (9 / 16F * transform().height()) + startY,
                    (10 / 16F * transform().width()) + startX,
                    (12 / 16F * transform().height()) + startY,
                    (5 / 16F * transform().width()) + startX,
                    (7 / 16F * transform().height()) + startY,

                    // Left rectangle lower triangle
                    (3 / 16F * transform().width()) + startX,
                    (9 / 16F * transform().height()) + startY,
                    (8 / 16F * transform().width()) + startX,
                    (14 / 16F * transform().height()) + startY,
                    (10 / 16F * transform().width()) + startX,
                    (12 / 16F * transform().height()) + startY,

                    // Right rectangle upper triangle
                    (15 / 16F * transform().width()) + startX,
                    (7 / 16F * transform().height()) + startY,
                    (13 / 16F * transform().width()) + startX,
                    (5 / 16F * transform().height()) + startY,
                    (8 / 16F * transform().width()) + startX,
                    (10 / 16F * transform().height()) + startY,

                    // Right rectangle lower triangle
                    (8 / 16F * transform().width()) + startX,
                    (10 / 16F * transform().height()) + startY,
                    (10 / 16F * transform().width()) + startX,
                    (12 / 16F * transform().height()) + startY,
                    (15 / 16F * transform().width()) + startX,
                    (7 / 16F * transform().height()) + startY
            );
        }

        public Property<Color> colorProperty()
        {
            return style.getOrCreateProperty("color", Color.class);
        }

        public Color color()
        {
            return style.getValue("color", Color.class, Color.BLACK);
        }

        public void color(Color color)
        {
            style.setPropertyDirect("color", color, Color.class);
        }
    }
}