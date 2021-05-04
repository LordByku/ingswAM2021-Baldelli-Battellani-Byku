package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.devCards.InvalidIdException;
import it.polimi.ingsw.model.playerBoard.resourceLocations.InvalidDepotSizeException;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.view.cli.TextColour;

/**
 * DepotLeaderCard represents all LeaderCards with a depot power.
 */

public class DepotLeaderCard extends LeaderCard{
    /**
     * the type of ConcreteResource that can be stored in the depot.
     */
    private final ConcreteResource type;
    /**
     * The size of the depot provided by this LeaderCard
     */
    private final int depotSize;

    /**
     * The constructor sets the parameters of the leader cards.
     * @param points victory points given by the leader card.
     * @param requirements requirements needed to play the leader card.
     * @param type type of resource that can be stored into the depot.
     * @param depotSize The size of the depot.
     * @throws InvalidPointsValueException points are less or equal to zero.
     * @throws InvalidRequirementsException requirements is null.
     * @throws InvalidResourceException type is null.
     * @throws InvalidDepotSizeException depotSize is not strictly positive
     */
    public DepotLeaderCard(int points, LeaderCardRequirements requirements, ConcreteResource type, int depotSize, int id)
            throws InvalidPointsValueException, InvalidRequirementsException, InvalidResourceException, InvalidDepotSizeException, InvalidIdException {
        super(points, requirements, id);
        if(type == null) {
            throw new InvalidResourceException();
        }
        if(depotSize <= 0) {
            throw new InvalidDepotSizeException();
        }

        this.type = type;
        this.depotSize = depotSize;
    }

    /**
     * Check if the card is playable and plays it. Adds a new LeaderCardDepot to the Warehouse.
     */
    @Override
    public void play() {
        if(isPlayable()) {
            active = true;
            board.getWarehouse().addLeaderCardDepot(new LeaderCardDepot(type, depotSize));
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(super.toString());
        StringBuilder effect = new StringBuilder(type.getColour().escape());
        for(int i = 0; i < depotSize; ++i) {
            effect.append("\u25ef ");
        }
        effect.append(TextColour.RESET);
        result.append("|");
        for(int i = 0; i < (width - effect.length()) / 2; ++i) {
            result.append(" ");
        }
        result.append(effect);
        for(int i = (width - effect.length()) / 2 + effect.length(); i < width; ++i) {
            result.append(" ");
        }
        result.append("|");
        return result.toString();
    }
}
