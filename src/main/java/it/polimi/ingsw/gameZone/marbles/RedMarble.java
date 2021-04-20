package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.resources.ChoiceSet;
import it.polimi.ingsw.resources.InvalidChoiceSetException;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

/**
 * RedMarble is the subclass for red Marbles
 */
public class RedMarble extends Marble {
    /**
     * The constructor builds a Marble with MarbleColour RED
     */
    public RedMarble() {
        super(MarbleColour.RED);
    }

    /**
     * collect returns an ObtainableResourceSet containing a faith point
     * @param choiceSet The ChoiceSet of possible conversions for this operation
     * @return An ObtainableResourceSet containing a faith point
     * @throws InvalidChoiceSetException choiceSet is null
     */
    @Override
    public ObtainableResourceSet collect(ChoiceSet choiceSet) throws InvalidChoiceSetException {
        if(choiceSet == null) {
            throw new InvalidChoiceSetException();
        }
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();
        return new ObtainableResourceSet(choiceResourceSet, 1);
    }
}
