package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.behavior.GuiListViewBehavior;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiScrollableBase;
import net.voxelindustry.brokkgui.data.RectAxis;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.skin.GuiListViewSkin;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GuiListView<T> extends GuiScrollableBase
{
    private final BaseProperty<Boolean> editableProperty;

    private final BaseListProperty<T>      elementsProperty;
    private final BaseProperty<GuiElement> placeholderProperty;
    private final BaseProperty<RectAxis>   orientationProperty;

    private final BaseProperty<Function<T, GuiListCell<T>>> cellFactoryProperty;

    private final BaseProperty<Float> cellWidthProperty, cellHeightProperty;
    private final BaseProperty<Float> cellXPaddingProperty, cellYPaddingProperty;

    private final BaseProperty<Integer> selectedCellIndexProperty;

    public GuiListView(List<T> elements)
    {
        elementsProperty = new BaseListProperty<>(elements, "elementsListProperty");

        editableProperty = new BaseProperty<>(false, "editableProperty");
        placeholderProperty = new BaseProperty<>(null, "placeholderProperty");
        orientationProperty = new BaseProperty<>(RectAxis.VERTICAL, "orientationProperty");

        cellFactoryProperty = new BaseProperty<>(null, "cellFactoryProperty");

        cellWidthProperty = new BaseProperty<>(100f, "cellWidthProperty");
        cellHeightProperty = new BaseProperty<>(20f, "cellHeightProperty");

        cellXPaddingProperty = new BaseProperty<>(0f, "cellXPaddingProperty");
        cellYPaddingProperty = new BaseProperty<>(0f, "cellYPaddingProperty");

        selectedCellIndexProperty = new BaseProperty<>(-1, "selectedCellIndexProperty");

        transform().childrenProperty().addListener(obs ->
        {
            if (!getPlaceholderProperty().isPresent())
                return;

            if (transform().childrenProperty().size() == 1 && !getPlaceholder().isVisible())
                getPlaceholder().setVisible(true);
            else if (transform().childrenProperty().size() != 1 && getPlaceholder().isVisible())
                getPlaceholder().setVisible(false);
        });

        getTrueWidthProperty().bind(new BaseBinding<Float>()
        {
            {
                bind(orientationProperty, cellWidthProperty, cellXPaddingProperty, elementsProperty);
            }

            @Override
            public Float computeValue()
            {
                if (getOrientation().equals(RectAxis.HORIZONTAL))
                    return getCellWidth() * getElements().size() + getCellXPadding() * (getElements().size() - 1);
                return getCellWidth();
            }
        });

        getTrueHeightProperty().bind(new BaseBinding<Float>()
        {
            {
                bind(orientationProperty, cellHeightProperty, cellYPaddingProperty, elementsProperty);
            }

            @Override
            public Float computeValue()
            {
                if (getOrientation().equals(RectAxis.VERTICAL))
                    return getCellHeight() * getElements().size() + getCellYPadding() * (getElements().size() - 1);
                return getCellHeight();
            }
        });
    }

    public GuiListView()
    {
        this(new ArrayList<>());
    }

    @Override
    public String type()
    {
        return "list-view";
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiListViewSkin<>(this, new GuiListViewBehavior<>(this, transform()::childrenProperty));
    }

    public BaseListProperty<T> getElementsProperty()
    {
        return elementsProperty;
    }

    public BaseProperty<Boolean> getEditableProperty()
    {
        return editableProperty;
    }

    public BaseProperty<GuiElement> getPlaceholderProperty()
    {
        return placeholderProperty;
    }

    public BaseProperty<RectAxis> getOrientationProperty()
    {
        return orientationProperty;
    }

    public BaseProperty<Function<T, GuiListCell<T>>> getCellFactoryProperty()
    {
        return cellFactoryProperty;
    }

    public BaseProperty<Float> getCellWidthProperty()
    {
        return cellWidthProperty;
    }

    public BaseProperty<Float> getCellHeightProperty()
    {
        return cellHeightProperty;
    }

    public BaseProperty<Integer> getSelectedCellIndexProperty()
    {
        return selectedCellIndexProperty;
    }

    public BaseProperty<Float> getCellXPaddingProperty()
    {
        return cellXPaddingProperty;
    }

    public BaseProperty<Float> getCellYPaddingProperty()
    {
        return cellYPaddingProperty;
    }

    /**
     * @return an immutable list
     */
    public List<T> getElements()
    {
        return getElementsProperty().getValue();
    }

    public void setElements(List<T> elements)
    {
        getElementsProperty().muteWhile(() ->
        {
            getElementsProperty().clear();
            getElementsProperty().addAll(elements);
        });
    }

    public void addElement(T element)
    {
        getElementsProperty().add(element);
    }

    public boolean removeElement(T element)
    {
        return getElementsProperty().remove(element);
    }

    public boolean hasElement(T element)
    {
        return getElementsProperty().contains(element);
    }

    public boolean isEditable()
    {
        return getEditableProperty().getValue();
    }

    public void setEditable(boolean editable)
    {
        getEditableProperty().setValue(editable);
    }

    public GuiElement getPlaceholder()
    {
        return getPlaceholderProperty().getValue();
    }

    public void setPlaceholder(GuiElement placeholder)
    {
        if (getPlaceholder() != null)
        {
            transform().removeChild(getPlaceholder().transform());
        }

        getPlaceholderProperty().setValue(placeholder);

        if (getPlaceholder() != null)
        {
            transform().addChild(getPlaceholder().transform());
            RelativeBindingHelper.bindToCenter(placeholder.transform(), transform());

            if (transform().childrenProperty().size() == 1)
                getPlaceholder().setVisible(true);
            else
                getPlaceholder().setVisible(false);
        }
    }

    public RectAxis getOrientation()
    {
        return getOrientationProperty().getValue();
    }

    public void setOrientation(RectAxis orientation)
    {
        getOrientationProperty().setValue(orientation);
    }

    public boolean isEmpty()
    {
        return getElementsProperty().getValue().isEmpty();
    }

    public void setCellFactory(Function<T, GuiListCell<T>> cellFactory)
    {
        getCellFactoryProperty().setValue(cellFactory);
    }

    public Function<T, GuiListCell<T>> getCellFactory()
    {
        if (!getCellFactoryProperty().isPresent())
            setCellFactory(getDefaultCellFactory());
        return getCellFactoryProperty().getValue();
    }

    private Function<T, GuiListCell<T>> getDefaultCellFactory()
    {
        return content ->
        {
            GuiListCell<T> cell = new GuiListCell<>(this, content);

            if (content == null)
                cell.setGraphic(null);
            else if (content instanceof GuiElement)
                cell.setGraphic((GuiElement) content);
            else
            {
                GuiLabel label = new GuiLabel(content.toString());
                label.setExpandToText(false);
                cell.setGraphic(label);
            }
            return cell;
        };
    }

    public float getCellWidth()
    {
        return getCellWidthProperty().getValue();
    }

    public void setCellWidth(float cellWidth)
    {
        getCellWidthProperty().setValue(cellWidth);
    }

    public float getCellHeight()
    {
        return getCellHeightProperty().getValue();
    }

    public void setCellHeight(float cellHeight)
    {
        getCellHeightProperty().setValue(cellHeight);
    }

    public void setCellSize(float cellWidth, float cellHeight)
    {
        setCellWidth(cellWidth);
        setCellHeight(cellHeight);
    }

    public float getCellXPadding()
    {
        return getCellXPaddingProperty().getValue();
    }

    public void setCellXPadding(float cellXPadding)
    {
        getCellXPaddingProperty().setValue(cellXPadding);
    }

    public float getCellYPadding()
    {
        return getCellYPaddingProperty().getValue();
    }

    public void setCellYPadding(float cellYPadding)
    {
        getCellYPaddingProperty().setValue(cellYPadding);
    }

    public void setCellPadding(float cellXPadding, float cellYPadding)
    {
        setCellXPadding(cellXPadding);
        setCellYPadding(cellYPadding);
    }

    public int getSelectedCellIndex()
    {
        return getSelectedCellIndexProperty().getValue();
    }

    public void setSelectedCellIndex(int selectedCell)
    {
        getSelectedCellIndexProperty().setValue(selectedCell);
    }
}