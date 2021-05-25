package it.polimi.ingsw.model.gameZone.marbles;

import it.polimi.ingsw.model.resources.ChoiceSet;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidChoiceSetException;
import it.polimi.ingsw.model.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.model.resources.resourceSets.ObtainableResourceSet;

/**
 * BlueMarble is the subclass for blue Marbles
 */
public class BlueMarble extends Marble {
    /**
     * The constructor builds a Marble with MarbleColour BLUE
     */
    public BlueMarble() {
        super(MarbleColour.BLUE);
    }

    /**
     * collect returns an ObtainableResourceSet containing a SHIELD
     *
     * @param choiceSet The ChoiceSet of possible conversions for this operation
     * @return An ObtainableResourceSet containing a SHIELD
     * @throws InvalidChoiceSetException choiceSet is null
     */
    @Override
    public ObtainableResourceSet collect(ChoiceSet choiceSet) throws InvalidChoiceSetException {
        if (choiceSet == null) {
            throw new InvalidChoiceSetException();
        }
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.SHIELD);
        return new ObtainableResourceSet(choiceResourceSet);
    }
}
