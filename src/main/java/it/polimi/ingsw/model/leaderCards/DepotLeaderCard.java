package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.devCards.InvalidIdException;
import it.polimi.ingsw.model.playerBoard.resourceLocations.InvalidDepotSizeException;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.view.cli.TextColour;
import it.polimi.ingsw.view.gui.images.leaderCard.DepotLeaderCardImage;
import it.polimi.ingsw.view.gui.images.leaderCard.LeaderCardImage;

import java.io.IOException;

/**
 * DepotLeaderCard represents all LeaderCards with a depot power.
 */

public class DepotLeaderCard extends LeaderCard {
    private final LeaderCardDepot depot;

    /**
     * The constructor sets the parameters of the leader cards.
     *
     * @param points       victory points given by the leader card.
     * @param requirements requirements needed to play the leader card.
     * @param type         type of resource that can be stored into the depot.
     * @param depotSize    The size of the depot.
     * @throws InvalidPointsValueException  points are less or equal to zero.
     * @throws InvalidRequirementsException requirements is null.
     * @throws InvalidResourceException     type is null.
     * @throws InvalidDepotSizeException    depotSize is not strictly positive
     */
    public DepotLeaderCard(int points, LeaderCardRequirements requirements, ConcreteResource type, int depotSize, int id)
            throws InvalidPointsValueException, InvalidRequirementsException, InvalidResourceException, InvalidDepotSizeException, InvalidIdException {
        super(points, requirements, id, LeaderCardType.DEPOT);
        this.depot = new LeaderCardDepot(type, depotSize);
    }

    /**
     * Check if the card is playable and plays it. Adds a new LeaderCardDepot to the Warehouse.
     */
    @Override
    public void play() {
        if (isPlayable()) {
            active = true;
            board.getWarehouse().addLeaderCardDepot(depot);
        }
    }

    public LeaderCardDepot getDepot() {
        return depot;
    }

    @Override
    public String getEffectString() {
        StringBuilder effect = new StringBuilder(depot.getResourceType().getColour().escape());
        for (int i = 0; i < depot.getSlots(); ++i) {
            if (i > 0) {
                effect.append(" ");
            }
            effect.append("\u25ef");
        }
        effect.append(TextColour.RESET);
        return effect.toString();
    }

    @Override
    public LeaderCardImage getLeaderCardImage(int width) {
        try {
            return new DepotLeaderCardImage(this, width);
        } catch (IOException e) {
            return null;
        }
    }
}
