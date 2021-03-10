package net.voxelindustry.brokkgui.control;

import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.style.IStyleParent;
import net.voxelindustry.brokkgui.style.StyleComponent;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public abstract class GuiFather extends GuiElement implements IStyleParent
{
    private StyleComponent style;

    @Override
    public void postConstruct()
    {
        super.postConstruct();

        style = get(StyleComponent.class);
    }

    public StyleComponent style()
    {
        return style;
    }

    /**
     * @return an immutable list
     */
    public List<Transform> getChildren()
    {
        return transform().childrenProperty().getValue();
    }

    public void addChild(GuiElement node)
    {
        transform().childrenProperty().add(node.transform());
    }

    public void addChildren(GuiElement... nodes)
    {
        for (GuiElement node : nodes)
            addChild(node);
    }

    public void removeChild(GuiElement node)
    {
        transform().removeChild(node.transform());
    }

    public void clearChildren()
    {
        transform().clearChildren();
    }

    public void removeChildrenOfType(Class<?> typeClass)
    {
        transform().streamChildren()
                .filter(childTransform -> typeClass.isInstance(childTransform.element()))
                .collect(toList())
                .forEach(transform -> transform().removeChild(transform));
    }

    public boolean hasChild(GuiElement node)
    {
        return transform().hasChild(node.transform());
    }

    public List<GuiElement> getNodesAtPoint(float pointX, float pointY, boolean searchChildren)
    {
        return streamNodesAtPoint(pointX, pointY, searchChildren).collect(toList());
    }

    public Stream<GuiElement> streamNodesAtPoint(float pointX, float pointY, boolean searchChildren)
    {
        Stream<GuiElement> filteredChildren = transform().streamChildren()
                .filter(child -> child.isPointInside(pointX, pointY)).map(GuiComponent::element);

        if (searchChildren)
            filteredChildren = filteredChildren.flatMap(child ->
                    Stream.concat(Stream.of(child), streamNodesAtPoint(pointX, pointY, true)));

        return filteredChildren;
    }

    /////////////////////
    //     STYLING     //
    /////////////////////

    @Override
    public List<StyleComponent> getChildStyles()
    {
        return transform().streamChildren().map(childTransform -> childTransform.element().get(StyleComponent.class)).collect(toList());
    }

    @Override
    public int getChildCount()
    {
        return transform().childrenProperty().size();
    }
}
