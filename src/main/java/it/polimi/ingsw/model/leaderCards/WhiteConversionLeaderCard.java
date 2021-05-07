package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.devCards.InvalidIdException;
import it.polimi.ingsw.model.gameZone.marbles.WhiteMarble;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;

/**
 * WhiteConversionLeaderCard represents all LeaderCards with a conversion power.
 */
public class WhiteConversionLeaderCard extends LeaderCard{

    /**
     * The type of ConcreteResource to convert the white marble into.
     */
    private final ConcreteResource type;

    /**
     * The constructor sets the parameters of the leader cards.
     * @param points victory points given by the leader card.
     * @param requirements needed to play the leader card.
     * @param type of resource to convert the white marble into.
     * @throws InvalidPointsValueException points are less or equal to zero.
     * @throws InvalidRequirementsException requirements is null.
     * @throws InvalidResourceException type is null.
     */
    public WhiteConversionLeaderCard(int points, LeaderCardRequirements requirements, ConcreteResource type, int id)
            throws InvalidPointsValueException, InvalidRequirementsException, InvalidResourceException, InvalidIdException {
        super(points, requirements, id);
        if(type == null){
            throw new InvalidResourceException();
        }

        this.type = type;
    }

    /**
     * Check if the card is playable and plays it. Adds a new ConversionEffect to the ConversionEffectArea
     */
    @Override
    public void play() {
        if(isPlayable()){
            active = true;
            board.getConversionEffectArea().addConversionEffect(new ConversionEffect(this.type));
        }
    }

    @Override
    public String getEffectString() {
        WhiteMarble whiteMarble = new WhiteMarble();
        return whiteMarble.getCLIString() + " = " + type.getCLIString();
    }
}
