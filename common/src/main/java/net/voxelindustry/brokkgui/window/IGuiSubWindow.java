package net.voxelindustry.brokkgui.window;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;

/**
 * @author Ourten 31 oct. 2016
 */
public interface IGuiSubWindow
{
    void open();

    void close();

    float getxRelativePos();

    float getyRelativePos();

    Property<Float> getxRelativePosProperty();

    Property<Float> getyRelativePosProperty();

    float getWidth();

    float getHeight();

    <T extends HermodEvent> void addEventHandler(EventType<T> type, EventHandler<? super T> handler);

    <T extends HermodEvent> void removeEventHandler(EventType<T> type, EventHandler<? super T> handler);

    <T extends HermodEvent> void dispatchEventRedirect(EventType<T> type, T event);

    <T extends HermodEvent> void dispatchEvent(EventType<T> type, T event);

    GuiElement getRootElement();
}