package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiComponentException;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.element.shape.Rectangle;
import net.voxelindustry.brokkgui.style.StyleHolder;

import static fr.ourten.teabeans.binding.BaseExpression.biCombine;

public class CheckMark extends GuiComponent
{
    private final GuiElement box;
    private final GuiElement mark;

    private final BaseProperty<Float> markPaddingProperty;

    private final BaseProperty<RectSide> boxSideProperty;
    private final BaseProperty<RectBox>  boxPaddingProperty;

    public CheckMark(GuiElement box, GuiElement mark)
    {
        this.box = box;
        this.mark = mark;

        box.ifHas(StyleHolder.class, style -> style.styleClass().add("box"));
        mark.ifHas(StyleHolder.class, style -> style.styleClass().add("mark"));

        this.markPaddingProperty = new BaseProperty<>(2F, "markPaddingProperty");

        this.boxSideProperty = new BaseProperty<>(RectSide.LEFT, "boxSideProperty");
        this.boxPaddingProperty = new BaseProperty<>(RectBox.EMPTY, "boxPaddingProperty");
    }

    public CheckMark()
    {
        this(new Rectangle(), new Rectangle());
    }

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        if (element == null)
            return;

        if (!element.has(Toggleable.class))
            throw new GuiComponentException(
                    "CheckMark must be added to a GuiElement already containing the component Toggleable!");

        element.transform().addChildren(box.transform(), mark.transform());

        box.transform().widthProperty().bind(box.transform().heightProperty());
        box.transform().heightRatio(1);

        mark.transform().widthProperty().bind(mark.transform().heightProperty());
        mark.transform().heightProperty().bind(biCombine(box.transform().heightProperty(), markPaddingProperty,
                (height, padding) -> height - padding * 2));
        mark.transform().xTranslateProperty().bind(markPaddingProperty);
        mark.transform().yTranslateProperty().bind(markPaddingProperty);

        mark.getVisibleProperty().bind(element.get(Toggleable.class).selectedProperty());
    }

    public GuiElement box()
    {
        return this.box;
    }

    public GuiElement mark()
    {
        return this.mark;
    }

    public BaseProperty<Float> markPaddingProperty()
    {
        return this.markPaddingProperty;
    }

    public BaseProperty<RectSide> boxSideProperty()
    {
        return this.boxSideProperty;
    }

    public BaseProperty<RectBox> boxPaddingProperty()
    {
        return this.boxPaddingProperty;
    }

    public float markPadding()
    {
        return this.markPaddingProperty().getValue();
    }

    public void markPadding(float markPadding)
    {
        this.markPaddingProperty().setValue(markPadding);
    }

    public RectSide boxSide()
    {
        return this.boxSideProperty().getValue();
    }

    public void boxSide(RectSide rectSide)
    {
        this.boxSideProperty().setValue(rectSide);
    }

    public RectBox boxPadding()
    {
        return this.boxPaddingProperty().getValue();
    }

    public void boxPadding(RectBox boxPadding)
    {
        this.boxPaddingProperty().setValue(boxPadding);
    }
}
