package it.polimi.ingsw.leaderCards;

import it.polimi.ingsw.resources.ConcreteResource;
import it.polimi.ingsw.resources.InvalidResourceException;

/**
 * DiscountLeaderCard represents all LeaderCards with a discount power.
 */

public class DiscountLeaderCard extends LeaderCard{

    /**
     * The type of ConcreteResource discounted.
     */
    private final ConcreteResource type;

    /**
     * The constructor sets the parameters of the leader cards.
     * @param points victory points given by the leader card.
     * @param requirements needed to play the leader card.
     * @param type of resource to discount.
     * @throws InvalidPointsValueException points are less or equal to zero.
     * @throws InvalidRequirementsException requirements is null.
     * @throws InvalidResourceException type is null.
     */
    public DiscountLeaderCard(int points, LeaderCardRequirements requirements, ConcreteResource type)
            throws InvalidPointsValueException, InvalidRequirementsException, InvalidResourceException {

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
     * Check if the card is playable and plays it. Adds a new discountEffect to the DiscountArea
     */
    @Override
    public void play() {
        if(isPlayable()){
            active = true;
            board.addLeaderCard(this);
            try {
                board.addDiscountEffect(new DiscountEffect(this.type));
            } catch (InvalidResourceException e) {
                e.printStackTrace();
            }
        }
    }
}
