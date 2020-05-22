package net.voxelindustry.brokkgui.window;

import fr.ourten.teabeans.value.BaseProperty;
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

    BaseProperty<Float> getxRelativePosProperty();

    BaseProperty<Float> getyRelativePosProperty();

    float getWidth();

    float getHeight();

    <T extends HermodEvent> void addEventHandler(EventType<T> type, EventHandler<? super T> handler);

    <T extends HermodEvent> void removeEventHandler(EventType<T> type, EventHandler<T> handler);

    void dispatchEventRedirect(EventType<? extends HermodEvent> type, HermodEvent event);

    void dispatchEvent(EventType<? extends HermodEvent> type, HermodEvent event);
}