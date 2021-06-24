package net.voxelindustry.brokkgui.window;

import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.hermod.EventHandler;
import net.voxelindustry.hermod.EventType;
import net.voxelindustry.hermod.HermodEvent;

import java.util.Collection;

/**
 * @author Ourten 31 oct. 2016
 */
public interface IGuiSubWindow
{
    void open();

    void close();

    float xRelativePos();

    float yRelativePos();

    Property<Float> xRelativePosProperty();

    Property<Float> yRelativePosProperty();

    float getWidth();

    float getHeight();

    <T extends HermodEvent> void addEventHandler(EventType<T> type, EventHandler<? super T> handler);

    <T extends HermodEvent> void removeEventHandler(EventType<T> type, EventHandler<? super T> handler);

    <T extends HermodEvent> void dispatchEventRedirect(EventType<T> type, T event);

    <T extends HermodEvent> void dispatchEvent(EventType<T> type, T event);

    GuiElement getRootElement();

    void addFloating(Transform transform);

    boolean removeFloating(Transform transform);

    Collection<Transform> getFloatingList();
}