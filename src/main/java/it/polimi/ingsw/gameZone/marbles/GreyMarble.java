package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.resources.ChoiceSet;
import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidChoiceSetException;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

/**
 * GreyMarble is the subclass for grey Marbles
 */
public class GreyMarble extends Marble {
    /**
     * The constructor builds a Marble with MarbleColour GREY
     */
    public GreyMarble() {
        super(MarbleColour.GREY);
    }

    /**
     * collect returns an ObtainableResourceSet containing a STONE
     * @param choiceSet The ChoiceSet of possible conversions for this operation
     * @return An ObtainableResourceSet containing a STONE
     * @throws InvalidChoiceSetException choiceSet is null
     */
    @Override
    public ObtainableResourceSet collect(ChoiceSet choiceSet) throws InvalidChoiceSetException {
        if(choiceSet == null) {
            throw new InvalidChoiceSetException();
        }
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        choiceResourceSet.addResource(ConcreteResource.STONE);
        return new ObtainableResourceSet(choiceResourceSet);
    }
}
