package it.polimi.ingsw.gameZone.marbles;

import it.polimi.ingsw.resources.ChoiceResource;
import it.polimi.ingsw.resources.ChoiceSet;
import it.polimi.ingsw.resources.InvalidChoiceSetException;
import it.polimi.ingsw.resources.resourceSets.ChoiceResourceSet;
import it.polimi.ingsw.resources.resourceSets.ObtainableResourceSet;

/**
 * WhiteMarble is the subclass for white Marbles
 */
public class WhiteMarble extends Marble {
    /**
     * The constructor builds a Marble with MarbleColour WHITE
     */
    public WhiteMarble() {
        super(MarbleColour.WHITE);
    }

    /**
     * collect returns an empty ObtainableResourceSet if the given board has no ConversionEffects,
     * otherwise an ObtainableResourceSet containing a ChoiceResource where possible choices are
     * those of ConversionEffects
     * @param choiceSet The ChoiceSet of possible conversions for this operation
     * @return An empty ObtainableResourceSet or an ObtainableResourceSet containing a ChoiceResource
     * @throws InvalidChoiceSetException choiceSet is null
     */
    @Override
    public ObtainableResourceSet collect(ChoiceSet choiceSet) throws InvalidChoiceSetException {
        if(choiceSet == null) {
            throw new InvalidChoiceSetException();
        }
        ChoiceResourceSet choiceResourceSet = new ChoiceResourceSet();

        if(!choiceSet.empty()) {
            choiceResourceSet.addResource(new ChoiceResource(choiceSet));
        }

        return new ObtainableResourceSet(choiceResourceSet);
    }
}
