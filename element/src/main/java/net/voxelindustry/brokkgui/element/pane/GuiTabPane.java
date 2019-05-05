package net.voxelindustry.brokkgui.element.pane;

import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import net.voxelindustry.brokkgui.behavior.GuiTabPaneBehavior;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.element.TabHeaderFactory;
import net.voxelindustry.brokkgui.policy.GuiOverflowPolicy;
import net.voxelindustry.brokkgui.shape.ScissorBox;
import net.voxelindustry.brokkgui.skin.GuiSkinBase;
import net.voxelindustry.brokkgui.skin.GuiTabPaneSkin;
import net.voxelindustry.brokkgui.style.tree.StyleList;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiTabPane extends GuiElement
{
    private final BaseListProperty<GuiTab> tabsProperty;
    private final BaseProperty<Integer>    selectedTabProperty, defaultTabProperty;
    private final BaseProperty<RectSide> sideProperty;

    private final BaseProperty<Float> tabHeaderWidthProperty;
    private final BaseProperty<Float> tabHeaderHeightProperty;

    private final Map<GuiTab, TabHeaderFactory> tabHeaderFactories;
    private       TabHeaderFactory              globalTabHeaderFactory;

    public GuiTabPane()
    {
        super("tabpane");

        this.tabsProperty = new BaseListProperty<>(null, "tabsProperty");
        this.selectedTabProperty = new BaseProperty<>(-1, "selectedTabProperty");
        this.defaultTabProperty = new BaseProperty<>(-1, "defaultTabProperty");
        this.sideProperty = new BaseProperty<>(RectSide.UP, "sideProperty");

        this.tabHeaderWidthProperty = new BaseProperty<>(-1f, "tabHeaderWidthProperty");
        this.tabHeaderHeightProperty = new BaseProperty<>(20f, "tabHeaderHeightProperty");

        this.setScissorBox(ScissorBox.fitNode(this));
        this.setGuiOverflow(GuiOverflowPolicy.TRIM_ALL);

        this.tabHeaderFactories = new HashMap<>();
    }

    @Override
    protected GuiSkinBase<?> makeDefaultSkin()
    {
        return new GuiTabPaneSkin<>(this, new GuiTabPaneBehavior<>(this));
    }

    public BaseListProperty<GuiTab> getTabsProperty()
    {
        return this.tabsProperty;
    }

    public BaseProperty<Integer> getSelectedTabProperty()
    {
        return this.selectedTabProperty;
    }

    public BaseProperty<Integer> getDefaultTabProperty()
    {
        return this.defaultTabProperty;
    }

    public BaseProperty<RectSide> getSideProperty()
    {
        return this.sideProperty;
    }

    public BaseProperty<Float> getTabHeaderWidthProperty()
    {
        return tabHeaderWidthProperty;
    }

    public BaseProperty<Float> getTabHeaderHeightProperty()
    {
        return tabHeaderHeightProperty;
    }

    /**
     * @return an immutable list
     */
    public List<GuiTab> getTabs()
    {
        return this.getTabsProperty().getValue();
    }

    public void addTab(final GuiTab tab)
    {
        this.setupTab(tab);
        this.getTabsProperty().add(tab);
    }

    public void addTab(final GuiTab tab, final int index)
    {
        this.setupTab(tab);
        this.getTabsProperty().add(index, tab);
    }

    public void addTabs(final GuiTab... tabs)
    {
        final List<GuiTab> tabsList = Arrays.asList(tabs);
        tabsList.forEach(this::setupTab);
        this.getTabsProperty().addAll(tabsList);
    }

    public void removeTab(final GuiTab tab)
    {
        this.disposeTab(tab);
        this.getTabsProperty().remove(tab);
    }

    public GuiTab getTab(final int index)
    {
        if (index != -1 && index < this.getTabsProperty().size())
            return this.getTabsProperty().get(index);
        return null;
    }

    public int getTabIndex(GuiTab tab)
    {
        return this.getTabs().indexOf(tab);
    }

    private void setupTab(final GuiTab tab)
    {
        tab.setTabPane(this);
    }

    private void disposeTab(final GuiTab tab)
    {
        tab.setTabPane(null);
    }

    public int getSelectedTabIndex()
    {
        return this.getSelectedTabProperty().getValue();
    }

    public GuiTab getSelectedTab()
    {
        if (this.getSelectedTabIndex() == -1 && this.getDefaultTabIndex() != -1)
            this.setSelectedTab(this.getDefaultTabIndex());
        if (this.getSelectedTabIndex() != -1 && this.getSelectedTabIndex() < this.getTabsProperty().size())
            return this.tabsProperty.get(this.getSelectedTabIndex());
        return null;
    }

    public void setSelectedTab(final int index)
    {
        this.getTabs().stream().filter(GuiTab::isSelected).forEach(tab ->
        {
            tab.setSelected(false);
            this.getChildrensProperty().remove(tab.getContent());
        });

        if (this.getTab(index) != null)
        {
            this.getTab(index).setSelected(true);
            this.getChildrensProperty().add(this.getTab(index).getContent());
        }
        this.getSelectedTabProperty().setValue(index);
    }

    public void setSelectedTab(final GuiTab tab)
    {
        this.setSelectedTab(this.tabsProperty.indexOf(tab));
    }

    public int getDefaultTabIndex()
    {
        return this.getDefaultTabProperty().getValue();
    }

    public GuiTab getDefaultTab()
    {
        if (this.getDefaultTabIndex() != -1 && this.getTabsProperty().size() < this.getDefaultTabIndex())
            return this.tabsProperty.get(this.getDefaultTabIndex());
        return null;
    }

    public void setDefaultTab(final int index)
    {
        this.getDefaultTabProperty().setValue(index);
    }

    public void setDefaultTab(final GuiTab tab)
    {
        this.setDefaultTab(this.tabsProperty.indexOf(tab));
    }

    public RectSide getTabSide()
    {
        return this.getSideProperty().getValue();
    }

    public void setTabSide(final RectSide side)
    {
        this.getSideProperty().setValue(side);
    }

    public float getTabHeaderWidth()
    {
        return this.getTabHeaderWidthProperty().getValue();
    }

    /**
     * Control the maximum width of tab headers, setting it to -1 use the default width of each elements
     *
     * @param tabHeaderWidth
     */
    public void setTabHeaderWidth(float tabHeaderWidth)
    {
        this.getTabHeaderWidthProperty().setValue(tabHeaderWidth);
    }

    public float getTabHeaderHeight()
    {
        return this.getTabHeaderHeightProperty().getValue();
    }

    /**
     * Control the maximum height of tab headers, setting it to -1 use the default height of each elements
     *
     * @param tabHeaderHeight
     */
    public void setTabHeaderHeight(float tabHeaderHeight)
    {
        this.getTabHeaderHeightProperty().setValue(tabHeaderHeight);
    }

    public void setGlobalTabHeaderFactory(TabHeaderFactory factory)
    {
        this.globalTabHeaderFactory = factory;
    }

    public TabHeaderFactory getGlobalTabHeaderFactory()
    {
        return this.globalTabHeaderFactory;
    }

    public void setTabHeaderFactory(GuiTab tab, TabHeaderFactory factory)
    {
        this.tabHeaderFactories.put(tab, factory);
    }

    public void removeTabHeaderFactory(GuiTab tab)
    {
        this.tabHeaderFactories.remove(tab);
    }

    public Optional<TabHeaderFactory> getTabHeaderFactory(GuiTab tab)
    {
        return Optional.ofNullable(this.tabHeaderFactories.getOrDefault(tab, this.globalTabHeaderFactory));
    }

    /////////////////////
    // STYLING //
    /////////////////////

    @Override
    public void setStyleTree(Supplier<StyleList> treeSupplier)
    {
        super.setStyleTree(treeSupplier);

        this.getTabs().forEach(node ->
        {
            if (node.getContent() != null)
                node.getContent().setStyleTree(treeSupplier);
        });
    }

    @Override
    public void refreshStyle()
    {
        super.refreshStyle();

        this.getTabs().forEach(node ->
        {
            if (node.getContent() != null)
                node.getContent().refreshStyle();
        });
    }
}