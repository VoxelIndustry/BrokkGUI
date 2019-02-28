package net.voxelindustry.brokkgui.behavior;

import fr.ourten.teabeans.value.BaseProperty;

public interface IGuiBehavable<B extends GuiBehaviorBase<?>>
{
    default B getBehaviour()
    {
        return this.getBehaviourProperty().getValue();
    }

    BaseProperty<B> getBehaviourProperty();

    void setBehaviour(B value);
}