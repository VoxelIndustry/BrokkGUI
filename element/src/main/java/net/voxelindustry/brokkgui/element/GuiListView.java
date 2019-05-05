package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.behavior.GuiListViewBehavior;
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

    private final BaseListProperty<T>    elementsProperty;
    private final BaseProperty<GuiNode>  placeholderProperty;
    private final BaseProperty<RectAxis> orientationProperty;

    private final BaseProperty<Function<T, GuiListCell<T>>> cellFactoryProperty;

    private final BaseProperty<Float> cellWidthProperty, cellHeightProperty;
    private final BaseProperty<Float> cellXPaddingProperty, cellYPaddingProperty;

    private final BaseProperty<Integer> selectedCellIndexProperty;

    public GuiListView(List<T> elements)
    {
        super("listview");

        this.elementsProperty = new BaseListProperty<>(elements, "elementsListProperty");

        this.editableProperty = new BaseProperty<>(false, "editableProperty");
        this.placeholderProperty = new BaseProperty<>(null, "placeholderProperty");
        this.orientationProperty = new BaseProperty<>(RectAxis.VERTICAL, "orientationProperty");

        this.cellFactoryProperty = new BaseProperty<>(null, "cellFactoryProperty");

        this.cellWidthProperty = new BaseProperty<>(100f, "cellWidthProperty");
        this.cellHeightProperty = new BaseProperty<>(20f, "cellHeightProperty");

        this.cellXPaddingProperty = new BaseProperty<>(0f, "cellXPaddingProperty");
        this.cellYPaddingProperty = new BaseProperty<>(0f, "cellYPaddingProperty");

        this.selectedCellIndexProperty = new BaseProperty<>(-1, "selectedCellIndexProperty");

        this.getChildrensProperty().addListener(obs ->
        {
            if (!this.getPlaceholderProperty().isPresent())
                return;

            if (this.getChildrensProperty().size() == 1 && !this.getPlaceholder().isVisible())
                this.getPlaceholder().setVisible(true);
            else if (this.getChildrensProperty().size() != 1 && this.getPlaceholder().isVisible())
                this.getPlaceholder().setVisible(false);
        });

        this.getTrueWidthProperty().bind(new BaseBinding<Float>()
        {
            {
                this.bind(orientationProperty, cellWidthProperty, cellXPaddingProperty, elementsProperty);
            }

            @Override
            public Float computeValue()
            {
                if (getOrientation().equals(RectAxis.HORIZONTAL))
                    return getCellWidth() * getElements().size() + getCellXPadding() * (getElements().size() - 1);
                return getCellWidth();
            }
        });

        this.getTrueHeightProperty().bind(new BaseBinding<Float>()
        {
            {
                this.bind(orientationProperty, cellHeightProperty, cellYPaddingProperty, elementsProperty);
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
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiListViewSkin<>(this, new GuiListViewBehavior<>(this, this::getChildrensProperty));
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

    public BaseProperty<RectAxis> getOrientationProperty()
    {
        return this.orientationProperty;
    }

    public BaseProperty<Function<T, GuiListCell<T>>> getCellFactoryProperty()
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
        return this.getElementsProperty().getValue();
    }

    public void setElements(final List<T> elements)
    {
        this.getElementsProperty().muteWhile(() ->
        {
            getElementsProperty().clear();
            getElementsProperty().addAll(elements);
        });
    }

    public void addElement(T element)
    {
        this.getElementsProperty().add(element);
    }

    public boolean removeElement(T element)
    {
        return this.getElementsProperty().remove(element);
    }

    public boolean hasElement(T element)
    {
        return this.getElementsProperty().contains(element);
    }

    public boolean isEditable()
    {
        return this.getEditableProperty().getValue();
    }

    public void setEditable(final boolean editable)
    {
        this.getEditableProperty().setValue(editable);
    }

    public GuiNode getPlaceholder()
    {
        return this.getPlaceholderProperty().getValue();
    }

    public void setPlaceholder(final GuiNode placeholder)
    {
        if (this.getPlaceholder() != null)
        {
            this.getPlaceholder().getxPosProperty().unbind();
            this.getPlaceholder().getyPosProperty().unbind();
            this.getPlaceholder().setFather(null);
            this.getChildrensProperty().remove(this.getPlaceholder());
        }

        this.getPlaceholderProperty().setValue(placeholder);

        if (this.getPlaceholder() != null)
        {
            RelativeBindingHelper.bindToCenter(placeholder, this);
            this.getPlaceholder().setFather(this);
            this.getChildrensProperty().add(this.getPlaceholder());

            if (this.getChildrensProperty().size() == 1)
                this.getPlaceholder().setVisible(true);
            else
                this.getPlaceholder().setVisible(false);
        }
    }

    public RectAxis getOrientation()
    {
        return this.getOrientationProperty().getValue();
    }

    public void setOrientation(final RectAxis orientation)
    {
        this.getOrientationProperty().setValue(orientation);
    }

    public boolean isEmpty()
    {
        return this.getElementsProperty().getValue().isEmpty();
    }

    public void setCellFactory(final Function<T, GuiListCell<T>> cellFactory)
    {
        this.getCellFactoryProperty().setValue(cellFactory);
    }

    public Function<T, GuiListCell<T>> getCellFactory()
    {
        if (!this.getCellFactoryProperty().isPresent())
            this.setCellFactory(this.getDefaultCellFactory());
        return this.getCellFactoryProperty().getValue();
    }

    private Function<T, GuiListCell<T>> getDefaultCellFactory()
    {
        return content ->
        {
            final GuiListCell<T> cell = new GuiListCell<>(this, content);

            if (content == null)
                cell.setGraphic(null);
            else if (content instanceof GuiNode)
                cell.setGraphic((GuiNode) content);
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
        return this.getCellWidthProperty().getValue();
    }

    public void setCellWidth(final float cellWidth)
    {
        this.getCellWidthProperty().setValue(cellWidth);
    }

    public float getCellHeight()
    {
        return this.getCellHeightProperty().getValue();
    }

    public void setCellHeight(final float cellHeight)
    {
        this.getCellHeightProperty().setValue(cellHeight);
    }

    public void setCellSize(float cellWidth, float cellHeight)
    {
        this.setCellWidth(cellWidth);
        this.setCellHeight(cellHeight);
    }

    public float getCellXPadding()
    {
        return this.getCellXPaddingProperty().getValue();
    }

    public void setCellXPadding(float cellXPadding)
    {
        this.getCellXPaddingProperty().setValue(cellXPadding);
    }

    public float getCellYPadding()
    {
        return this.getCellYPaddingProperty().getValue();
    }

    public void setCellYPadding(float cellYPadding)
    {
        this.getCellYPaddingProperty().setValue(cellYPadding);
    }

    public void setCellPadding(float cellXPadding, float cellYPadding)
    {
        this.setCellXPadding(cellXPadding);
        this.setCellYPadding(cellYPadding);
    }

    public int getSelectedCellIndex()
    {
        return this.getSelectedCellIndexProperty().getValue();
    }

    public void setSelectedCellIndex(final int selectedCell)
    {
        this.getSelectedCellIndexProperty().setValue(selectedCell);
    }
}