package net.voxelindustry.brokkgui.gui;

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

    float xRelativePos();

    float yRelativePos();

    BaseProperty<Float> xRelativePosProperty();

    BaseProperty<Float> yRelativePosProperty();

    float width();

    float height();

    <T extends HermodEvent> void addEventHandler(EventType<T> type, EventHandler<? super T> handler);

    <T extends HermodEvent> void removeEventHandler(EventType<T> type, EventHandler<T> handler);

    void dispatchEventRedirect(EventType<? extends HermodEvent> type, HermodEvent event);

    void dispatchEvent(EventType<? extends HermodEvent> type, HermodEvent event);
}