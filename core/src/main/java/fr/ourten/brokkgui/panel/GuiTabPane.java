package fr.ourten.brokkgui.panel;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import fr.ourten.brokkgui.behavior.GuiTabPaneBehavior;
import fr.ourten.brokkgui.component.GuiTab;
import fr.ourten.brokkgui.control.GuiControl;
import fr.ourten.brokkgui.data.ESide;
import fr.ourten.brokkgui.policy.EOverflowPolicy;
import fr.ourten.brokkgui.skin.GuiSkinBase;
import fr.ourten.brokkgui.skin.GuiTabPaneSkin;
import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiTabPane extends GuiControl
{
    private final BaseListProperty<GuiTab> tabsProperty;
    private final BaseProperty<Integer>    selectedTabProperty, defaultTabProperty;
    private final BaseProperty<ESide>      sideProperty;

    public GuiTabPane()
    {
        this.tabsProperty = new BaseListProperty<>(null, "tabsProperty");
        this.selectedTabProperty = new BaseProperty<>(-1, "selectedTabProperty");
        this.defaultTabProperty = new BaseProperty<>(-1, "defaultTabProperty");
        this.sideProperty = new BaseProperty<>(ESide.UP, "sideProperty");

        this.setOverflowPolicy(EOverflowPolicy.TRIM_ALL);
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

    public BaseProperty<ESide> getSideProperty()
    {
        return this.sideProperty;
    }

    public ImmutableList<GuiTab> getTabs()
    {
        return this.tabsProperty.getValue();
    }

    public void addTab(final GuiTab tab)
    {
        this.setupTab(tab);
        this.tabsProperty.add(tab);
    }

    public void addTab(final GuiTab tab, final int index)
    {
        this.setupTab(tab);
        this.tabsProperty.add(index, tab);
    }

    public void addTabs(final GuiTab... tabs)
    {
        final List<GuiTab> tabsList = Arrays.asList(tabs);
        tabsList.forEach(this::setupTab);
        this.tabsProperty.addAll(tabsList);
    }

    public void removeTab(final GuiTab tab)
    {
        this.disposeTab(tab);
        this.tabsProperty.remove(tab);
    }

    public GuiTab getTab(final int index)
    {
        if (index != -1 && index < this.getTabsProperty().size())
            return this.tabsProperty.get(index);
        return null;
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
        return this.selectedTabProperty.getValue();
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
        this.getTabs().stream().filter(tab -> tab.isSelected()).forEach(tab -> tab.setSelected(false));
        if (this.getTab(index) != null)
            this.getTab(index).setSelected(true);
        this.selectedTabProperty.setValue(index);
    }

    public void setSelectedTab(final GuiTab tab)
    {
        this.setSelectedTab(this.tabsProperty.indexOf(tab));
    }

    public int getDefaultTabIndex()
    {
        return this.defaultTabProperty.getValue();
    }

    public GuiTab getDefaultTab()
    {
        if (this.getDefaultTabIndex() != -1 && this.getTabsProperty().size() < this.getDefaultTabIndex())
            return this.tabsProperty.get(this.getDefaultTabIndex());
        return null;
    }

    public void setDefaultTab(final int index)
    {
        this.defaultTabProperty.setValue(index);
    }

    public void setDefaultTab(final GuiTab tab)
    {
        this.setDefaultTab(this.tabsProperty.indexOf(tab));
    }

    public ESide getTabSide()
    {
        return this.sideProperty.getValue();
    }

    public void setTabSide(final ESide side)
    {
        this.sideProperty.setValue(side);
    }
}