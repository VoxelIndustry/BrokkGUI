package net.voxelindustry.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseProperty;
import fr.ourten.teabeans.value.Observable;
import fr.ourten.teabeans.value.ObservableValue;
import net.voxelindustry.brokkgui.behavior.GuiTabPaneBehavior;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.component.GuiTab;
import net.voxelindustry.brokkgui.data.RectOffset;
import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.event.ClickEvent;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.RenderPass;
import net.voxelindustry.brokkgui.panel.GuiTabPane;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiTabPaneSkin<T extends GuiTabPane> extends GuiBehaviorSkinBase<T, GuiTabPaneBehavior<T>>
{
    private List<GuiNode>         tabHeaders;
    private BaseProperty<Boolean> headerDirtyProperty;

    public GuiTabPaneSkin(final T model, final GuiTabPaneBehavior<T> behavior)
    {
        super(model, behavior);

        this.headerDirtyProperty = new BaseProperty<>(Boolean.FALSE, "headerDirtyProperty");

        this.tabHeaders = new ArrayList<>();
        model.getTabsProperty().addListener(this::onTabChange);
        model.getTabs().forEach(this::createHeader);

        model.getTabHeaderWidthProperty().addListener(this::refreshAll);
        model.getTabHeaderHeightProperty().addListener(this::refreshAll);

        getModel().getEventDispatcher().addHandler(ClickEvent.TYPE, this::onClick);
    }

    private void refreshLayout(GuiNode from)
    {
        if (from != null)
        {
            int index = tabHeaders.indexOf(from);
            float lastSpacing = (float) tabHeaders.stream()
                    .filter(header -> tabHeaders.indexOf(header) < index)
                    .mapToDouble(GuiNode::getWidth).sum();

            for (GuiNode header : tabHeaders.subList(index + 1, tabHeaders.size()))
                header.getxPosProperty().setValue(getModel().getxPos() + getModel().getxTranslate() + lastSpacing);
        }
    }

    private void refreshAll(Observable obs)
    {
        this.tabHeaders.forEach(this::disposeHeader);
        this.tabHeaders.clear();
        getModel().getTabs().forEach(this::createHeader);
    }

    private void disposeHeader(GuiNode header)
    {
        header.getxPosProperty().unbind();
        header.getyPosProperty().unbind();
        header.getWidthProperty().unbind();
        header.getHeightProperty().unbind();

        getModel().removeStyleChild(header);
    }

    private void onTabChange(ObservableValue<?> observableValue, GuiTab oldTab, GuiTab newTab)
    {
        if (oldTab != null)
        {
            this.disposeHeader(tabHeaders.get(getModel().getTabIndex(oldTab)));
            tabHeaders.remove(getModel().getTabIndex(oldTab));
        }
        if (newTab != null)
            this.createHeader(newTab);
    }

    private void createHeader(GuiTab guiTab)
    {
        GuiNode header = getModel().getTabHeaderFactory(guiTab).orElse(this::defaultHeaderFactory)
                .create(guiTab, getModel().getTabHeaderWidth(), getModel().getTabHeaderHeight());

        header.addStyleClass("tab-header");
        getModel().addStyleChild(header);

        header.getWidthProperty().addListener(this::onHeaderWidthChange);
        header.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().getxPosProperty(), getModel().getxTranslateProperty(), headerDirtyProperty);
            }

            @Override
            public Float computeValue()
            {
                float lastSpacing = (float) tabHeaders.stream()
                        .filter(candidate -> tabHeaders.indexOf(candidate) < getModel().getTabIndex(guiTab))
                        .mapToDouble(GuiNode::getWidth).sum();
                return getModel().getxPos() + getModel().getxTranslate() + lastSpacing;
            }
        });
        header.getyPosProperty().bind(BaseExpression.biCombine(getModel().getyPosProperty(),
                getModel().getyTranslateProperty(), (y, translate) -> y + translate));

        tabHeaders.add(getModel().getTabIndex(guiTab), header);
    }

    private void onHeaderWidthChange(Observable obs)
    {
        this.headerDirtyProperty.setValue(true);
    }

    private GuiNode defaultHeaderFactory(GuiTab guiTab, float maxWidth, float maxHeight)
    {
        GuiLabel label = new GuiLabel(guiTab.getText());
        label.getTextProperty().bind(guiTab.getTextProperty());
        label.setTextPadding(new RectOffset(0, 2, 0, 2));
        if (maxWidth != -1)
            label.setWidth(maxWidth);
        else
            label.setExpandToText(true);
        if (maxHeight != -1)
            label.setHeight(maxHeight);
        else
            label.setExpandToText(true);
        return label;
    }

    @Override
    public void render(final RenderPass pass, final IGuiRenderer renderer, final int mouseX, final int mouseY)
    {
        super.render(pass, renderer, mouseX, mouseY);

        this.tabHeaders.forEach(node -> node.renderNode(renderer, pass, mouseX, mouseY));

        if (this.getModel().getSelectedTab() != null)
            this.getModel().getSelectedTab().renderChild(renderer, pass, mouseX, mouseY);
    }

    ////////////
    // EVENTS //
    ////////////

    public void onClick(ClickEvent event)
    {
        GuiTab tab = this.getClickedTab(event.getMouseX(), event.getMouseY());

        if (tab != null && getModel().getSelectedTab() != tab)
            getModel().setSelectedTab(tab);
    }

    private GuiTab getClickedTab(int pointX, int pointY)
    {
        return this.tabHeaders.stream().filter(node -> node.isPointInside(pointX, pointY))
                .map(header -> getModel().getTab(tabHeaders.indexOf(header))).findFirst().orElse(null);
    }
}