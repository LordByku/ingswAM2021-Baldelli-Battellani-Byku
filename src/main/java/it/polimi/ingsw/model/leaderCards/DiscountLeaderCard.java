package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.devCards.InvalidIdException;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.view.gui.images.leaderCard.DiscountLeaderCardImage;
import it.polimi.ingsw.view.gui.images.leaderCard.LeaderCardImage;

import java.io.IOException;

/**
 * DiscountLeaderCard represents all LeaderCards with a discount power.
 */

public class DiscountLeaderCard extends LeaderCard {
    private final DiscountEffect discountEffect;

    /**
     * The constructor sets the parameters of the leader cards.
     *
     * @param points       victory points given by the leader card.
     * @param requirements requirements needed to play the leader card.
     * @param type         type of resource to discount.
     * @param discount     The amount of resources discounted
     * @throws InvalidPointsValueException  points are less or equal to zero.
     * @throws InvalidRequirementsException requirements is null.
     * @throws InvalidResourceException     type is null.
     * @throws InvalidDiscountException     discount is not strictly positive
     */
    public DiscountLeaderCard(int points, LeaderCardRequirements requirements, ConcreteResource type, int discount, int id)
            throws InvalidPointsValueException, InvalidRequirementsException, InvalidResourceException, InvalidDiscountException, InvalidIdException {
        super(points, requirements, id, LeaderCardType.DISCOUNT);
        discountEffect = new DiscountEffect(type, discount);
    }

    /**
     * Check if the card is playable and plays it. Adds a new discountEffect to the DiscountArea
     */
    @Override
    public void play() {
        if (isPlayable()) {
            board.getLeaderCardArea().removeLeaderCard(this);
            board.getLeaderCardArea().addLeaderCard(this);
            active = true;
            board.getDiscountArea().addDiscountEffect(discountEffect);
        }
    }

    public DiscountEffect getDiscountEffect() {
        return discountEffect;
    }

    @Override
    public String getEffectString() {
        return "???? -" + discountEffect.getDiscount() + discountEffect.getType().getCLIString();
    }

    @Override
    public LeaderCardImage getLeaderCardImage(int width) {
        try {
            return new DiscountLeaderCardImage(this, width);
        } catch (IOException e) {
            return null;
        }
    }
}
