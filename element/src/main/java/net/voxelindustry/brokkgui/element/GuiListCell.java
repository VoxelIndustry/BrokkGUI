package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.data.RectAxis;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.skin.GuiListCellSkin;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

public class GuiListCell<T> extends GuiElement
{
    private final GuiListView<T> listView;

    private final BaseProperty<T>       itemProperty;
    private final BaseProperty<GuiNode> graphicProperty;

    public GuiListCell(final GuiListView<T> listView, final T item)
    {
        super("listcell");

        this.listView = listView;

        this.itemProperty = new BaseProperty<>(item, "itemProperty");
        this.graphicProperty = new BaseProperty<>(null, "graphicProperty");

        this.getWidthProperty().bind(listView.getCellWidthProperty());
        this.getHeightProperty().bind(listView.getCellHeightProperty());

        this.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(listView.getxPosProperty(), getWidthProperty(),
                        listView.getOrientationProperty(), listView.getScrollXProperty(),
                        listView.getCellXPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                if (listView.getOrientation() == RectAxis.HORIZONTAL)
                    return listView.getxPos() + listView.getScrollX()
                            + listView.getElements().indexOf(GuiListCell.this.getItem()) * GuiListCell.this.getWidth()
                            + GuiListCell.this.getWidth() / 2
                            + listView.getCellXPadding() * listView.getElements().indexOf(GuiListCell.this.getItem());
                else
                    return listView.getxPos() + listView.getScrollX();
            }
        });

        this.getyPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(listView.getyPosProperty(), GuiListCell.this.getHeightProperty(),
                        listView.getOrientationProperty(), listView.getScrollYProperty(),
                        listView.getCellYPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                if (listView.getOrientation() == RectAxis.VERTICAL)
                    return listView.getyPos() + listView.getScrollY()
                            + listView.getElements().indexOf(GuiListCell.this.getItem()) * GuiListCell.this.getHeight()
                            + listView.getCellYPadding() * listView.getElements().indexOf(GuiListCell.this.getItem());
                else
                    return listView.getyPos() + listView.getScrollY();
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

    public void setItem(T item)
    {
        this.getItemProperty().setValue(item);
    }

    public GuiNode getGraphic()
    {
        return this.getGraphicProperty().getValue();
    }

    public void setGraphic(GuiNode graphic)
    {
        if (this.getGraphic() != null)
        {
            this.removeChild(this.getGraphic());
            this.getGraphic().getWidthProperty().unbind();
            this.getGraphic().getHeightProperty().unbind();
        }
        this.getGraphicProperty().setValue(graphic);
        if (this.getGraphic() != null)
        {
            this.addChild(this.getGraphic());

            RelativeBindingHelper.bindToPos(this.getGraphic(), this);
            this.getGraphic().getWidthProperty().bind(this.getWidthProperty());
            this.getGraphic().getHeightProperty().bind(this.getHeightProperty());
        }
    }

    public GuiListView<T> getListView()
    {
        return this.listView;
    }
}