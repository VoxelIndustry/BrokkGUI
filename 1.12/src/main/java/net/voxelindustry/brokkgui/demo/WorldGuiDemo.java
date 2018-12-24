package net.voxelindustry.brokkgui.demo;

import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.panel.GuiRelativePane;

public class WorldGuiDemo extends BrokkGuiScreen
{
    public WorldGuiDemo()
    {
        super(0.5f, 0.5f, 256, 256);

        this.addStylesheet("/assets/brokkguidemo/gui/css/demo.css");

        GuiRelativePane pane = new GuiRelativePane();
        this.setMainPanel(pane);

        pane.setBackgroundTexture(new Texture("brokkguidemo:textures/gui/background.png"));

        GuiLabel label = new GuiLabel("Oh hey it's me!");
        label.setScale(2);
        label.setID("pretty-label");

        pane.addChild(label, 0.5f, 0.5f);
    }
}
