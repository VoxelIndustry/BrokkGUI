package net.voxelindustry.brokkgui.element;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.control.GuiSkinedElement;
import net.voxelindustry.brokkgui.data.RectAxis;
import net.voxelindustry.brokkgui.data.RelativeBindingHelper;
import net.voxelindustry.brokkgui.skin.GuiListCellSkin;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;

public class GuiListCell<T> extends GuiSkinedElement
{
    private final GuiListView<T> listView;

    private final BaseProperty<T>          itemProperty;
    private final BaseProperty<GuiElement> graphicProperty;

    public GuiListCell(GuiListView<T> listView, T item)
    {
        this.listView = listView;

        itemProperty = new BaseProperty<>(item, "itemProperty");
        graphicProperty = new BaseProperty<>(null, "graphicProperty");

        transform().widthProperty().bind(listView.getCellWidthProperty());
        transform().heightProperty().bind(listView.getCellHeightProperty());

        transform().xPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(listView.transform().xPosProperty(), transform().widthProperty(),
                        listView.getOrientationProperty(), listView.getScrollXProperty(),
                        listView.getCellXPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                if (listView.getOrientation() == RectAxis.HORIZONTAL)
                    return listView.transform().xPos() + listView.getScrollX()
                            + listView.getElements().indexOf(getItem()) * transform().width()
                            + transform().width() / 2
                            + listView.getCellXPadding() * listView.getElements().indexOf(getItem());
                else
                    return listView.transform().xPos() + listView.getScrollX();
            }
        });

        transform().yPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(listView.transform().yPosProperty(), transform().heightProperty(),
                        listView.getOrientationProperty(), listView.getScrollYProperty(),
                        listView.getCellYPaddingProperty());
            }

            @Override
            public Float computeValue()
            {
                if (listView.getOrientation() == RectAxis.VERTICAL)
                    return listView.transform().yPos() + listView.getScrollY()
                            + listView.getElements().indexOf(getItem()) * transform().height()
                            + listView.getCellYPadding() * listView.getElements().indexOf(getItem());
                else
                    return listView.transform().yPos() + listView.getScrollY();
            }
        });
    }

    public GuiListCell(GuiListView<T> listView)
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
        return itemProperty;
    }

    public BaseProperty<GuiElement> getGraphicProperty()
    {
        return graphicProperty;
    }

    public T getItem()
    {
        return getItemProperty().getValue();
    }

    public void setItem(T item)
    {
        getItemProperty().setValue(item);
    }

    public GuiElement getGraphic()
    {
        return getGraphicProperty().getValue();
    }

    public void setGraphic(GuiElement graphic)
    {
        if (getGraphic() != null)
        {
            removeChild(getGraphic());
            getGraphic().transform().widthProperty().unbind();
            getGraphic().transform().heightProperty().unbind();
        }
        getGraphicProperty().setValue(graphic);
        if (getGraphic() != null)
        {
            addChild(getGraphic());

            RelativeBindingHelper.bindToPos(getGraphic().transform(), transform());
            getGraphic().transform().widthProperty().bind(transform().widthProperty());
            getGraphic().transform().heightProperty().bind(transform().heightProperty());
        }
    }

    public GuiListView<T> getListView()
    {
        return listView;
    }

    @Override
    public String type()
    {
        return "list-cell";
    }
}