package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.game.actionTokens.ActionTokenDeck;
import it.polimi.ingsw.model.gameZone.CardMarket;
import it.polimi.ingsw.model.playerBoard.faithTrack.FaithTrack;

/**
 * Computer is the class for the automated Player
 * in single player mode
 */
public class Computer extends Player {
    /**
     * faithTrack is the FaithTrack of the Computer
     */
    private final FaithTrack faithTrack;
    /**
     * deck is the ActionTokenDeck of the Computer
     */
    private ActionTokenDeck deck;

    /**
     * The constructor initializes deck to a new ActionTokenDeck
     */
    public Computer() {
        super(PlayerType.COMPUTER);
        faithTrack = FaithTrack.builder();
        deck = new ActionTokenDeck(faithTrack);
    }

    /**
     * startTurn executes the Computer's turn and ends the turn
     */
    @Override
    protected void startTurn() {
        deck = deck.flipFirstToken();
        endTurn();
    }

    /**
     * endTurn calls the turn handler method in Game
     */
    @Override
    public void endTurn() {
        Game.getInstance().handleTurnOrder();
    }

    /**
     * getFaithTrack returns the FaithTrack of the Computer
     *
     * @return The FaithTrack of the Computer
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public boolean hasWon() {
        CardMarket cardMarket = Game.getInstance().getGameZone().getCardMarket();
        return faithTrack.getMarkerPosition() == faithTrack.getFinalPosition() || cardMarket.hasEmptyColour();
    }
}
