package net.voxelindustry.brokkgui.behavior;

import fr.ourten.teabeans.property.Property;

public interface IGuiBehavable<B extends GuiBehaviorBase<?>>
{
    default B getBehaviour()
    {
        return getBehaviourProperty().getValue();
    }

    Property<B> getBehaviourProperty();

    void setBehaviour(B value);
}