package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.devCards.InvalidIdException;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.model.resources.resourceSets.InvalidQuantityException;
import it.polimi.ingsw.view.cli.Strings;

/**
 * DiscountLeaderCard represents all LeaderCards with a discount power.
 */

public class DiscountLeaderCard extends LeaderCard{
    /**
     * The type of ConcreteResource discounted.
     */
    private final ConcreteResource type;
    /**
     * The amount of resources discounted.
     */
    private final int discount;

    /**
     * The constructor sets the parameters of the leader cards.
     * @param points victory points given by the leader card.
     * @param requirements requirements needed to play the leader card.
     * @param type type of resource to discount.
     * @param discount The amount of resources discounted
     * @throws InvalidPointsValueException points are less or equal to zero.
     * @throws InvalidRequirementsException requirements is null.
     * @throws InvalidResourceException type is null.
     * @throws InvalidDiscountException discount is not strictly positive
     */
    public DiscountLeaderCard(int points, LeaderCardRequirements requirements, ConcreteResource type, int discount, int id)
            throws InvalidPointsValueException, InvalidRequirementsException, InvalidResourceException, InvalidDiscountException, InvalidIdException {
        super(points, requirements, id, LeaderCardType.DISCOUNT);
        if(type == null){
            throw new InvalidResourceException();
        }
        if(discount <= 0) {
            throw new InvalidQuantityException();
        }

        this.type = type;
        this.discount = discount;
    }

    /**
     * Check if the card is playable and plays it. Adds a new discountEffect to the DiscountArea
     */
    @Override
    public void play() {
        if(isPlayable()){
            active = true;
            board.getDiscountArea().addDiscountEffect(new DiscountEffect(this.type));
        }
    }

    @Override
    public String getEffectString() {
        return "???? -1" + type.getCLIString();
    }
}
