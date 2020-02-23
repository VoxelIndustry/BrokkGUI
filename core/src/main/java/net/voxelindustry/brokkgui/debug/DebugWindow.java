package net.voxelindustry.brokkgui.debug;

import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.gui.IGuiWindow;
import net.voxelindustry.brokkgui.immediate.ImmediateWindow;
import net.voxelindustry.brokkgui.immediate.style.BoxStyle;
import net.voxelindustry.brokkgui.immediate.style.TextStyle;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.ColorConstants;

import java.text.NumberFormat;

public class DebugWindow extends ImmediateWindow
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

    private final IGuiWindow window;

    public DebugWindow(IGuiWindow window)
    {
        this.window = window;

        this.setBoxStyle(BoxStyle.build()
                .setBoxColor(Color.BLACK.addAlpha(-0.5F))
                .setBorderColor(ColorConstants.getColor("steelblue"))
                .create());

        this.setTextStyle(TextStyle.build()
                .setTextColor(ColorConstants.getColor("gold"))
                .create());
    }

    @Override
    public void immediateRender()
    {
        if (!(window instanceof BrokkGuiScreen))
            return;

        box(0, 0, 100, getScreenHeight());

        drawHierarchy(((BrokkGuiScreen) window).getMainPanel(), 0, 0);
    }

    private int drawHierarchy(GuiFather node, int depth, int height)
    {
        int addedHeight = 0;
        for (GuiNode child : node.getChildrens())
        {
            text(DebugRenderer.getNodeName(child),
                    2 + depth * 5,
                    2 + (height + addedHeight) * getRenderer().getHelper().getStringHeight());
            addedHeight++;

            if (child instanceof GuiFather)
                addedHeight += drawHierarchy((GuiFather) child, depth + 1, height + addedHeight);
        }
        return addedHeight;
    }
}
