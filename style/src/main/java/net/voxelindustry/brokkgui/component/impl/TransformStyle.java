package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.RectCorner;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.event.StyleComponentEvent;
import net.voxelindustry.brokkgui.style.shorthand.ShorthandArgMappers;

public class TransformStyle extends Transform
{
    private StyleComponent style;

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        if (element.has(StyleComponent.class))
        {
            style = element.get(StyleComponent.class);
            style.registerConditionalProperties("border*", this::registerProperties);
        }
        getEventDispatcher().addHandler(StyleComponentEvent.TYPE, this::onStyleComponentAdded);
    }

    private void onStyleComponentAdded(StyleComponentEvent event)
    {
        style = event.component();
        registerProperties(style);
    }

    private void registerProperties(StyleComponent styleComponent)
    {
        styleComponent.registerShorthand("border-width", 0F,
                Float.class, ShorthandArgMappers.BOX_MAPPER,
                "border-top-width", "border-right-width", "border-bottom-width", "border-left-width");

        styleComponent.registerShorthand("border-radius", 0, Integer.class, ShorthandArgMappers.BOX_MAPPER,
                "border-top-left-radius", "border-top-right-radius",
                "border-bottom-right-radius", "border-bottom-left-radius");
    }

    private StyleComponent style()
    {
        return style;
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    @Override
    public Property<Float> borderWidthLeftProperty()
    {
        if (borderWidthLeftProperty == null)
            borderWidthLeftProperty = style().getOrCreateProperty("border-left-width", Float.class);
        return borderWidthLeftProperty;
    }

    @Override
    public Property<Float> borderWidthRightProperty()
    {
        if (borderWidthRightProperty == null)
            borderWidthRightProperty = style().getOrCreateProperty("border-right-width", Float.class);
        return borderWidthRightProperty;
    }

    @Override
    public Property<Float> borderWidthTopProperty()
    {
        if (borderWidthTopProperty == null)
            borderWidthTopProperty = style().getOrCreateProperty("border-top-width", Float.class);
        return borderWidthTopProperty;
    }

    @Override
    public Property<Float> borderWidthBottomProperty()
    {
        if (borderWidthBottomProperty == null)
            borderWidthBottomProperty = style().getOrCreateProperty("border-bottom-width", Float.class);
        return borderWidthBottomProperty;
    }

    @Override
    public Property<Integer> borderRadiusTopLeftProperty()
    {
        if (borderRadiusTopLeftProperty == null)
            borderRadiusTopLeftProperty = style().getOrCreateProperty("border-top-left-radius", Integer.class);
        return borderRadiusTopLeftProperty;
    }

    @Override
    public Property<Integer> borderRadiusTopRightProperty()
    {
        if (borderRadiusTopRightProperty == null)
            borderRadiusTopRightProperty = style().getOrCreateProperty("border-top-right-radius", Integer.class);
        return borderRadiusTopRightProperty;
    }

    @Override
    public Property<Integer> borderRadiusBottomLeftProperty()
    {
        if (borderRadiusBottomLeftProperty == null)
            borderRadiusBottomLeftProperty = style().getOrCreateProperty("border-bottom-left-radius", Integer.class);
        return borderRadiusBottomLeftProperty;
    }

    @Override
    public Property<Integer> borderRadiusBottomRightProperty()
    {
        if (borderRadiusBottomRightProperty == null)
            borderRadiusBottomRightProperty = style().getOrCreateProperty("border-bottom-right-radius", Integer.class);
        return borderRadiusBottomRightProperty;
    }

    @Override
    public float borderWidth()
    {
        return style().getValue("border-top-width", Float.class, 0F);
    }

    @Override
    public float borderWidth(RectSide side)
    {
        return style().getValue("border-" + side.getCssString() + "-width", Float.class, 0F);
    }

    @Override
    public void borderWidth(float width)
    {
        borderWidth(RectSide.UP, width);
        borderWidth(RectSide.DOWN, width);
        borderWidth(RectSide.LEFT, width);
        borderWidth(RectSide.RIGHT, width);
    }

    @Override
    public void borderWidth(RectSide side, float borderWidth)
    {
        style().setPropertyDirect("border-" + side.getCssString() + "-width", borderWidth, Float.class);
    }

    @Override
    public int borderRadius(RectCorner corner)
    {
        return style().getValue("border-" + corner.getCssString() + "-radius", Integer.class, 0);
    }

    @Override
    public void borderRadius(RectCorner corner, int radius)
    {
        style().setPropertyDirect("border-" + corner.getCssString() + "-radius", radius, Integer.class);
    }
}
