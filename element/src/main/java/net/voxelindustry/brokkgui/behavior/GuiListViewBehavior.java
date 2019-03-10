package net.voxelindustry.brokkgui.behavior;

import fr.ourten.teabeans.value.BaseListProperty;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.element.GuiListCell;
import net.voxelindustry.brokkgui.element.GuiListView;
import net.voxelindustry.brokkgui.event.ClickEvent;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GuiListViewBehavior<T> extends GuiScrollableBehavior<GuiListView<T>>
{
    private Supplier<BaseListProperty<GuiNode>> childrenSupplier;

    public GuiListViewBehavior(final GuiListView<T> model, Supplier<BaseListProperty<GuiNode>> childrenSupplier)
    {
        super(model);

        this.childrenSupplier = childrenSupplier;

        this.mapAllCells();
        this.getModel().getElementsProperty()
                .addListener(obs -> this.mapAllCells());

        this.getModel().getEventDispatcher().addHandler(ClickEvent.TYPE, this::onClick);
    }

    /**
     * Used to transfer ClickEvent to the right node inside the ListView. If the
     * list does not contains any GuiListCell it's forwarded to the placeholder
     * node.
     *
     * @param e
     */
    private void onClick(final ClickEvent e)
    {
        if (!this.childrenSupplier.get().isEmpty())
        {
            this.childrenSupplier.get().getModifiableValue().stream()
                    .filter(cell -> cell instanceof GuiListCell && cell.isPointInside(e.getMouseX(), e.getMouseY()))
                    .findFirst()
                    .ifPresent(guiNode -> this.selectCell(this.childrenSupplier.get().indexOf(guiNode)));
        }
    }

    /**
     * Used to select cell after the skin forward the ClickEvent to the
     * behavior.
     *
     * @param cellIndex
     */
    private void selectCell(final int cellIndex)
    {
        this.getModel().setSelectedCellIndex(cellIndex);
    }

    private void mapAllCells()
    {
        // Remove without triggering the listeners and with one iteration only
        Iterator<GuiNode> childrenIterator = this.childrenSupplier.get().getModifiableValue().iterator();

        while (childrenIterator.hasNext())
        {
            GuiNode next = childrenIterator.next();

            if (next instanceof GuiListCell)
            {
                next.setFather(null);
                childrenIterator.remove();
            }
        }

        // Add and trigger the listeners to refresh the style of the cell
        this.childrenSupplier.get().addAll(
                this.getModel().getElements().stream().map(this.getModel().getCellFactory())
                        .peek(cell -> cell.setFather(this.getModel()))
                        .collect(Collectors.toList()));
    }
}