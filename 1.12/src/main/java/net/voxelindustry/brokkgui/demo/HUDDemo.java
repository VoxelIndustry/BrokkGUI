package net.voxelindustry.brokkgui.demo;

import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.panel.GuiRelativePane;

public class HUDDemo extends BrokkGuiScreen
{
    public HUDDemo()
    {
        super(0.2f, 0.2f, 96, 96);

        this.addStylesheet("/assets/brokkguidemo/gui/css/demo.css");

        GuiRelativePane pane = new GuiRelativePane();
        this.setMainPanel(pane);

        pane.setBackgroundTexture(new Texture("brokkguidemo:textures/gui/background.png"));

        GuiLabel label = new GuiLabel("I'm on a HUD now!");
        label.setID("pretty-label");

        pane.addChild(label, 0.5f, 0.5f);
    }
}
