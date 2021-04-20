package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.resources.ChoiceSet;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidChoiceSetException;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

/**
 * Yellow is the subclass for yellow Marbles
 */
public class YellowMarble extends Marble {
    /**
     * The constructor builds a Marble with MarbleColour YELLOW
     */
    public YellowMarble() {
        super(MarbleColour.YELLOW);
    }

    /**
     * collect returns an ObtainableResourceSet containing a COIN
     * @param choiceSet The ChoiceSet of possible conversions for this operation
     * @return An ObtainableResourceSet containing a COIN
     * @throws InvalidChoiceSetException choiceSet is null
     */
    @Override
    public ObtainableResourceSet collect(ChoiceSet choiceSet) throws InvalidChoiceSetException {
        if(choiceSet == null) {
            throw new InvalidChoiceSetException();
        }
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.COIN);
        return new ObtainableResourceSet(choiceResourceSet);
    }
}
