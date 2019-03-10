package net.voxelindustry.brokkgui.debug;

import fr.ourten.teabeans.binding.BaseExpression;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.gui.IGuiWindow;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.ColorConstants;
import net.voxelindustry.brokkgui.panel.GuiAbsolutePane;

import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

public class DebugWindow extends BrokkGuiScreen
{
    private static final NumberFormat FORMAT;

    static
    {
        FORMAT = NumberFormat.getInstance();
        FORMAT.setMinimumFractionDigits(0);
    }

    public static final Color BORDER_COLOR       = ColorConstants.getColor("steelblue");
    public static final Color TEXT_COLOR         = ColorConstants.getColor("gold");
    public static final Color HOVERED_TEXT_COLOR = ColorConstants.getColor("gold").shade(0.2f);
    public static final Color BOX_COLOR          = Color.BLACK.addAlpha(-0.5f);

    private DebugLayoutPanel debugLayoutPanel;
    private IGuiWindow       window;

    public DebugWindow(IGuiWindow window)
    {
        super(0.5f, 0.5f, 0, 0);
        this.getWidthProperty().bind(BaseExpression.transform(this.getScreenWidthProperty(), Integer::floatValue));
        this.getHeightProperty().bind(BaseExpression.transform(this.getScreenHeightProperty(), Integer::floatValue));
        this.window = window;

        GuiAbsolutePane mainPanel = new GuiAbsolutePane();
        this.setMainPanel(mainPanel);

        this.debugLayoutPanel = new DebugLayoutPanel();
        debugLayoutPanel.setHeightRatio(1);
        debugLayoutPanel.setWidth(200);
        mainPanel.addChild(debugLayoutPanel,0,0);

        this.runLater(this::updateLayout, 500, TimeUnit.MILLISECONDS);
    }

    private void updateLayout()
    {
        this.debugLayoutPanel.updateLayout(window);
        this.runLater(this::updateLayout, 500, TimeUnit.MILLISECONDS);
    }
}
