package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.resources.ChoiceSet;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidChoiceSetException;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

/**
 * PurpleMarble is the subclass for purple Marbles
 */
public class PurpleMarble extends Marble {
    /**
     * The constructor builds a Marble with MarbleColour PURPLE
     */
    public PurpleMarble() {
        super(MarbleColour.PURPLE);
    }

    /**
     * collect returns an ObtainableResourceSet containing a SERVANT
     * @param choiceSet The ChoiceSet of possible conversions for this operation
     * @return An ObtainableResourceSet containing a SERVANT
     * @throws InvalidChoiceSetException choiceSet is null
     */
    @Override
    public ObtainableResourceSet collect(ChoiceSet choiceSet) throws InvalidChoiceSetException {
        if(choiceSet == null) {
            throw new InvalidChoiceSetException();
        }
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.SERVANT);
        return new ObtainableResourceSet(choiceResourceSet);
    }
}
