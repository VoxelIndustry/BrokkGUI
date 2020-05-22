package net.voxelindustry.brokkgui.control;

import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.policy.GuiOverflowPolicy;
import net.voxelindustry.brokkgui.style.IStyleParent;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.window.IGuiSubWindow;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public abstract class GuiFather extends GuiElement implements IStyleParent
{
    private BaseProperty<GuiOverflowPolicy> guiOverflowProperty;

    public GuiFather()
    {
        guiOverflowProperty = new BaseProperty<>(GuiOverflowPolicy.NONE, "overflowProperty");
        guiOverflowProperty.addListener(obs ->
        {
            if (getScissorBox() == null)
                return;

            if (guiOverflowProperty.getValue() == GuiOverflowPolicy.TRIM)
                getScissorBox().setRenderPassPredicate(pass -> pass.getPriority() <= RenderPass.FOREGROUND.getPriority());
            else if (guiOverflowProperty.getValue() == GuiOverflowPolicy.NONE)
                getScissorBox().setRenderPassPredicate(pass -> false);
            else
                getScissorBox().setRenderPassPredicate(pass -> true);
        });
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

    public BaseProperty<GuiOverflowPolicy> getGuiOverflowProperty()
    {
        return guiOverflowProperty;
    }

    public void setGuiOverflow(GuiOverflowPolicy guiOverflow)
    {
        getGuiOverflowProperty().setValue(guiOverflow);
    }

    public GuiOverflowPolicy getGuiOverflow()
    {
        return getGuiOverflowProperty().getValue();
    }

    @Override
    public void dispose()
    {
        super.dispose();

        transform().streamChildren().forEach(childTransform -> childTransform.element().dispose());
    }

    @Override
    public void setWindow(IGuiSubWindow window)
    {
        super.setWindow(window);

        getChildren().forEach(child -> child.element().setWindow(getWindow()));
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
