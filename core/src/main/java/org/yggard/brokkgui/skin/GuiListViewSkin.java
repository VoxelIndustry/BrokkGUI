package org.yggard.brokkgui.skin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.yggard.brokkgui.behavior.GuiListViewBehavior;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.element.GuiListCell;
import org.yggard.brokkgui.element.GuiListView;
import org.yggard.brokkgui.event.ClickEvent;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.RenderPass;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.value.BaseListProperty;

public class GuiListViewSkin<T> extends GuiBehaviorSkinBase<GuiListView<T>, GuiListViewBehavior<T>>
{
    private List<GuiListCell<T>> listCell;
    
    private BaseListProperty<GuiNode> childrenProperty;

    public GuiListViewSkin(final GuiListView<T> model, final GuiListViewBehavior<T> behaviour, BaseListProperty<GuiNode> childrenProperty)
    {
        super(model, behaviour);
        
        this.childrenProperty = childrenProperty;

        this.reloadAllCells();

        this.getModel().getElementsProperty()
                .addListener((ListValueChangeListener<T>) (observable, oldValue, newValue) -> this.reloadAllCells());

        this.getModel().getEventDispatcher().addHandler(ClickEvent.TYPE, this::onClick);
    }

    /**
     * Used to set the selected slot value.
     *
     * @param e
     */
    public void onClick(final ClickEvent e)
    {
        if (!this.listCell.isEmpty())
        {
            final Optional<GuiListCell<T>> rtn = this.listCell.stream()
                    .filter(cell -> cell.isPointInside(e.getMouseX(), e.getMouseY())).findFirst();

            if (rtn.isPresent())
            {
                this.getBehavior().selectCell(this.listCell.indexOf(rtn.get()));
            }
        }
    }

    @Override
    public void render(final RenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);

        if (!this.listCell.isEmpty())
            this.listCell.forEach(cell -> cell.renderNode(renderer, pass, mouseX, mouseY));
        else if (this.getModel().getPlaceholder() != null)
            this.getModel().getPlaceholder().renderNode(renderer, pass, mouseX, mouseY);
    }

    public void reloadAllCells()
    {
        if(this.listCell != null)
        {
                this.listCell.forEach(cell -> 
                {
                    cell.setFather(null);
                    this.childrenProperty.remove(cell);
                });
        }
        if(this.getModel().getPlaceholder() != null)
        {
            this.getModel().getPlaceholder().setFather(null);
            this.childrenProperty.remove(this.getModel().getPlaceholder());
        }
        
        this.listCell = this.getModel().getElements().stream().map(this.getModel().getCellFactory()::apply)
                .collect(Collectors.toList());
        

        this.listCell.forEach(cell -> 
        {
            this.childrenProperty.add(cell);
            cell.setFather(getModel());
        });
        
        if(this.listCell.isEmpty() && this.getModel().getPlaceholder() != null)
        {
            this.childrenProperty.add(this.getModel().getPlaceholder());
            this.getModel().getPlaceholder().setFather(this.getModel());
        }
    }
}