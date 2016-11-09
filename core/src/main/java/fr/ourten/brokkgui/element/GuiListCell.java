package fr.ourten.brokkgui.element;

import fr.ourten.brokkgui.component.GuiNode;
import fr.ourten.brokkgui.control.GuiLabeled;
import fr.ourten.brokkgui.data.EOrientation;
import fr.ourten.brokkgui.skin.GuiListCellSkin;
import fr.ourten.brokkgui.skin.GuiSkinBase;
import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;

public class GuiListCell<T> extends GuiLabeled
{
    private final GuiListView<T>        listView;

    private final BaseProperty<T>       itemProperty;
    private final BaseProperty<GuiNode> graphicProperty;

    public GuiListCell(final GuiListView<T> listView, final T item)
    {
        this.listView = listView;

        this.itemProperty = new BaseProperty<>(item, "itemProperty");
        this.graphicProperty = new BaseProperty<>(null, "graphicProperty");

        this.getWidthProperty().bind(listView.getCellWidthProperty());
        this.getHeightProperty().bind(listView.getCellHeightProperty());

        this.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(listView.getxPosProperty(), GuiListCell.this.getWidthProperty(),
                        listView.getOrientationProperty());
            }

            @Override
            public Float computeValue()
            {
                if (listView.getOrientation() == EOrientation.HORIZONTAL)
                    return listView.getxPos()
                            + listView.getElements().indexOf(GuiListCell.this.getItem()) * GuiListCell.this.getWidth()
                            + GuiListCell.this.getWidth() / 2;
                else
                    return listView.getxPos();
            }
        });

        this.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(listView.getyPosProperty(), GuiListCell.this.getHeightProperty(),
                        listView.getOrientationProperty());
            }

            @Override
            public Float computeValue()
            {
                if (listView.getOrientation() == EOrientation.VERTICAL)
                    return listView.getyPos()
                            + listView.getElements().indexOf(GuiListCell.this.getItem()) * GuiListCell.this.getHeight();
                else
                    return listView.getyPos();
            }
        });
    }

    public GuiListCell(final GuiListView<T> listView)
    {
        this(listView, null);
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiListCellSkin<>(this);
    }

    public BaseProperty<T> getItemProperty()
    {
        return this.itemProperty;
    }

    public BaseProperty<GuiNode> getGraphicProperty()
    {
        return this.graphicProperty;
    }

    public T getItem()
    {
        return this.getItemProperty().getValue();
    }

    public void setItem(final T item)
    {
        this.getItemProperty().setValue(item);
    }

    public GuiNode getGraphic()
    {
        return this.getGraphicProperty().getValue();
    }

    public void setGraphic(final GuiNode graphic)
    {
        if (this.getGraphic() != null)
        {
            this.getGraphic().setFather(null);
            this.getChildrensProperty().remove(this.getGraphic());
        }
        if (this.getGraphic() != null)
        {
            this.getGraphic().getxPosProperty().unbind();
            this.getGraphic().getyPosProperty().unbind();
            this.getGraphic().getWidthProperty().unbind();
            this.getGraphic().getHeightProperty().unbind();
        }
        this.getGraphicProperty().setValue(graphic);
        if (this.getGraphic() != null)
        {
            this.getGraphic().getxPosProperty().bind(this.getxPosProperty());
            this.getGraphic().getyPosProperty().bind(this.getyPosProperty());
            this.getGraphic().getWidthProperty().bind(this.getWidthProperty());
            this.getGraphic().getHeightProperty().bind(this.getHeightProperty());
            this.getChildrensProperty().add(graphic);
            graphic.setFather(this);
        }
    }

    public GuiListView<T> getListView()
    {
        return this.listView;
    }
}