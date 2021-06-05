package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.devCards.InvalidIdException;
import it.polimi.ingsw.model.devCards.InvalidProductionDetailsException;
import it.polimi.ingsw.model.devCards.ProductionDetails;
import it.polimi.ingsw.view.gui.images.leaderCard.LeaderCardImage;
import it.polimi.ingsw.view.gui.images.leaderCard.ProductionLeaderCardImage;

import java.io.IOException;

/**
 * ProductionLeaderCard represents all LeaderCards with a production power.
 */
public class ProductionLeaderCard extends LeaderCard {
    /**
     * The ProductionDetails of the power provided by this LeaderCard
     */
    private final ProductionDetails productionPower;

    /**
     * The constructor sets the parameters of the leader cards.
     *
     * @param points            victory points given by the leader card.
     * @param requirements      requirements needed to play the leader card.
     * @param productionDetails Production power of this card
     * @throws InvalidPointsValueException       points are less or equal to zero.
     * @throws InvalidRequirementsException      requirements is null.
     * @throws InvalidProductionDetailsException productionDetails is null
     */
    public ProductionLeaderCard(int points, LeaderCardRequirements requirements, ProductionDetails productionDetails, int id)
            throws InvalidPointsValueException, InvalidRequirementsException, InvalidProductionDetailsException, InvalidIdException {
        super(points, requirements, id, LeaderCardType.PRODUCTION);
        if (productionDetails == null) {
            throw new InvalidProductionDetailsException();
        }

        this.productionPower = productionDetails.clone();
    }

    /**
     * Check if the card is playable and plays it. Adds a new ProductionDetails to the ProductionArea
     */
    @Override
    public void play() {
        if (isPlayable()) {
            active = true;
            board.getProductionArea().addLeaderCardProduction(productionPower);
        }
    }

    public ProductionDetails getProductionPower() {
        return productionPower;
    }

    @Override
    public String getEffectString() {
        return productionPower.getCLIString();
    }

    @Override
    public LeaderCardImage getLeaderCardImage(int width) {
        try {
            return new ProductionLeaderCardImage(this, width);
        } catch (IOException e) {
            return null;
        }
    }
}
