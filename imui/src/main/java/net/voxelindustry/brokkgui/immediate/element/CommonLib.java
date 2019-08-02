package net.voxelindustry.brokkgui.immediate.element;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.internal.IGuiHelper;

public interface CommonLib {

    default IGuiHelper guiHelper() {
        return BrokkGuiPlatform.instance().guiHelper();
    }
}
