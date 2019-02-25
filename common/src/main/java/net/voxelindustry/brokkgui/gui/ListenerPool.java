package net.voxelindustry.brokkgui.gui;

import fr.ourten.teabeans.listener.ListValueChangeListener;
import fr.ourten.teabeans.listener.ValueChangeListener;
import fr.ourten.teabeans.listener.ValueInvalidationListener;
import fr.ourten.teabeans.value.ListProperty;
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
        this.invalidationMap = new IdentityHashMap<>(8);
        this.valueChangeMap = new IdentityHashMap<>(8);
        this.listValueChangeMap = new IdentityHashMap<>(8);
    }

    public void attach(Observable obs, ValueInvalidationListener listener)
    {
        if (!this.invalidationMap.containsKey(obs))
            this.invalidationMap.put(obs, new ArrayList<>(4));
        this.invalidationMap.get(obs).add(listener);

        obs.addListener(listener);
    }

    public <T> void attach(ObservableValue<T> obs, ValueChangeListener<T> listener)
    {
        if (!this.valueChangeMap.containsKey(obs))
            this.valueChangeMap.put(obs, new ArrayList<>(4));
        this.valueChangeMap.get(obs).add(listener);

        obs.addListener(listener);
    }

    public <T> void attach(ListProperty<T> obs, ListValueChangeListener<T> listener)
    {
        if (!this.listValueChangeMap.containsKey(obs))
            this.listValueChangeMap.put(obs, new ArrayList<>(4));
        this.listValueChangeMap.get(obs).add(listener);

        obs.addListener(listener);
    }

    @SuppressWarnings("unchecked")
    public void clear()
    {
        this.invalidationMap.forEach((obs, listeners) ->
        {
            listeners.forEach(obs::removeListener);
            listeners.clear();
        });
        this.invalidationMap.clear();

        this.valueChangeMap.forEach((obs, listeners) ->
        {
            for (ValueChangeListener<?> listener : listeners)
                obs.removeListener((ValueChangeListener<Object>) listener);
            listeners.clear();
        });
        this.valueChangeMap.clear();

        this.listValueChangeMap.forEach((obs, listeners) ->
        {
            for (ListValueChangeListener<?> listener : listeners)
                obs.removeListener((ListValueChangeListener<Object>) listener);
            listeners.clear();
        });
        this.listValueChangeMap.clear();
    }
}
