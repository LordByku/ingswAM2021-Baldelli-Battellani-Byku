package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidResourceException;

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
    public WhiteConversionLeaderCard(int points, LeaderCardRequirements requirements, ConcreteResource type) throws InvalidPointsValueException, InvalidRequirementsException, InvalidResourceException {

        if(points<=0){
            throw new InvalidPointsValueException();
        }
        if(requirements == null){
            throw new InvalidRequirementsException();
        }
        if(type == null){
            throw new InvalidResourceException();
        }

        this.points=points;
        this.requirements = (LeaderCardRequirements) requirements.clone();
        this.type=type;
    }

    /**
     * Check if the card is playable and plays it. Adds a new ConversionEffect to the ConversionEffectArea
     */
    @Override
    public void play() {
        if(isPlayable()){
            active = true;
            try {
                board.addConversionEffect(new ConversionEffect(this.type));
            } catch (InvalidResourceException e) {
                e.printStackTrace();
            }
        }
    }
}
