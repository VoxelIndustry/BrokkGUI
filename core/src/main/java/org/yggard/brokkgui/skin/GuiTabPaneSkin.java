package org.yggard.brokkgui.skin;

import org.yggard.brokkgui.behavior.GuiTabPaneBehavior;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.RenderPass;
import org.yggard.brokkgui.paint.Texture;
import org.yggard.brokkgui.panel.GuiTabPane;

/**
 * @author Ourten 15 oct. 2016
 */
public class GuiTabPaneSkin<T extends GuiTabPane> extends GuiBehaviorSkinBase<T, GuiTabPaneBehavior<T>>
{
    public GuiTabPaneSkin(final T model, final GuiTabPaneBehavior<T> behavior)
    {
        super(model, behavior);
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