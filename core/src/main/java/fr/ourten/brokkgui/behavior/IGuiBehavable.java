package fr.ourten.brokkgui.behavior;

import fr.ourten.teabeans.value.BaseProperty;

public interface IGuiBehavable<B extends GuiBehaviorBase<?>>
{
    default B getBehaviour()
    {
        return this.getBehaviourProperty().getValue();
    }

    public BaseProperty<B> getBehaviourProperty();

    public void setBehaviour(B value);
}