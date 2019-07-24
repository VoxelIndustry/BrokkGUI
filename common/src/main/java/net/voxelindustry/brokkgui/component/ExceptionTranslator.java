package net.voxelindustry.brokkgui.component;

import java.util.logging.Logger;

public class ExceptionTranslator
{
    private static Logger LOGGER = Logger.getLogger("BrokkGUI ExceptionTranslator");

    public static void createNullChildInElement(GuiElement element)
    {
        String message = String.format("A null child has been found in a GuiElement.\nThis exception is usually triggered by faulty code." +
                "\nGuiElement: ID=%s, TYPE=%s" +
                "\nLook for the following cases:" +
                "\n- A null Transform has been inserted in the child list of another one." +
                "\n- A child has been removed from a Transform during an event loop." +
                "\n  Like calling removeChild on an element of a Pane from the click action of a Button also contained in the Pane.", element.getId(), element.type());
        LOGGER.severe(message);

        throw new NullChildInElementException("Found a null child in a Transform child list.");
    }
}
