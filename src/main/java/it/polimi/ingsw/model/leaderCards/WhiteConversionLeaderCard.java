package it.polimi.ingsw.model.leaderCards;

import it.polimi.ingsw.model.devCards.InvalidIdException;
import it.polimi.ingsw.model.gameZone.marbles.MarbleColour;
import it.polimi.ingsw.model.resources.ConcreteResource;
import it.polimi.ingsw.model.resources.InvalidResourceException;
import it.polimi.ingsw.view.gui.images.leaderCard.ConversionLeaderCardImage;
import it.polimi.ingsw.view.gui.images.leaderCard.LeaderCardImage;

import java.io.IOException;

/**
 * WhiteConversionLeaderCard represents all LeaderCards with a conversion power.
 */
public class WhiteConversionLeaderCard extends LeaderCard {
    /**
     * The type of ConcreteResource to convert the white marble into.
     */
    private final ConversionEffect conversionEffect;

    /**
     * The constructor sets the parameters of the leader cards.
     *
     * @param points       victory points given by the leader card.
     * @param requirements needed to play the leader card.
     * @param type         of resource to convert the white marble into.
     * @throws InvalidPointsValueException  points are less or equal to zero.
     * @throws InvalidRequirementsException requirements is null.
     * @throws InvalidResourceException     type is null.
     */
    public WhiteConversionLeaderCard(int points, LeaderCardRequirements requirements, ConcreteResource type, int id)
            throws InvalidPointsValueException, InvalidRequirementsException, InvalidResourceException, InvalidIdException {
        super(points, requirements, id, LeaderCardType.CONVERSION);
        conversionEffect = new ConversionEffect(type);
    }

    /**
     * Check if the card is playable and plays it. Adds a new ConversionEffect to the ConversionEffectArea
     */
    @Override
    public void play() {
        if (isPlayable()) {
            board.getLeaderCardArea().removeLeaderCard(this);
            board.getLeaderCardArea().addLeaderCard(this);
            active = true;
            board.getConversionEffectArea().addConversionEffect(conversionEffect);
        }
    }

    public ConversionEffect getConversionEffect() {
        return conversionEffect;
    }

    @Override
    public String getEffectString() {
        return MarbleColour.WHITE.getCLIString() + " = " + conversionEffect.getResource().getCLIString();
    }

    @Override
    public LeaderCardImage getLeaderCardImage(int width) {
        try {
            return new ConversionLeaderCardImage(this, width);
        } catch (IOException e) {
            return null;
        }
    }
}
