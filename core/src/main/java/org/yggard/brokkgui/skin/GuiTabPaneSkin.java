package org.yggard.brokkgui.skin;

import fr.ourten.teabeans.binding.BaseBinding;
import fr.ourten.teabeans.value.Observable;
import fr.ourten.teabeans.value.ObservableValue;
import org.yggard.brokkgui.behavior.GuiTabPaneBehavior;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.component.GuiTab;
import org.yggard.brokkgui.element.GuiLabel;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.paint.Texture;
import org.yggard.brokkgui.panel.GuiTabPane;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiTabPaneSkin<T extends GuiTabPane> extends GuiBehaviorSkinBase<T, GuiTabPaneBehavior<T>>
{
    private List<GuiNode> tabHeaders;

    public GuiTabPaneSkin(final T model, final GuiTabPaneBehavior<T> behavior)
    {
        super(model, behavior);

        this.tabHeaders = new ArrayList<>();
        model.getTabsProperty().addListener(this::onTabChange);
        model.getTabs().forEach(this::createHeader);

        model.getTabHeaderWidthProperty().addListener(this::refreshAll);
        model.getTabHeaderHeightProperty().addListener(this::refreshAll);
    }

    private void refreshLayout(GuiNode from)
    {
        if(from != null)
        {
            
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

        header.getxPosProperty().bind(new BaseBinding<Float>()
        {
            {
                super.bind(getModel().get);
            }

            @Override
            public Float computeValue()
            {
                return null;
            }
        });

        tabHeaders.add(getModel().getTabIndex(guiTab), header);
    }

    private GuiNode defaultHeaderFactory(GuiTab guiTab, float maxWidth, float maxHeight)
    {
        GuiLabel label = new GuiLabel(guiTab.getText());
        label.getTextProperty().bind(guiTab.getTextProperty());

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

        this.getModel().getTabs().forEach(tab ->
        {
            switch (this.getModel().getTabSide())
            {
                case UP:
                    if (this.getModel().getBackgroundColor().getAlpha() != 0)
                        renderer.getHelper().drawColoredRect(renderer,
                                this.getModel().getxPos() + this.getModel().getxTranslate()
                                        + this.getModel().getWidth() / this.getModel().getTabsProperty().size()
                                        * this.getModel().getTabsProperty().indexOf(tab),
                                this.getModel().getyPos() + this.getModel().getyTranslate(),
                                this.getModel().getWidth() / this.getModel().getTabsProperty().size(),
                                this.getModel().getHeight() * this.getModel().getTabHeightRatio(),
                                this.getModel().getzLevel(), this.getModel().getBackgroundColor());
                    else if (this.getModel().getBackgroundTexture() != Texture.EMPTY)
                    {
                        final Texture texture = this.getModel().getBackgroundTexture();
                        renderer.getHelper().bindTexture(texture);
                        renderer.getHelper().drawTexturedRect(renderer,
                                this.getModel().getxPos() + this.getModel().getxTranslate()
                                        + this.getModel().getWidth() / this.getModel().getTabsProperty().size()
                                        * this.getModel().getTabsProperty().indexOf(tab),
                                this.getModel().getyPos() + this.getModel().getyTranslate(), texture.getUMin(),
                                texture.getVMin(), texture.getUMax(), texture.getVMax(),
                                this.getModel().getWidth() / this.getModel().getTabsProperty().size(),
                                this.getModel().getHeight() * this.getModel().getTabHeightRatio(),
                                this.getModel().getzLevel());
                    }
                    renderer.getHelper().drawColoredEmptyRect(renderer,
                            this.getModel().getxPos() + this.getModel().getxTranslate()
                                    + this.getModel().getWidth() / this.getModel().getTabsProperty().size()
                                    * this.getModel().getTabsProperty().indexOf(tab),
                            this.getModel().getyPos() + this.getModel().getyTranslate(),
                            this.getModel().getWidth() / this.getModel().getTabsProperty().size(),
                            this.getModel().getHeight() * this.getModel().getTabHeightRatio(),
                            this.getModel().getzLevel(), this.getBorderColor(), this.getBorderThin());
                    renderer.getHelper().drawString(tab.getText(),
                            this.getModel().getxPos() + this.getModel().getxTranslate() + 2
                                    + this.getModel().getWidth() / this.getModel().getTabsProperty().size()
                                    * this.getModel().getTabsProperty().indexOf(tab),
                            this.getModel().getyPos() + this.getModel().getyTranslate() + 2,
                            this.getModel().getzLevel(), Color.WHITE);
                    break;
                // TODO : tab render for every possible sides
                case DOWN:
                    break;
                case LEFT:
                    break;
                case RIGHT:
                    break;
                default:
                    break;
            }
        });
        if (this.getModel().getSelectedTab() != null)
            this.getModel().getSelectedTab().renderChild(renderer, pass, mouseX, mouseY);
    }
}