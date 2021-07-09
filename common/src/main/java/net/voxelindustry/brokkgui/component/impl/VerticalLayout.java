package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.binding.specific.FloatBinding;
import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
import fr.ourten.teabeans.property.specific.BooleanProperty;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.data.AlignmentMode;

public class VerticalLayout extends GuiComponent
{
    private final ListValueChangeListener<Transform>  transformListener = this::doLayout;
    private final ListValueChangeListener<GuiElement> elementListener   = this::doLayout;

    private final Property<AlignmentMode> alignmentProperty = new Property<>(AlignmentMode.BEGIN);

    private ListProperty<Transform>  childrenTransformProperty;
    private ListProperty<GuiElement> childrenElementProperty;

    // Value is always true
    // This property is observed with changes-unaware listeners so any calls will trigger a new layout pass
    private final BooleanProperty           dirtyLayoutProperty = new BooleanProperty(true);
    private final ValueInvalidationListener dirtyLayoutListener = obs -> dirtyLayoutProperty.set(true);

    private boolean addToHierarchy;

    private void doLayout(ObservableValue<?> observable, Object oldValue, Object newValue)
    {
        if (oldValue instanceof GuiElement childElement)
        {
            childElement.transform().heightProperty().removeListener(dirtyLayoutListener);
            childElement.transform().yTranslateProperty().removeListener(dirtyLayoutListener);
            childElement.transform().xPosProperty().unbind();
            childElement.transform().yPosProperty().unbind();

            if (addToHierarchy)
                transform().removeChild(childElement.transform());
        }
        else if (oldValue instanceof Transform childTransform)
        {
            childTransform.heightProperty().removeListener(dirtyLayoutListener);
            childTransform.transform().yTranslateProperty().removeListener(dirtyLayoutListener);
            childTransform.xPosProperty().unbind();
            childTransform.yPosProperty().unbind();

            if (addToHierarchy)
                transform().removeChild(childTransform);
        }

        bindElementToLayout(newValue);
    }

    private void bindElementToLayout(Object value)
    {
        if (value == null)
            return;

        var boundTransform = value instanceof Transform t ? t : value instanceof GuiElement ? ((GuiElement) value).transform() : null;

        if (boundTransform == null)
            return;

        if (addToHierarchy)
            transform().addChild(boundTransform);

        boundTransform.heightProperty().addListener(dirtyLayoutListener);
        boundTransform.yTranslateProperty().addListener(dirtyLayoutListener);

        boundTransform.xPosProperty().bindProperty(new FloatBinding()
        {
            {
                super.bind(transform().xPosProperty(),
                        transform().xTranslateProperty(),
                        transform().xOffsetProperty(),
                        transform().widthProperty(),
                        boundTransform.widthProperty(),
                        alignmentProperty);
            }

            @Override
            protected float computeValue()
            {
                return switch (alignmentProperty.getValue())
                        {
                            case BEGIN -> transform().leftPos();
                            case CENTER -> transform().leftPos() + transform().width() / 2 - boundTransform.width() / 2;
                            case END -> transform().rightPos() - boundTransform.width();
                        } + transform().xOffsetProperty().get();
            }
        });

        if (value instanceof GuiElement)
        {
            boundTransform.yPosProperty().bindProperty(new FloatBinding()
            {
                {
                    super.bind(transform().yPosProperty(),
                            transform().yTranslateProperty(),
                            transform().yOffsetProperty(),
                            dirtyLayoutProperty);
                }

                @Override
                protected float computeValue()
                {
                    var childPos = 0F;
                    var childIndex = childrenElementProperty.indexOf(boundTransform.element());

                    for (int index = 0; index < childIndex; index++)
                    {
                        var previousChild = childrenElementProperty.get(index);
                        childPos += previousChild.height() + previousChild.yTranslate();
                    }

                    return transform().topPos() + transform().yOffsetProperty().get() + childPos;
                }
            });
        }
        else
        {
            boundTransform.yPosProperty().bindProperty(new FloatBinding()
            {
                {
                    super.bind(transform().yPosProperty(),
                            transform().yTranslateProperty(),
                            transform().yOffsetProperty(),
                            dirtyLayoutProperty);
                }

                @Override
                protected float computeValue()
                {
                    var childPos = 0F;
                    var childIndex = childrenTransformProperty.indexOf(boundTransform);

                    for (int index = 0; index < childIndex; index++)
                    {
                        var previousChild = childrenTransformProperty.get(index);
                        childPos += previousChild.height() + previousChild.yTranslate();
                    }

                    return transform().topPos() + transform().yOffsetProperty().get() + childPos;
                }
            });
        }
    }

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        setChildrenTransforms(transform().childrenProperty());
    }

    public void setChildrenTransforms(ListProperty<Transform> childrenProperty)
    {
        if (childrenTransformProperty != null)
            childrenTransformProperty.removeChangeListener(transformListener);
        if (childrenElementProperty != null)
            childrenElementProperty.removeChangeListener(elementListener);

        childrenTransformProperty = childrenProperty;
        childrenProperty.addChangeListener(dirtyLayoutListener);
        childrenProperty.addChangeListener(transformListener);

        childrenProperty.forEach(this::bindElementToLayout);
    }

    public void setChildrenElements(ListProperty<GuiElement> childrenProperty)
    {
        if (childrenElementProperty != null)
            childrenElementProperty.removeChangeListener(elementListener);
        if (childrenTransformProperty != null)
            childrenTransformProperty.removeChangeListener(transformListener);

        childrenElementProperty = childrenProperty;
        childrenProperty.addChangeListener(dirtyLayoutListener);
        childrenProperty.addChangeListener(elementListener);

        childrenProperty.forEach(this::bindElementToLayout);
    }

    public void addElementsToHierarchy(boolean addToHierarchy)
    {
        this.addToHierarchy = addToHierarchy;
        if (addToHierarchy)
        {
            if (childrenElementProperty != null)
            {
                for (var element : childrenElementProperty)
                    transform().addChild(element.transform());
            }
            if (childrenTransformProperty != null)
            {
                for (var transform : childrenTransformProperty)
                    transform().addChild(transform);
            }
        }
        else
        {
            if (childrenElementProperty != null)
            {
                for (var element : childrenElementProperty)
                    transform().removeChild(element.transform());
            }
            if (childrenTransformProperty != null)
            {
                for (var transform : childrenTransformProperty)
                    transform().removeChild(transform);
            }
        }
    }

    ////////////////
    // PROPERTIES //
    ////////////////

    public Property<AlignmentMode> alignmentProperty()
    {
        return alignmentProperty;
    }

    ////////////
    // VALUES //
    ////////////

    public AlignmentMode alignment()
    {
        return alignmentProperty().getValue();
    }

    public void alignment(AlignmentMode alignmentMode)
    {
        alignmentProperty().setValue(alignmentMode);
    }
}
