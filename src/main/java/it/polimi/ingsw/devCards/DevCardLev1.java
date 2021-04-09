package it.polimi.ingsw.devCards;

import it.polimi.ingsw.resources.ConcreteResourceSet;

/**
 * DevCardLev1 is a subclass of DevCard and represents development cards at level 1
 */
public class DevCardLev1 extends DevCard{

    DevCardLev1 (ConcreteResourceSet reqResources, CardColour colour){
        super(reqResources, colour);
        this.level=CardLevel.I;
    }
}
