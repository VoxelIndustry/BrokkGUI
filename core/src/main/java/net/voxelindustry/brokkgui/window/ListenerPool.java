package net.voxelindustry.brokkgui.window;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.listener.ValueChangeListener;
import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.value.Observable;
import fr.ourten.teabeans.value.ObservableValue;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class ListenerPool
{
    private IdentityHashMap<Observable, List<ValueInvalidationListener>>       invalidationMap;
    private IdentityHashMap<ObservableValue<?>, List<ValueChangeListener<?>>>  valueChangeMap;
    private IdentityHashMap<ListProperty<?>, List<ListValueChangeListener<?>>> listValueChangeMap;

    public ListenerPool()
    {
        invalidationMap = new IdentityHashMap<>(8);
        valueChangeMap = new IdentityHashMap<>(8);
        listValueChangeMap = new IdentityHashMap<>(8);
    }

    public void attach(Observable obs, ValueInvalidationListener listener)
    {
        if (!invalidationMap.containsKey(obs))
            invalidationMap.put(obs, new ArrayList<>(4));
        invalidationMap.get(obs).add(listener);

        obs.addListener(listener);
    }

    public <T> void attach(ObservableValue<T> obs, ValueChangeListener<T> listener)
    {
        if (!valueChangeMap.containsKey(obs))
            valueChangeMap.put(obs, new ArrayList<>(4));
        valueChangeMap.get(obs).add(listener);

        obs.addChangeListener(listener);
    }

    public <T> void attach(ListProperty<T> obs, ListValueChangeListener<T> listener)
    {
        if (!listValueChangeMap.containsKey(obs))
            listValueChangeMap.put(obs, new ArrayList<>(4));
        listValueChangeMap.get(obs).add(listener);

        obs.addChangeListener(listener);
    }

    public void clear()
    {
        invalidationMap.forEach((obs, listeners) ->
        {
            listeners.forEach(obs::removeListener);
            listeners.clear();
        });
        invalidationMap.clear();

        valueChangeMap.forEach((obs, listeners) ->
        {
            for (ValueChangeListener<?> listener : listeners)
                obs.removeChangeListener((ValueChangeListener<Object>) listener);
            listeners.clear();
        });
        valueChangeMap.clear();

        listValueChangeMap.forEach((obs, listeners) ->
        {
            for (ListValueChangeListener<?> listener : listeners)
                obs.removeChangeListener((ListValueChangeListener<Object>) listener);
            listeners.clear();
        });
        listValueChangeMap.clear();
    }
}
