package net.voxelindustry.brokkgui.behavior;

import fr.ourten.teabeans.property.ListProperty;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.element.GuiListCell;
import net.voxelindustry.brokkgui.element.GuiListView;
import net.voxelindustry.brokkgui.event.ClickEvent;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GuiListViewBehavior<T> extends GuiScrollableBehavior<GuiListView<T>>
{
    private Supplier<ListProperty<Transform>> childrenSupplier;

    public GuiListViewBehavior(GuiListView<T> model, Supplier<ListProperty<Transform>> childrenSupplier)
    {
        super(model);

        this.childrenSupplier = childrenSupplier;

        mapAllCells();
        getModel().getElementsProperty()
                .addListener(obs -> mapAllCells());

        getModel().getEventDispatcher().addHandler(ClickEvent.TYPE, this::onClick);
    }

    /**
     * Used to transfer ClickEvent to the right node inside the ListView. If the
     * list does not contains any GuiListCell it's forwarded to the placeholder
     * node.
     *
     * @param e
     */
    private void onClick(ClickEvent e)
    {
        if (!childrenSupplier.get().isEmpty())
        {
            childrenSupplier.get().getModifiableValue().stream()
                    .filter(cell -> cell.element() instanceof GuiListCell && cell.isPointInside(e.getMouseX(), e.getMouseY()))
                    .findFirst()
                    .ifPresent(guiNode -> selectCell(childrenSupplier.get().indexOf(guiNode)));
        }
    }

    /**
     * Used to select cell after the skin forward the ClickEvent to the
     * behavior.
     *
     * @param cellIndex
     */
    private void selectCell(int cellIndex)
    {
        getModel().setSelectedCellIndex(cellIndex);
    }

    private void mapAllCells()
    {
        // Remove without triggering the listeners and with one iteration only
        Iterator<Transform> childrenIterator = childrenSupplier.get().getModifiableValue().iterator();

        while (childrenIterator.hasNext())
        {
            Transform next = childrenIterator.next();

            if (next.element() instanceof GuiListCell)
            {
                next.transform().setParent(null);
                childrenIterator.remove();
            }
        }

        // Add and trigger the listeners to refresh the style of the cell
        childrenSupplier.get().addAll(
                getModel().getElements().stream()
                        .map(getModel().getCellFactory())
                        .peek(cell -> cell.transform().setParent(getModel().transform()))
                        .map(GuiElement::transform)
                        .collect(Collectors.toList()));
    }
}