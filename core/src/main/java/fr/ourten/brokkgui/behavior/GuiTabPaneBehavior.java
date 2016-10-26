package fr.ourten.brokkgui.behavior;

import fr.ourten.brokkgui.component.GuiTab;
import fr.ourten.brokkgui.event.ClickEvent;
import fr.ourten.brokkgui.panel.GuiTabPane;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiTabPaneBehavior<T extends GuiTabPane> extends GuiBehaviorBase<T>
{
    public GuiTabPaneBehavior(final T model)
    {
        super(model);

        this.getModel().getEventDispatcher().addHandler(ClickEvent.TYPE, this::onClick);
    }

    public void onClick(final ClickEvent event)
    {
        final GuiTab tab = this.getClickedTab(event.getMouseX(), event.getMouseY());

        if (tab != null && this.getModel().getSelectedTab() != tab)
            this.getModel().setSelectedTab(tab);
        else if (tab == null && this.getModel().getSelectedTab() != null
                && this.getModel().getSelectedTab().getContent() != null)
            this.getModel().getSelectedTab().getContent().handleClick(event.getMouseX(), event.getMouseY(),
                    event.getKey());
    }

    private GuiTab getClickedTab(final int pointX, final int pointY)
    {
        GuiTab rtn = null;
        float index = -1;

        switch (this.getModel().getTabSide())
        {
            case DOWN:
                if (pointY > this.getModel().getyPos() + this.getModel().getyTranslate()
                        + 9 * (this.getModel().getHeight() / 10))
                    index = (pointX - this.getModel().getxPos() - this.getModel().getxTranslate())
                            / this.getModel().getWidth() * this.getModel().getTabsProperty().size();
                break;
            case LEFT:
                if (pointX < this.getModel().getxPos() + this.getModel().getxTranslate()
                        + this.getModel().getWidth() / 10)
                    index = (pointY - this.getModel().getyPos() - this.getModel().getyTranslate())
                            / this.getModel().getHeight() * this.getModel().getTabsProperty().size();
                break;
            case RIGHT:
                if (pointX > this.getModel().getxPos() + this.getModel().getxTranslate()
                        + 9 * (this.getModel().getWidth() / 10))
                    index = (pointY - this.getModel().getyPos() - this.getModel().getyTranslate())
                            / this.getModel().getHeight() * this.getModel().getTabsProperty().size();
                break;
            case UP:
                if (pointY < this.getModel().getyPos() + this.getModel().getyTranslate()
                        + this.getModel().getHeight() / 10)
                    index = (pointX - this.getModel().getxPos() - this.getModel().getxTranslate())
                            / this.getModel().getWidth() * this.getModel().getTabsProperty().size();
                break;
            default:
                break;
        }
        rtn = this.getModel().getTab((int) index);
        return rtn;
    }
}