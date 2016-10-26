package fr.ourten.brokkgui.element;

import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import fr.ourten.brokkgui.behavior.GuiListViewBehavior;
import fr.ourten.brokkgui.component.GuiNode;
import fr.ourten.brokkgui.control.GuiControl;
import fr.ourten.brokkgui.data.EOrientation;
import fr.ourten.brokkgui.skin.GuiListViewSkin;
import fr.ourten.brokkgui.skin.GuiSkinBase;
import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;

public class GuiListView<T> extends GuiControl
{
    private final BaseProperty<Boolean>                               editableProperty;

    private final BaseListProperty<T>                                 elementsProperty;
    private final BaseProperty<GuiNode>                               placeholderProperty;
    private final BaseProperty<EOrientation>                          orientationProperty;

    private final BaseProperty<Function<T, ? extends GuiListCell<T>>> cellFactoryProperty;

    private final BaseProperty<Float>                                 cellWidthProperty, cellHeightProperty;

    private final BaseProperty<Integer>                               selectedCellIndexProperty;

    public GuiListView(final List<T> elements)
    {
        this.elementsProperty = new BaseListProperty<>(elements, "elementsListProperty");

        this.editableProperty = new BaseProperty<>(false, "editableProperty");
        this.placeholderProperty = new BaseProperty<>(null, "placeholderProperty");
        this.orientationProperty = new BaseProperty<>(EOrientation.VERTICAL, "orientationProperty");

        this.cellFactoryProperty = new BaseProperty<>(null, "cellFactoryProperty");

        this.cellWidthProperty = new BaseProperty<>(0f, "cellWidthProperty");
        this.cellHeightProperty = new BaseProperty<>(0f, "cellHeightProperty");

        this.selectedCellIndexProperty = new BaseProperty<>(-1, "selectedCellIndexProperty");
    }

    public GuiListView()
    {
        this(Lists.newArrayList());
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiListViewSkin<>(this, new GuiListViewBehavior<>(this));
    }

    public BaseListProperty<T> getElementsProperty()
    {
        return this.elementsProperty;
    }

    public BaseProperty<Boolean> getEditableProperty()
    {
        return this.editableProperty;
    }

    public BaseProperty<GuiNode> getPlaceholderProperty()
    {
        return this.placeholderProperty;
    }

    public BaseProperty<EOrientation> getOrientationProperty()
    {
        return this.orientationProperty;
    }

    public BaseProperty<Function<T, ? extends GuiListCell<T>>> getCellFactoryProperty()
    {
        return this.cellFactoryProperty;
    }

    public BaseProperty<Float> getCellWidthProperty()
    {
        return this.cellWidthProperty;
    }

    public BaseProperty<Float> getCellHeightProperty()
    {
        return this.cellHeightProperty;
    }

    public BaseProperty<Integer> getSelectedCellIndexProperty()
    {
        return this.selectedCellIndexProperty;
    }

    public ImmutableList<T> getElements()
    {
        return this.elementsProperty.getValue();
    }

    public void setElements(final List<T> elements)
    {
        this.elementsProperty.clear();
        this.elementsProperty.addAll(elements);
    }

    public boolean isEditable()
    {
        return this.editableProperty.getValue();
    }

    public void setEditable(final boolean editable)
    {
        this.editableProperty.setValue(editable);
    }

    public GuiNode getPlaceholder()
    {
        return this.placeholderProperty.getValue();
    }

    public void setPlaceholder(final GuiNode placeholder)
    {
        this.placeholderProperty.setValue(placeholder);
    }

    public EOrientation getOrientation()
    {
        return this.orientationProperty.getValue();
    }

    public void setOrientation(final EOrientation orientation)
    {
        this.orientationProperty.setValue(orientation);
    }

    public boolean isEmpty()
    {
        return this.elementsProperty.getValue().isEmpty();
    }

    public void setCellFactory(final Function<T, ? extends GuiListCell<T>> cellFactory)
    {
        this.cellFactoryProperty.setValue(cellFactory);
    }

    public Function<T, ? extends GuiListCell<T>> getCellFactory()
    {
        if (this.cellFactoryProperty.getValue() == null)
            this.setCellFactory(this.getDefaultCellFactory());
        return this.cellFactoryProperty.getValue();
    }

    private Function<T, ? extends GuiListCell<T>> getDefaultCellFactory()
    {
        return T ->
        {
            final GuiListCell<T> cell = new GuiListCell<>(this, T);

            if (T == null)
            {
                cell.setGraphic(null);
                cell.setText(null);
            }
            else if (T instanceof GuiNode)
            {
                cell.setGraphic((GuiNode) T);
                cell.setText(null);
            }
            else
            {
                cell.setGraphic(null);
                cell.setText(T.toString());
            }
            return cell;
        };
    }

    public float getCellWidth()
    {
        return this.cellWidthProperty.getValue();
    }

    public void setCellWidth(final float cellWidth)
    {
        this.cellWidthProperty.setValue(cellWidth);
    }

    public float getCellHeight()
    {
        return this.cellHeightProperty.getValue();
    }

    public void setCellHeight(final float cellHeight)
    {
        this.cellHeightProperty.setValue(cellHeight);
    }

    public int getSelectedCellIndex()
    {
        return this.selectedCellIndexProperty.getValue();
    }

    public void setSelectedCellIndex(final int selectedCell)
    {
        this.selectedCellIndexProperty.setValue(selectedCell);
    }
}