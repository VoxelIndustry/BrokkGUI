package org.yggard.brokkgui.panel;

import java.util.Arrays;
import java.util.List;

import org.yggard.brokkgui.behavior.GuiTabPaneBehavior;
import org.yggard.brokkgui.component.GuiTab;
import org.yggard.brokkgui.control.GuiControl;
import org.yggard.brokkgui.data.ESide;
import org.yggard.brokkgui.policy.EOverflowPolicy;
import org.yggard.brokkgui.skin.GuiSkinBase;
import org.yggard.brokkgui.skin.GuiTabPaneSkin;

import com.google.common.collect.ImmutableList;

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

    private final BaseProperty<Float>      tabHeightRatioProperty;

    public GuiTabPane()
    {
        this.tabsProperty = new BaseListProperty<>(null, "tabsProperty");
        this.selectedTabProperty = new BaseProperty<>(-1, "selectedTabProperty");
        this.defaultTabProperty = new BaseProperty<>(-1, "defaultTabProperty");
        this.sideProperty = new BaseProperty<>(ESide.UP, "sideProperty");

        this.tabHeightRatioProperty = new BaseProperty<>(.1f, "tabHeightRatioProperty");

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

    public BaseProperty<Float> getTabHeightRatioProperty()
    {
        return this.tabHeightRatioProperty;
    }

    public ImmutableList<GuiTab> getTabs()
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
        this.getTabs().stream().filter(tab -> tab.isSelected()).forEach(tab -> tab.setSelected(false));
        if (this.getTab(index) != null)
            this.getTab(index).setSelected(true);
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

    public ESide getTabSide()
    {
        return this.getSideProperty().getValue();
    }

    public void setTabSide(final ESide side)
    {
        this.getSideProperty().setValue(side);
    }

    public float getTabHeightRatio()
    {
        return this.getTabHeightRatioProperty().getValue();
    }

    public void setTabHeightRatio(final float tabHeight)
    {
        this.getTabHeightRatioProperty().setValue(tabHeight);
    }
}