package net.voxelindustry.brokkgui.debug;

import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.gui.GuiCompositeWindow;
import org.apache.commons.lang3.StringUtils;

public class DebugRenderer
{
    public static DebugWindow wrap(BrokkGuiScreen brokkGuiScreen)
    {
        DebugWindow debugWindow = new DebugWindow(brokkGuiScreen);
        GuiCompositeWindow composite = new GuiCompositeWindow(brokkGuiScreen, debugWindow);
        debugWindow.setWrapper(brokkGuiScreen.getWrapper());
        brokkGuiScreen.getWrapper().setGuiWindow(composite);
        return debugWindow;
    }

    public static String getNodeName(GuiNode node)
    {
        StringBuilder builder = new StringBuilder();

        if (!StringUtils.isEmpty(node.getID()))
            builder.append("#").append(node.getID());
        else
            builder.append(node.getClass().getSimpleName());

        return builder.toString();
    }
}
