package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.binding.Binding;
import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
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
    private final Property<Boolean> editableProperty;

    private final ListProperty<T>      elementsProperty;
    private final Property<GuiElement> placeholderProperty;
    private final Property<RectAxis>   orientationProperty;

    private final Property<Function<T, GuiListCell<T>>> cellFactoryProperty;

    private final Property<Float> cellWidthProperty, cellHeightProperty;
    private final Property<Float> cellXPaddingProperty, cellYPaddingProperty;

    private final Property<Integer> selectedCellIndexProperty;

    public GuiListView(List<T> elements)
    {
        elementsProperty = new ListProperty<>(elements);

        editableProperty = new Property<>(false);
        placeholderProperty = new Property<>(null);
        orientationProperty = new Property<>(RectAxis.VERTICAL);

        cellFactoryProperty = new Property<>(null);

        cellWidthProperty = new Property<>(100F);
        cellHeightProperty = new Property<>(20F);

        cellXPaddingProperty = new Property<>(0F);
        cellYPaddingProperty = new Property<>(0F);

        selectedCellIndexProperty = new Property<>(-1);

        transform().childrenProperty().addListener(obs ->
        {
            if (!getPlaceholderProperty().isPresent())
                return;

            if (transform().childrenProperty().size() == 1 && !getPlaceholder().isVisible())
                getPlaceholder().setVisible(true);
            else if (transform().childrenProperty().size() != 1 && getPlaceholder().isVisible())
                getPlaceholder().setVisible(false);
        });

        getTrueWidthProperty().bindProperty(new Binding<Float>()
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

        getTrueHeightProperty().bindProperty(new Binding<Float>()
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

    public ListProperty<T> getElementsProperty()
    {
        return elementsProperty;
    }

    public Property<Boolean> getEditableProperty()
    {
        return editableProperty;
    }

    public Property<GuiElement> getPlaceholderProperty()
    {
        return placeholderProperty;
    }

    public Property<RectAxis> getOrientationProperty()
    {
        return orientationProperty;
    }

    public Property<Function<T, GuiListCell<T>>> getCellFactoryProperty()
    {
        return cellFactoryProperty;
    }

    public Property<Float> getCellWidthProperty()
    {
        return cellWidthProperty;
    }

    public Property<Float> getCellHeightProperty()
    {
        return cellHeightProperty;
    }

    public Property<Integer> getSelectedCellIndexProperty()
    {
        return selectedCellIndexProperty;
    }

    public Property<Float> getCellXPaddingProperty()
    {
        return cellXPaddingProperty;
    }

    public Property<Float> getCellYPaddingProperty()
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
                label.expandToText(false);
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