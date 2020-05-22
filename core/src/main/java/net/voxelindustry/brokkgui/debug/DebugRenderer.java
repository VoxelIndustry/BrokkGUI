package net.voxelindustry.brokkgui.debug;

import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.window.BrokkGuiScreen;
import net.voxelindustry.brokkgui.window.GuiCompositeWindow;
import org.apache.commons.lang3.StringUtils;

public class DebugRenderer
{
    public static DebugWindow wrap(BrokkGuiScreen brokkGuiScreen)
    {
        DebugWindow debugWindow = new DebugWindow(brokkGuiScreen);
        GuiCompositeWindow composite = new GuiCompositeWindow(brokkGuiScreen, debugWindow);
        composite.setInputEventFilter(debugWindow);
        debugWindow.setWrapper(brokkGuiScreen.getWrapper());
        brokkGuiScreen.getWrapper().setGuiWindow(composite);

        debugWindow.initGui();
        return debugWindow;
    }

    public static String getNodeName(GuiElement node)
    {
        StringBuilder builder = new StringBuilder();

        if (!StringUtils.isEmpty(node.id()))
            builder.append("#").append(node.id());
        else
            builder.append(node.getClass().getSimpleName());

        return builder.toString();
    }
}
